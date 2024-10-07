package com.example.oatnote.user.service.email;

import java.time.LocalDateTime;
import java.util.Objects;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.oatnote.user.service.email.model.EmailVerification;
import com.example.oatnote.web.exception.client.OatDataNotFoundException;
import com.example.oatnote.web.exception.server.OatExternalServiceException;
import com.example.oatnote.web.exception.client.OatIllegalArgumentException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final JavaMailSender mailSender;

    private static final int CODE_LENGTH = 6;

    public void sendCode(String email, int expiryMinutes) {
        log.info("이메일 인증 코드 전송 - 이메일: {}", email);
        try {
            String code = RandomStringUtils.randomNumeric(CODE_LENGTH);
            sendEmail(email, code);
            insertEmailVerification(email, code, expiryMinutes);
        } catch (MessagingException e) {
            throw OatExternalServiceException.withDetail("이메일 전송에 실패했습니다.", email);
        }
    }

    public void insertEmailVerification(String email, String code, int expiryMinutes) {
        log.info("이메일 인증 코드 저장 - 이메일: {}", email);
        if (emailVerificationRepository.existsByEmail(email)) {
            emailVerificationRepository.deleteByEmail(email);
        }
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(expiryMinutes);
        emailVerificationRepository.insert(new EmailVerification(email, code, expiryTime));
    }

    public void verifyCode(String email, String code) {
        log.info("이메일 인증 코드 확인 - 이메일: {}", email);
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(email)
            .orElseThrow(() -> OatDataNotFoundException.withDetail("인증 코드가 발급되지 않았습니다.", email));

        if (!Objects.equals(code, emailVerification.getCode())) {
            throw OatIllegalArgumentException.withDetail("인증 코드가 일치하지 않습니다.", email);
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
        helper.setSubject(String.format("Oatnote : 이메일 인증 코드 - %s", code));
        helper.setText(htmlMessage, true);
        mailSender.send(message);
    }
}
