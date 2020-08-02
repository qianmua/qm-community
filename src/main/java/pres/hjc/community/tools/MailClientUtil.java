package pres.hjc.community.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  14:59
 * @description :
 */
@Component
@Slf4j
public class MailClientUtil {

    @Autowired
    private JavaMailSender mailSender;

    @Value("spring.mail.username")
    private String from;

    /**
     * 发送邮件
     * @param to to
     * @param sub sub
     * @param content content
     */
    public void sendMail(String to , String sub , String content){
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(sub);
            helper.setText(content, true);
            // send
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            log.error("send mail fail.." + e.getMessage());
        }
    }


}
