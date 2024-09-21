package com.zk.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zk.bean.Category;
import com.zk.bean.Order_;
import com.zk.bean.Product;
import com.zk.bean.Result;
import com.zk.service.CategoryService;
import com.zk.service.OrderService;
import com.zk.service.ProductService;
import com.zk.utils.LanTuPaySignUtil;
import com.zk.utils.YiPayUtil;
import com.zk.utils.fakaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Controller
@Slf4j
public class UserController {

    @Resource
    private OrderService orderService;

    @Resource
    private YiPayUtil yiPayUtil;

    @Resource
    private fakaUtils utils;

    @Resource
    private ProductService productService;

    @Resource
    private CategoryService categoryService;

    @PostMapping("/saveO")
    @ResponseBody
    public Result saveO(@Validated @RequestBody Order_ order, Errors errors) {
        StringBuilder validate = fakaUtils.validate(errors);
        if (validate.length() > 0) {
            return Result.error("1001", validate.toString());
        }
        /*解读：调起蓝兔支付*/
        String name = "zukedog";
        //商户订单号，只能是数字、大小写字母_-且在同一个商户号下唯一。
        String outTradeNo = LanTuPaySignUtil.generateOrderId(16);
        String res = LanTuPaySignUtil.startLanTu(name, order.getAmount(), outTradeNo, order.getName());
        String[] split = res.split("zukedog");
        Integer code = Integer.parseInt(split[0]);
        String msg = split[1];
        if (code == 0){
            order.setName("2024.2.1ZKGPTAPI" + order.getName());
            order.setOutTradeNo(outTradeNo);
            order.setStatus("0");
            order.setCreateTime(new Date());
            order.setUpdateTime(new Date());
            orderService.save(order);
            return Result.success(msg);
        }else {
            return Result.error(code + "", msg);
        }

        /*解读：调起支付易支付*/
        /*JSONObject jsonObject = payUtil.toImGES("2024.2.1ZKGPTAPI" + order.getName(), order.getAmount());
        String code = jsonObject.getStr("code");
        if ("200".equals(code)) {
            String tradeNo = jsonObject.getStr("trade_no");
            String outTradeNo = jsonObject.getStr("out_trade_no");

            String codeUrl = jsonObject.getStr("code_url");

            order.setName("2024.2.1ZKGPTAPI" + order.getName());


            order.setStatus("0");
            order.setCreateTime(new Date());
            order.setUpdateTime(new Date());
            order.setTradeNo(tradeNo);
            order.setOutTradeNo(outTradeNo);
            orderService.save(order);
            return Result.success(codeUrl);
        } else {
            String msg = jsonObject.getStr("msg");
            return Result.error(code, msg);
        }*/
    }

    //易支付回调
    @GetMapping("/notify_url.php")
    @ResponseBody
    public void notify_(@RequestParam Map<String, String> map) {
        log.info("notify_");
        try {
            String email = map.get("name").split("ZKGPTAPI")[1];
            log.info(email);
            if ("TRADE_SUCCESS".equals(map.get("trade_status"))) {
                String money = map.get("money");
                QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
                queryWrapper.orderByDesc("id");
                queryWrapper.last("limit 1");
                queryWrapper.select("id", "gpt_key", "category_id");
                queryWrapper.eq("price", money);
                Product product = productService.getOne(queryWrapper);
                log.info("product = {}", product);
                if (product != null) {
                    utils.sendEmail(email, product.getGptKey());

                    UpdateWrapper<Order_> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("trade_no", map.get("trade_no"));
                    Order_ order = new Order_();
                    order.setProductId(product.getGptKey());
                    order.setStatus("1");
                    order.setTradeStatus(map.get("trade_status"));
                    orderService.update(order, updateWrapper);

                    UpdateWrapper<Category> updateWrapper1 = new UpdateWrapper<>();
                    updateWrapper1.eq("id", product.getCategoryId()).setSql("stock = stock - 1");
                    categoryService.update(updateWrapper1);

                    productService.removeById(product.getId());
                } else {
                    utils.sendEmailError(email);
                }
            } else {
                utils.sendEmailError(email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/return_url.php")
    @ResponseBody
    public String redirect_(@RequestBody String redirect) throws Exception {
        System.out.println("return_url = " + redirect);
        utils.sendEmail("15394809658@163.com", "zzz");
        utils.sendEmailError("15394809658@163.com");
        return "ok";
    }

    /*解读：
    * 蓝兔支付回调
    * */
    @PostMapping("/wxpay/pay.action")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> notify_LanTu(@RequestParam Map<String, String> map) {
        log.info("支付回调参数：{}", map);
        try {
            String email = map.get("attach");
            String code = map.get("code");
            String outTradeNo = map.get("out_trade_no");
            String totalFee = map.get("total_fee");

            // 参数校验
            if (email == null || code == null || outTradeNo == null) {
                log.warn("支付回调参数缺失");
                return ResponseEntity.badRequest().body("Invalid parameters");
            }

            // 检查订单是否已处理
            Order_ existingOrder = orderService.getOne(new QueryWrapper<Order_>()
                    .eq("out_trade_no", outTradeNo)
                    .eq("status", 1));
            if (existingOrder != null) {
                log.info("订单已处理，订单号：{}", outTradeNo);
                return ResponseEntity.ok("SUCCESS");
            }

            if ("0".equals(code)) {
                // **支付成功的处理**

                // 获取商品信息
                BigDecimal money = new BigDecimal(totalFee);
                Product product = productService.getOne(new QueryWrapper<Product>()
                        .eq("price", money)
                        .orderByDesc("id")
                        .last("limit 1")
                        .select("id", "gpt_key", "category_id"));

                if (product == null) {
                    log.warn("未找到对应价格的商品，金额：{}", money);
                    // 发送支付失败的邮件
                    utils.sendEmailError(email);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR");
                }

                // 更新订单信息
                Order_ order = new Order_();
                order.setProductId(product.getGptKey());
                order.setStatus("1");
                order.setTradeStatus(code);
                order.setTradeNo(map.get("order_no"));
                orderService.update(order, new UpdateWrapper<Order_>().eq("out_trade_no", outTradeNo));

                // 扣减库存
                boolean updateCount = categoryService.update(new UpdateWrapper<Category>()
                        .eq("id", product.getCategoryId())
                        .setSql("stock = stock - 1"));
                if (!updateCount) {
                    throw new RuntimeException("库存扣减失败");
                }

                // 删除商品
                productService.removeById(product.getId());

                // 发送支付成功的邮件
                try {
                    utils.sendEmail(email, product.getGptKey());
                } catch (Exception e) {
                    log.error("邮件发送失败：", e);
                    // 可根据业务需求决定是否需要回滚事务
                }

                return ResponseEntity.ok("SUCCESS");
            } else {
                // **支付失败的处理**

                // 记录支付失败的订单信息
                Order_ order = new Order_();
                order.setStatus("0");
                order.setTradeStatus(code);
                order.setTradeNo(map.get("order_no"));
                orderService.update(order, new UpdateWrapper<Order_>().eq("out_trade_no", outTradeNo));

                // 发送支付失败的邮件
                try {
                    utils.sendEmailError(email);
                } catch (Exception e) {
                    log.error("邮件发送失败：", e);
                    // 可根据业务需求决定是否需要特殊处理
                }

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR");
            }
        } catch (Exception e) {
            log.error("支付回调处理异常：", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR");
        }
    }
}
