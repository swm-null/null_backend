package com.example.oatnote.user.service.email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationCode(String email, String verificationCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        String htmlMsg = "<div style='font-family: Arial, sans-serif; line-height: 2; color: #333;'>"
            + "<p> <strong>OatNote</strong> 에서 인증 코드를 전송드립니다.</p>"
            + "<div style='margin-top: 20px;'>"
            + "    <div style='background: #faf4e6; border-radius: 25px; padding: 15px; font-size: 24px;"
            + "    font-weight: bold; color: #7f6d5f; display: inline-block; letter-spacing: 3px;"
            + "    max-width: 200px; word-wrap: break-word; text-align: center;'>"
            +      verificationCode
            + "    </div>"
            + "</div>"
            + "<p style='margin-top: 20px;'>개인정보 보호를 위해 인증 코드는 <strong>10분</strong> 동안 유효합니다.</p>"
            + "</div>";
        helper.setTo(email);
        helper.setSubject("Oatnote : 이메일 인증 코드");
        helper.setText(htmlMsg, true);
        mailSender.send(message);
    }
}
