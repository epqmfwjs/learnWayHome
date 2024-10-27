package com.learnway.member.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    // 이메일 인증 코드를 임시로 저장할 맵
    private final Map<String, String> verificationCodes = new HashMap<>();

    @Value("${spring.mail.username}")
    private String fromEmail; // 발신자 이메일 주소

    // 이메일 전송 메서드
    public boolean sendVerificationEmail(String email) {
        String code = generateVerificationCode(); // 인증 코드 생성
        verificationCodes.put(email, code); // 인증 코드를 임시로 저장

        try {
            // 이메일 메시지 생성 (MimeMessage : 이메일의 내용을 담고 있는 객체)
            MimeMessage message = mailSender.createMimeMessage();
            // 멀티파트 모드로 MimeMessageHelper( 마임메세지 작성 편리하게 도와줌) 생성
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);            // 수신자
            helper.setFrom(fromEmail);      // 발신자
            helper.setSubject("LearnWay 이메일 인증"); // 이메일 제목

            // HTML 형식의 이메일 내용 작성
            String htmlMsg = "<div style='text-align: center;'>" +
                    "<img src='cid:logoImage' alt='Logo' style='width: 100px; margin-bottom: 20px;'/>" +
                    "<h2>목표에 도달하는 길,</h2>" +
                    "<h2>LEARN WAY에 오신 걸 환영합니다.</h2>" +
                    "<p>인증 코드: <strong>" + code + "</strong></p>" +
                    "<p>LEARN WAY에서 인증 코드를 입력해주세요.</p>" +
                    "</div>";

            helper.setText(htmlMsg, true); // HTML 내용으로 설정

            // 이미지 첨부 (메인 로고)
            helper.addInline("logoImage", new ClassPathResource("static/img/mainLogo.png"));
            mailSender.send(message); // 이메일 전송

            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 이메일 인증 코드 검증 메서드
    public boolean verifyEmail(String email, String code) {
        String storedCode = verificationCodes.get(email); // 저장된 인증 코드 가져오기
        return storedCode != null && storedCode.equals(code); // 입력된 코드와 저장된 코드 비교
    }

    // 인증 코드 생성 메서드
    private String generateVerificationCode() {
        Random random = new Random();
        String number = String.format("%06d", random.nextInt(1000000));
        System.out.println(number + " : 이메일 인증번호");
        return number; // 6자리 인증 코드 생성
    }
}
