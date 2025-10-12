package PRM392com.example.car_service_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetToken(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Yêu cầu đặt lại mật khẩu");
        message.setText("Chào bạn,\n\nĐể đặt lại mật khẩu, hãy sử dụng mã token sau trong ứng dụng của bạn:\n\n" + token + "\n\nNếu bạn không yêu cầu việc này, vui lòng bỏ qua email này.");
        mailSender.send(message);
    }
}