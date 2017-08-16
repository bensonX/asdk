package org.alan.asdk.service;

import org.alan.asdk.common.Log;
import org.alan.asdk.entity.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service("mailManager")
public class MailManager {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TaskExecutor taskExecutor;


    /**
     * 通过异步方式发送邮件
     * @param mail
     */
    public void sendMailByAsynchronousMode(final Mail mail){
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    sendMailBySynchronizationMode(mail);
                } catch (MessagingException e) {
                    Log.e("邮件发送错误",e);
                }
            }
        });
    }

    /**
     * 通过同步方式发送邮件
     */
    public void sendMailBySynchronizationMode(Mail mail) throws MessagingException {
        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime);
        helper.setFrom("tsdk.game@dao.tsixi.com");
        helper.setTo(mail.getToAddress());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getContent(),true);
        mailSender.send(mime);
        Log.i("向["+mail.getToAddress()+"] 发送主题["+mail.getSubject()+"] 完成");
    }


    /**
     * 发送带有模板的邮件(异步)
     * @param mail 邮件内容
     */
    public void sendMailByTemplate(Mail mail){
        String content = "<style class=\"fox_global_style\">div.fox_html_content { line-height: 1.5; }div.fox_html_content { font-size: 10.5pt; font-family: 微软雅黑; color: rgb(51, 51, 51); line-height: 1.5; }</style><div style=\"background:#fbfbfb;width:96%;margin:0 2%; border: 1px solid #e1e1e1;font-size:11pt;\"> <div style=\"border-bottom: 1px solid #e1e1e1; padding:20px;\">Tsixi Games</div><div style=\"border-bottom: 1px solid #e1e1e1; padding:20px;\">"+mail.getContent()+"</div> <div style=\"background:#f9f9f7;border-top: 1px solid #fff; padding-left:20px;padding-right:20px;\"> <span style=\"color:#aaa;float:left; height:40px;line-height:40px;\">Tsixi</span> <span style=\"color:#aaa;float:right; height:40px;line-height:40px;font-family:Bradley Hand ITC\"></span> <div style=\"clear:both;\"></div> </div> </div> <br>";
        mail.setContent(content);
        sendMailByAsynchronousMode(mail);
    }

}
