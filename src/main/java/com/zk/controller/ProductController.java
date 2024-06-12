package com.zk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zk.bean.Category;
import com.zk.bean.Product;
import com.zk.bean.Result;
import com.zk.service.CategoryService;
import com.zk.service.ProductService;
import com.zk.utils.fakaUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Controller
public class ProductController {

    @Resource
    private ProductService productService;

    @Resource
    private CategoryService categoryService;

    @PostMapping("/admin/savePdct")
    @ResponseBody
    public Result savePdct(@Validated @RequestBody Product product, Errors errors) {
        StringBuilder validate = fakaUtils.validate(errors);
        if (validate.length() > 0) {
            return Result.error("1001", validate.toString());
        }
        product.setCreateTime(new Date());
        product.setUpdateTime(new Date());
        String cname = categoryService.getById(product.getCategoryId()).getName();
        product.setCategoryName(cname);
        productService.save(product);

        UpdateWrapper<Category> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", product.getCategoryId()).setSql("stock = stock + 1");
        categoryService.update(updateWrapper);
        return Result.success();
    }

    @PostMapping("/admin/delPdct")
    @ResponseBody
    public Result delPdct(@RequestParam("pid") String pid) {
        if (!StringUtils.hasText(pid)) {
            return Result.error("1001", "未能确定需要删除的分类");
        }
        Product product = productService.getById(pid);
        productService.removeById(pid);

        UpdateWrapper<Category> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", product.getCategoryId()).setSql("stock = stock - 1");
        categoryService.update(updateWrapper);
        return Result.success();
    }

    @PostMapping("/admin/upPdct")
    @ResponseBody
    public Result upPdct(@Validated @RequestBody Product product, Errors errors) {
        StringBuilder validate = fakaUtils.validate(errors);
        if (validate.length() > 0) {
            return Result.error("1001", validate.toString());
        }
        if (!StringUtils.hasText(product.getId().toString())) {
            return Result.error("1001", "请正确输入商品信息");
        }
        String cname = categoryService.getById(product.getCategoryId()).getName();
        product.setCategoryName(cname);
        product.setUpdateTime(new Date());
        productService.saveOrUpdate(product);
        return Result.success();
    }

    @GetMapping("/admin/listByCidPdct")
    @ResponseBody
    public Result listByCidPdct(@RequestParam("cid") String cid) {
        if (!StringUtils.hasText(cid)) {
            return Result.error("1001", "请正确输入分类信息");
        }
        QueryWrapper<Product> queryWrapper = Wrappers.query();
        queryWrapper.eq("category_id", cid);
        List<Product> productList = productService.list(queryWrapper);
        return Result.success(productList);
    }

    @GetMapping("/admin/listByidPdct")
    @ResponseBody
    public Result listByidPdct(@RequestParam("pid") String pid) {
        if (!StringUtils.hasText(pid)) {
            return Result.error("1001", "请正确输入分类信息");
        }
        Product product = productService.getById(pid);
        return Result.success(product);
    }

    @GetMapping("/admin/listPdct")
    @ResponseBody
    public Result listPdct() {
        List<Product> productList = productService.list();
        return Result.success(productList);
    }

    @GetMapping("/admin/listByPagePdct")
    @ResponseBody
    public Result listByPagePdct(@RequestParam("pageSize") Integer pageSize,
                                 @RequestParam("pageNum") Integer pageNum,
                                 @RequestParam(value = "categoryId", defaultValue = "") String categoryId,
                                 @RequestParam(value = "searchName", defaultValue = "") String searchName) {
        LambdaQueryWrapper<Product> lambdaQueryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.hasLength(categoryId)) {
            lambdaQueryWrapper.eq(Product::getCategoryId, categoryId);
        }
        if (StringUtils.hasLength(searchName)) {
            lambdaQueryWrapper.like(Product::getName, searchName);
        }
        Page<Product> page = productService.page(new Page<>(pageNum, pageSize), lambdaQueryWrapper);
        return Result.success(page);
    }

    @GetMapping("/listByCidPdct")
    @ResponseBody
    public Result listByCidPdctU(@RequestParam("cid") String cid) {
        if (!StringUtils.hasText(cid)) {
            return Result.error("1001", "请正确输入分类信息");
        }
        QueryWrapper<Product> queryWrapper = Wrappers.query();
        queryWrapper.eq("category_id", cid)
                .select("name", "id", "price"); // 只选择特定字段
        List<Product> productList = productService.list(queryWrapper);
        return Result.success(productList);
    }

    @GetMapping("/listPdctU")
    @ResponseBody
    public Result listPdctU() {
        QueryWrapper<Product> queryWrapper = Wrappers.query();
        queryWrapper.select("name", "id", "price"); // 只选择特定字段
        List<Product> productList = productService.list(queryWrapper);
        return Result.success(productList);
    }


}
