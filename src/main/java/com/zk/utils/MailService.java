package com.zk.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    // 创建一个固定大小的线程池，用于异步发送邮件
    private ExecutorService executorService;

    @PostConstruct
    public void init() {
        //初始化线程池
        executorService = Executors.newFixedThreadPool(5);
    }

    /**
     * 发送文本邮件（异步）
     * @param to 接收人
     * @param subject 主题
     * @param content 邮件内容
     */
    public void sendSimpleMail(String to, String subject, String content) {
        executorService.submit(() -> {
            try {
                // 参数验证
                if (to == null || to.isEmpty()) {
                    logger.error("邮件接收人为空");
                    return;
                }
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(to);
                message.setSubject(subject);
                message.setText(content);
                message.setFrom(from);
                mailSender.send(message);
                logger.info("简单邮件已发送。收件人：{}", to);
            } catch (MailException e) {
                logger.error("发送简单邮件时发生异常！收件人：{}，错误信息：{}", to, e.getMessage());
                // 可在此处增加重试机制
            } catch (Exception e) {
                logger.error("发送简单邮件时发生未知异常！收件人：{}，错误信息：{}", to, e.getMessage());
            }
        });
    }

    /**
     * 发送HTML邮件（异步）
     */
    public void sendHtmlMail(String to, String subject, String content) {
        executorService.submit(() -> {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            try {
                // 参数验证
                if (to == null || to.isEmpty()) {
                    logger.error("邮件接收人为空");
                    return;
                }
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                helper.setFrom(from);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(content, true);
                mailSender.send(mimeMessage);
                logger.info("HTML邮件已发送。收件人：{}", to);
            } catch (MessagingException e) {
                logger.error("构建HTML邮件时发生异常！收件人：{}，错误信息：{}", to, e.getMessage());
            } catch (MailException e) {
                logger.error("发送HTML邮件时发生异常！收件人：{}，错误信息：{}", to, e.getMessage());
                // 可在此处增加重试机制
            } catch (Exception e) {
                logger.error("发送HTML邮件时发生未知异常！收件人：{}，错误信息：{}", to, e.getMessage());
            }
        });
    }

    /**
     * 发送带附件的邮件（异步）
     */
    public void sendAttachmentMail(String to, String subject, String content, String filepath) {
        executorService.submit(() -> {
            MimeMessage message = mailSender.createMimeMessage();
            try {
                // 参数验证
                if (to == null || to.isEmpty()) {
                    logger.error("邮件接收人为空");
                    return;
                }
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setFrom(from);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(content, true);

                // 文件验证
                File file = new File(filepath);
                if (!file.exists()) {
                    logger.error("附件文件不存在，路径：{}", filepath);
                    return;
                }
                FileSystemResource fileResource = new FileSystemResource(file);
                String filename = fileResource.getFilename();
                helper.addAttachment(filename, fileResource);

                mailSender.send(message);
                logger.info("带附件的邮件已发送。收件人：{}", to);
            } catch (MessagingException e) {
                logger.error("构建带附件的邮件时发生异常！收件人：{}，错误信息：{}", to, e.getMessage());
            } catch (MailException e) {
                logger.error("发送带附件的邮件时发生异常！收件人：{}，错误信息：{}", to, e.getMessage());
                // 可在此处增加重试机制
            } catch (Exception e) {
                logger.error("发送带附件的邮件时发生未知异常！收件人：{}，错误信息：{}", to, e.getMessage());
            }
        });
    }

    /**
     * 发送图片邮件（异步）
     */
    public void sendImageMail(String to, String subject, String content, String rscPath, String rscId) {
        executorService.submit(() -> {
            logger.info("开始发送图片邮件：收件人：{}，主题：{}", to, subject);
            MimeMessage message = mailSender.createMimeMessage();
            try {
                // 参数验证
                if (to == null || to.isEmpty()) {
                    logger.error("邮件接收人为空");
                    return;
                }
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setFrom(from);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(content, true);

                // 资源文件验证
                File file = new File(rscPath);
                if (!file.exists()) {
                    logger.error("内嵌资源文件不存在，路径：{}", rscPath);
                    return;
                }
                FileSystemResource res = new FileSystemResource(file);
                helper.addInline(rscId, res);

                mailSender.send(message);
                logger.info("发送图片邮件成功。收件人：{}", to);
            } catch (MessagingException e) {
                logger.error("构建图片邮件时发生异常！收件人：{}，错误信息：{}", to, e.getMessage());
            } catch (MailException e) {
                logger.error("发送图片邮件时发生异常！收件人：{}，错误信息：{}", to, e.getMessage());
                // 可在此处增加重试机制
            } catch (Exception e) {
                logger.error("发送图片邮件时发生未知异常！收件人：{}，错误信息：{}", to, e.getMessage());
            }
        });
    }

    // 销毁线程池
    public void shutdown() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
