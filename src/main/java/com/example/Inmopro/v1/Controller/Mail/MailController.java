package com.example.Inmopro.v1.Controller.Mail;

import com.example.Inmopro.v1.Dto.Mail.MailRequest;
import com.example.Inmopro.v1.Service.Mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<MailResponse> sendMail(@RequestBody MailRequest emailRequest) {
        mailService.sendSimpleEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getText());
        return ResponseEntity.ok(MailResponse.builder().message("Mail sent successfully").build());
    }
}
