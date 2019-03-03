package ru.geekbrains.gkportal.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.geekbrains.gkportal.config.MailConfig;
import ru.geekbrains.gkportal.entity.Communication;
import ru.geekbrains.gkportal.entity.Contact;
import ru.geekbrains.gkportal.entity.PropertyType;
import ru.geekbrains.gkportal.entity.questionnaire.QuestionnaireContactConfirm;
import ru.geekbrains.gkportal.util.MailMessageBuilder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MailService {

    private static final Logger logger = Logger.getLogger(MailService.class);

    private JavaMailSender sender;
    private MailMessageBuilder builder;
    private PropertyService propertyService;
    private ContactService contactService;

    @Autowired
    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    public void setSender(JavaMailSender sender) {
        this.sender = sender;
    }

    @Autowired
    public void setBuilder(MailMessageBuilder builder) {
        this.builder = builder;
    }

    @Autowired
    public void setPropertyService(PropertyService propertyService) {
        this.propertyService = propertyService;
    }


    public List<Boolean> sendMail(List<String> emails, String subject, String text, boolean isHtml) {
        List<Boolean> res = new ArrayList<>();
        for (String mail : emails) {
            res.add(sendMail(mail, subject, text, isHtml));
        }
        return res;
    }

    /**
     * Отправка писем через сайт по списку контактов
     *
     * @param toEmails
     * @param fromEmail
     * @param subject
     * @param text
     * @param isHtml
     * @return
     */
    public boolean sendMail(List<Contact> toEmails, Contact fromEmail, String subject, String text, boolean isHtml) {

        for (Contact contact : toEmails) {
            //res.add(sendMail(mail, subject, text, isHtml));
        }
        return false;
    }



    public boolean sendMail(String email, String subject, String text) {
        return sendMail(email, subject, text, true);
    }

    public boolean sendMailToAdmin(String subject, String text) {
        return sendMail(MailConfig.ADMIN_MAIL, subject, text, true);
    }

    public boolean sendMail(String email, String subject, String text, boolean isHtml) {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(email);
            helper.setText(text, isHtml);
            helper.setSubject(subject);
            helper.setFrom(propertyService.getPropertyValue("user_name", PropertyType.MAIL));
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }

        try {
            executorService.submit(() -> sender.send(message));
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean sendRegistrationMail(String email) {
        return sendMail(email, "Регистрация на сайте  ЖК Город", builder.buildRegistrationEmail());
    }

    public boolean sendRegistrationMail(Contact contact) {
        String url = getCurentURL();
        Communication email = contactService.getEmailCommunication(contact);

        return sendMail(email.getIdentify(),
                "Регистрация на сайте ЖК Город",
                builder.buildRegistrationEmail(contact.getLastName() + " " + contact.getFirstName() + " " + contact.getMiddleName() + " ",
                        url + "/confirmMail/" + email.getIdentify() + "/" + email.getConfirmCode()));
    }

    public boolean sendRegistrationMail(Contact contact, QuestionnaireContactConfirm confirm) {
        String email = contactService.getEmail(contact);
        if (email == null) return false;
        String url = getCurentURL();
        return sendMail(email,
                "Регистрация в опросе на сайте ЖК Город",
                builder.buildRegistrationQuestionEmail(contact.getLastName() + " " + contact.getFirstName() + " " + contact.getMiddleName() + " ",
                        url + "/confirmQuestion/" + contact.getUuid() + "/" + confirm.getConfirmCode()));
    }

    public String getCurentURL() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = sra.getRequest();
        int port = req.getServerPort();
        if (port == 80) return "http://" + req.getServerName();
        else if (port == 443) return "https://" + req.getServerName();
        else return "http://" + req.getServerName() + ":" + req.getServerPort();
    }
}