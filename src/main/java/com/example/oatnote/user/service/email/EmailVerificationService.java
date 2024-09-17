package com.example.oatnote.user.service.email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.oatnote.user.service.email.exception.EmailDispatchException;
import com.example.oatnote.user.service.email.exception.EmailVerificationException;
import com.example.oatnote.user.service.email.model.EmailVerification;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final JavaMailSender mailSender;

    public void sendVerificationCode(String email, String code) {
        try {
            sendEmail(email, code);
            emailVerificationRepository.findByEmail(email).ifPresent(emailVerificationRepository::delete);
            emailVerificationRepository.save(new EmailVerification(email, code));
        } catch (MessagingException e) {
            throw new EmailDispatchException("이메일 전송에 실패했습니다.");
        }
    }

    public void verifyEmail(String email, String code) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(email)
            .orElseThrow(() -> new EmailVerificationException("인증 코드가 만료되었거나 존재하지 않습니다."));
        if (!code.equals(emailVerification.getCode())) {
            throw new EmailVerificationException("인증 코드가 일치하지 않습니다.");
        }
        emailVerificationRepository.delete(emailVerification);
    }

    void sendEmail(String to, String code) throws MessagingException {
        String htmlMessage = "<div style='font-family: Arial, sans-serif; line-height: 2; color: #333;'>"
            + "<p> <strong>OatNote</strong> 에서 인증 코드를 전송드립니다.</p>"
            + "<div style='margin-top: 20px;'>"
            + "    <div style='background: #faf4e6; border-radius: 25px; padding: 15px; font-size: 24px;"
            + "    font-weight: bold; color: #7f6d5f; display: inline-block; letter-spacing: 3px;"
            + "    max-width: 200px; word-wrap: break-word; text-align: center;'>"
            + code
            + "    </div>"
            + "</div>"
            + "<p style='margin-top: 20px;'>개인정보 보호를 위해 인증 코드는 <strong>10분</strong> 동안 유효합니다.</p>"
            + "</div>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        helper.setTo(to);
        helper.setSubject("Oatnote : 이메일 인증 코드");
        helper.setText(htmlMessage, true);
        mailSender.send(message);
    }
}
