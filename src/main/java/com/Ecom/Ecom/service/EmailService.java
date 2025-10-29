package com.Ecom.Ecom.service;

import com.Ecom.Ecom.Entity.Email;
import com.Ecom.Ecom.Repo.EmailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private EmailRepo emailRepo;
    @Autowired
    private  MailSender mailSender;
    public ResponseEntity<?> SaveAndSendMail(String email) {

        String subject = "Welcome to O2 Shop Newsletter!";
        String body = "Hi there!\n\nThanks for subscribing to O2 Shop. You'll now receive our latest offers and updates!";
        SimpleMailMessage mail=new SimpleMailMessage();
        mail.setSubject(subject);
        mail.setText(body);
        mail.setTo(email);
        mail.setFrom("Sujitswain077@gmail.com");
        mailSender.send(mail);
        try {
            Email email1 = new Email();
            email1.setMail(email);
            emailRepo.save(email1);
        }
        catch (Exception e){
            System.out.println("Alredy");
        }


        return  ResponseEntity.ok("done");

    }
}
