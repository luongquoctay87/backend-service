package vn.tayjava.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "EMAIL-SERVICE")
public class EmailService {

    private final SendGrid sendGrid;

    @Value("${spring.sendGrid.fromEmail:dummy-from-email}")
    private String from;

    @Value("${spring.sendGrid.templateId:dummy-template-id}")
    private String templateId;

    @Value("${spring.sendGrid.verificationLink:dummy-verification-link}")
    private String verificationLink;

    /**
     * Send simple email with sendGrid
     *
     * @param to
     * @param subject
     * @param text
     */
    public void send(String to, String subject, String text) {
        Email fromEmail = new Email(from);
        Email toEmail = new Email(to);

        Content content = new Content("text/plain", text);
        Mail mail = new Mail(fromEmail, subject, toEmail, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);

            if (response.getStatusCode() == 202) {
                log.info("Email sent successfully");
            } else {
                log.error("Email sent failed");
            }
        } catch (IOException e) {
            log.error("Email sent failed, errorMessage={}", e.getMessage());
        }
    }

    /**
     * Send email verification
     *
     * @param to
     * @param name
     */
    public void sendVerificationEmail(String to, String name) throws IOException {
        log.info("Sending verification email for name={}", name);

        Email fromEmail = new Email(from, "Tây Java");
        Email toEmail = new Email(to);
        String subject = "Xác thực tài khoản";

        // Generate secret code and save to db
        String secretCode = UUID.randomUUID().toString();
        log.info("secretCode = {}", secretCode);

        // TOD0 save secretCode to db

        // Tạo dynamic template data
        Map<String, String> dynamicTemplateData = new HashMap<>();
        dynamicTemplateData.put("name", name);
        dynamicTemplateData.put("verification_link", verificationLink + "?secretCode=" + secretCode);

        Mail mail = new Mail();
        mail.setFrom(fromEmail);
        mail.setSubject(subject);
        Personalization personalization = new Personalization();
        personalization.addTo(toEmail);

        // Add dynamic template data
        dynamicTemplateData.forEach(personalization::addDynamicTemplateData);

        mail.addPersonalization(personalization);
        mail.setTemplateId(templateId); // Template ID từ SendGrid

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sendGrid.api(request);
        if (response.getStatusCode() == 202) {
            log.info("Verification sent successfully");
        } else {
            log.error("Verification sent failed");
        }
    }
}
