package do_an.traodoido.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final OtpCacheService otpCache;

    public void sendOtp(String email) {
        String otp = OtpUtil.generate();
        otpCache.save(email, otp);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Ma OTP xac thuc");
        msg.setText(
                "Ma OTP cua ban la: " + otp +
                        "\nHieu luc trong 5 phut.\nVui long khong chia se."
        );

        mailSender.send(msg);
    }
}
