package com.zk.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//易支付
@Component
public class YiPayUtil {

    // 商户ID
    @Value("${yipay.ID}")
    private String pid;
    // 支付方式
    @Value("${yipay.pay_type}")
    private String type;
    // 服务器异步通知地址
    @Value("${yipay.notify_url}")
    private String notify_url;
    // 页面跳转通知地址
    @Value("${yipay.return_url}")
    private String return_url;
    // 设备类型
    @Value("${yipay.device}")
    private String device;
    //加密默认MD5
    @Value("${yipay.sign_type}")
    private String sign_type;
    //API接口支付
    @Value("${yipay.api_url}")
    private String url;
    //私钥
    @Value("${yipay.key}")
    private String key;

    private String sign;

    public String getSign(){
        return sign;
    }

    public JSONObject toImGES(String name, BigDecimal money) {
        //pid={商户ID}&type={支付方式}&out_trade_no={商户订单号}&notify_url={服务器异步通知地址}&return_url={页面跳转通知地址}&name={商品名称}&money={金额}&clientip={用户IP地址}&device={设备类型}&sign={签名字符串}&sign_type=MD5
        String out_trade_no  = UUID.randomUUID().toString() + System.currentTimeMillis();;
        String clientip = "0.0.0.0";

        //[clientip, device, money, name, notify_url, out_trade_no, pid, return_url, sign_type, type]

        //封装好我们的请求参数
        String mdString = "clientip=" + clientip + "&device=" + device + "&money=" + money + "&name=" + name + "&" +
                "notify_url=" + notify_url + "&out_trade_no=" + out_trade_no + "&pid=" + pid + "&return_url=" + return_url +
                "&type=" + type + key;

        //把我们的请求参数 md5加密
        sign = MD5Util.GetMD5Code(mdString);

        //封装我们的请求参数 调用的api  是我们
        Map<String, Object> map = new HashMap<>();
        map.put("clientip", clientip);
        map.put("device", device);
        map.put("money", money);
        map.put("name", name);
        map.put("notify_url", notify_url);
        map.put("out_trade_no", out_trade_no);
        map.put("pid", pid);
        map.put("return_url", return_url);
        map.put("sign_type", sign_type);
        map.put("type", type);
        map.put("sign", sign);
        String body = HttpUtil.post(url, map);
        return new JSONObject(body);
        //JSONObject jsonObject = new JSONObject(body);
        //System.out.println(jsonObject);
        //拿到我们的返回的wx支付的url  转成二维码显示到前端
        //String wxUrl = (String) jsonObject.get("qrcode");
        //try {
        //    QrConfig config = new QrConfig(300, 300);
        //    QrCodeUtil.generate(wxUrl, config, "png", response.getOutputStream());
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
    }
}
