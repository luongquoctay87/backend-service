package vn.tayjava.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.tayjava.service.EmailService;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "EMAIL-CONTROLLER")
@Tag(name = "Email Controller")
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/send-email")
    public void sendEmail(@RequestParam String to, String subject, String body) {
        log.info("Sending email to {}", to);
        emailService.send(to, subject, body);
        log.info("Email sent");
    }

    @GetMapping("/send-verification-email")
    public void sendVerificationEmail(@RequestParam String to, @RequestParam String name) {
        try {
            emailService.sendVerificationEmail(to, name);
            log.info("Verification email sent successfully!");
        } catch (Exception e) {
            log.info("Failed to send verification email.");
        }
    }
}
