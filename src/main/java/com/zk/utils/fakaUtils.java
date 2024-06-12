package com.zk.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class fakaUtils {

    @Resource
    private MailService mailService;

    public void sendEmail(String email, String gptKey) throws Exception {
        String content = "<html>\n" +
                "<body>\n" +
                "<h3 style=\"color: deepskyblue\">感谢您的支持，下面是您的兑换码</h3>\n" +
                "<h1 style=\"color: red\">"+gptKey+"</h1>\n" +
                "</body>\n" +
                "</html>";

        mailService.sendHtmlMail(email, "zkGPT兑换码", content);
    }

    public void sendEmailError(String email) throws Exception {
        String content = "<html>\n" +
                "<body>\n" +
                "<h1 style=\"color: red\">非常抱歉，支付失败，请联系管理员微信：<span style=\"color: deepskyblue\">xlmdlF</span></h1>\n" +
                "</body>\n" +
                "</html>";

        mailService.sendHtmlMail(email, "zkGPT兑换码购买失败", content);
    }

    public static StringBuilder validate(Errors errors) {
        HashMap<String, Object> errorsMap = new HashMap<>();
        List<FieldError> fieldErrors = errors.getFieldErrors();
        for (FieldError error : fieldErrors) {
            errorsMap.put(error.getField(), error.getDefaultMessage());
        }
        StringBuilder res = new StringBuilder();
        res.append("");
        if (!errorsMap.isEmpty()) {
            for (Map.Entry<String, Object> entry : errorsMap.entrySet()) {
                res.append(entry.getValue() + "\n");
            }
        }
        return res;
    }

    public static String encrypt(String source) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(source.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) sb.append('0');
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
