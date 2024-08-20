package com.xi.fmcs.support.util;

import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

@UtilityClass
public class MailUtil {

    @Value("${MAIL}")
    private static String mail;
    @Value("${SMTP}")
    private static String smtp;
    @Value("${PWD}")
    private static String pwd;
    @Value("${SSL}")
    private static String ssl;


    // 비밀번호 찾기 메일 발송
    public boolean SendPasswordMail(String toMail, String randomPwd) {
        try {
            //메일 본문 내용
            StringBuilder sbBody = new StringBuilder();
            String randomPwdBody = "임시비밀번호 : " + randomPwd;
            return SendMail(toMail, "비밀번호 찿기", randomPwdBody);
        } catch (Exception ex) {
            LogUtil.writeLog("[SendPasswordMail] " + ex.getMessage());
            return false;
        }
    }

    public boolean SendMail(String to, String subject, String body) {
        try {
            JavaMailSenderImpl sender = new JavaMailSenderImpl();
            sender.setHost(smtp);
            sender.setUsername(mail);
            sender.setPassword(pwd);
            sender.setPort(587);

            if (ssl.equals("TRUE")) {
                sender.setProtocol("smtps");
            }

            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            sender.send(message);

            return true;
        } catch (Exception ex) {
            LogUtil.writeLog("[SendMail] " + ex.getMessage());
            return false;
        }
    }


    public boolean SendMailV2(String fromMail, String fromName, String toMail, String subject
            , String nextDate, String randomVal, HttpServletRequest request) {
        try {
                /*
                mail_32 : 수신동의
                mail_33 : 미동의
                mail_34 : 수정시철회했을경우
                */

            String httpHost = request.getServerName();
            JavaMailSenderImpl client = new JavaMailSenderImpl();
            Properties props = client.getJavaMailProperties();


            // 메일 발송 분기
            if (httpHost.contains("midashelp.com") || httpHost.contains("localhost")
                    || httpHost.contains("127.0.0.1") || httpHost.contains("localfmcs.pitapat.net")) {
                // 마이다스 혹은 개발컴퓨터에서 테스트시 메일 발송
                client.setHost("smtp.gmail.com");
                client.setPort(587);
                client.setUsername("pitapat2020@gmail.com");
                client.setPassword("vlxjvoxm2020");

                props.put("mail.transport.protocol", "smtp");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
            } else {
                // 자이 실서버
                client.setHost("spam.gsconst.co.kr");
                client.setPort(25);

                props.put("mail.transport.protocol", "smtp");
                props.put("mail.smtp.auth", "false");
                props.put("mail.smtp.starttls.enable", "false");
            }

            // 추후 현재 위치와 타입에 따른 메일 주소를 바꿔줘야한다.
            String mailBodyFile = System.getProperty("user.dir") + "/Content/mailForm/AuthCode.html";
            String readMailBody = new String(Files.readAllBytes(Paths.get(mailBodyFile)));

            // 기존 인증 메일 주소 방식
            // [serverHost]/brand/login/join_proc.aspx?rnm=[RANDOMNUMBER]&em=[USEREMAILID]
            String urlScheme = request.getRequestURL().toString();

            //www.xi.co.kr / files
            //readMailBody = readMailBody.Replace("[serverHost]", urlScheme + "://" + HttpContext.Current.Request.ServerVariables["HTTP_HOST"]);
            readMailBody = readMailBody.replace("[serverHost]", urlScheme + "://www.xi.co.kr/files/fmcs");
            readMailBody = readMailBody.replace("[AUTH_NUM]", randomVal);


            //// 마이다스 혹은 개발컴퓨터에서 테스트시 메일 발송
            //client = new SmtpClient("smtp.gmail.com", 587);
            //client.UseDefaultCredentials = false;
            //client.EnableSsl = true;
            //client.DeliveryMethod = SmtpDeliveryMethod.Network; // 구글메일 인증시 사용
            //client.Credentials = new System.Net.NetworkCredential("pitapat2020@gmail.com", "vlxjvoxm2020");

            MimeMessage message = client.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromMail, fromName);
            helper.setTo(toMail);
            helper.setSubject(subject);
            helper.setText(readMailBody + "\n", true);

            client.send(message);

            return true;
        } catch (Exception ex) {
            LogUtil.writeLog(ex);
            return false;
        }
    }
}
