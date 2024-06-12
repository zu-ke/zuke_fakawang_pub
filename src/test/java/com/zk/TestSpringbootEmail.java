package com.zk;

import com.zk.utils.MailService;
import com.zk.utils.fakaUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
public class TestSpringbootEmail {

    @Resource
    MailService mailService;

    @Resource
    fakaUtils utils;

    /**
     *  简单文本邮件发送
     */
    @Test
    public void sendSimpleMailTest(){
        mailService.sendSimpleMail("15394809658@163.com","简单文本邮件","这是我的第一封邮件,哈哈...");
    }

    @Test
    public void t() throws Exception {
        utils.sendEmail("15394809658@163.com","zzz");
    }

    /**
     *  HTML邮件发送
     *
     * @throws Exception
     */
    @Test
    public void sendHtmlMailTest() throws Exception{

        String content = "<html>\n" +
                "<body>\n" +
                "<h3 style=\"color: deepskyblue\">感谢您的支持，下面是您的兑换码</h3>\n" +
                "<h1 style=\"color: red\"></h1>\n" +
                "</body>\n" +
                "</html>";


        mailService.sendHtmlMail("15394809658@163.com","zkGPT兑换码",content);
    }

    /**
     *  发送副本邮件
     *
     * @throws Exception
     */
    @Test
    public void sendAttachmentMailTest() throws Exception{
        String filepath = "/Users/fish/Desktop/linux 常用命令.pdf";

        mailService.sendAttachmentMail("15394809658@163.com","发送副本","这是一篇带附件的邮件",filepath);

    }

    /**
     *  发送图片邮件
     *
     * @throws Exception
     */
    @Test
    public void sendImageMailTest() throws Exception{
        //发送多个图片的话可以定义多个 rscId,定义多个img标签

        String filePath = "/Users/fish/Desktop/test.png";
        String rscId = "ligh001";
        String content = "<html><body> 这是有图片的邮件: <img src=\'cid:"+rscId+"\'> </img></body></html>";

        mailService.sendImageMail("15394809658@huluwa.cc","这是一个带图片的邮件",content,filePath,rscId);
    }



}
