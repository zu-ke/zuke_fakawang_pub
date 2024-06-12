package com.zk.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zk.config.LanTuPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/*解读：
* 蓝兔支付加密签名类
* */
@Component
public class LanTuPaySignUtil {

    //商户id
    private static String mchId;
    //回调地址
    private static String notifyUrl;
    //商户密钥
    private static String key;

    //public static  String mchId = "1668330966";
    //public static final String notifyUrl = "https://43g8009n65.yicp.fun/wxpay/pay.action";
    //public static final String notifyUrl = "http://47.108.218.73:65507/wxpay/pay.action";
    //public static final String key = "4037f0af9c6d03476bb8bf69d53d4835";

    @Autowired
    public LanTuPaySignUtil(LanTuPayConfig lanTuPayConfig) {
        mchId = lanTuPayConfig.getMchId();
        notifyUrl = lanTuPayConfig.getNotifyUrl();
        key = lanTuPayConfig.getKey();
    }

    /**
     * 支付参数签名
     *
     * @param params     需要参与签名的参数
     * @param partnerKey 商户密钥
     * @return {String} 参数签名
     */
    public static String createSign(Map<String, Object> params, String partnerKey) {
        // 生成签名前先去除sign
        params.remove("sign");
        String stringA = packageSign(params, false);
        String stringSignTemp = stringA + "&key=" + partnerKey;
        return SecureUtil.md5(stringSignTemp).toUpperCase();
    }

    /**
     * 组装签名的字段
     *
     * @param params     参数
     * @param urlEncoder 是否urlEncoder
     * @return {String}
     */
    public static String packageSign(Map<String, Object> params, boolean urlEncoder) {
        // 先将参数以其参数名的字典序升序进行排序
        TreeMap<String, Object> sortedParams = new TreeMap<String, Object>(params);
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Object> param : sortedParams.entrySet()) {
            String value = String.valueOf(param.getValue());
            if (StrUtil.isBlank(value)) {
                continue;
            }
            if (first) {
                first = false;
            } else {
                sb.append("&");
            }
            sb.append(param.getKey()).append("=");
            if (urlEncoder) {
                try {
                    value = urlEncode(value);
                } catch (UnsupportedEncodingException e) {
                }
            }
            sb.append(value);
        }
        return sb.toString();
    }

    public static String urlEncode(String src) throws UnsupportedEncodingException {
        return URLEncoder.encode(src, "utf-8");
    }

    /*解读：
    * 生成订单号
    * */
    public static String generateOrderId(int length) {
        if (length < 6 || length > 32) {
            throw new IllegalArgumentException("Length must be between 6 and 32");
        }

        // 获取当前时间戳
        long currentTimeMillis = System.currentTimeMillis();

        // 生成UUID并替换掉"-"
        String randomUUID = UUID.randomUUID().toString().replaceAll("-", "");

        // 创建订单号
        String orderId = currentTimeMillis + randomUUID;

        // 确保订单号长度符合要求，如果超长则截断
        return orderId.length() > length ? orderId.substring(0, length) : orderId;
    }

    public static String startLanTu(String name, BigDecimal money, String outTradeNo, String email){
        //System.out.println(out_trade_no);
        Long stime = System.currentTimeMillis()/1000;
        //当前时间戳
        String timestamp = String.valueOf(stime);
        Map<String, Object> map = new HashMap<>();
        map.put("mch_id", mchId);
        map.put("out_trade_no", outTradeNo);
        map.put("total_fee", money);
        map.put("body", name);
        map.put("notify_url", notifyUrl);
        map.put("timestamp", timestamp);
        String sign = LanTuPaySignUtil.createSign(map, key);
        map.put("sign", sign);
        map.put("attach", email);
        //微信扫码支付接口地址
        String url = "https://api.ltzf.cn/api/wxpay/native";
        String result = HttpRequest.post(url).form(map).execute().body();
        //System.out.println(result);
        Integer code = 0;
        String msg = "";
        if (StrUtil.isBlank(result)) {
            msg = "API接口返回为空，请联系客服";
        }else{
            try{
                JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
                code = jsonObject.getInteger("code");
                if(code==0){
                    String qrcode = jsonObject.getJSONObject("data").getString("QRcode_url");
                    msg = qrcode;
                }else{
                    String msgstr = jsonObject.getString("msg");
                    msg = "返回失败：" + msgstr;
                }
            } catch (JSONException e) {
                msg = e.getMessage();
            } catch (Exception e) {
                msg = e.getMessage();
            }
        }
        //System.out.println("msg = " + msg);
        return code + "zukedog" + msg;
    }
}
