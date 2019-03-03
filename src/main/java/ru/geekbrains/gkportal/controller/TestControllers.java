package ru.geekbrains.gkportal.controller;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.gkportal.security.IsAdmin;
import ru.geekbrains.gkportal.service.MailService;

import java.util.Arrays;
import java.util.List;

///


@RestController
@RequestMapping("/test")
public class TestControllers {

    private static final Logger logger = Logger.getLogger(TestControllers.class);

    private MailService mailService;

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @IsAdmin
    @GetMapping("/email")
    public int sendMail() {
        mailService.sendRegistrationMail("admin@chertenok.ru");
        return HttpStatus.OK.value();
    }

    @IsAdmin
    @GetMapping("/emailGroup")
    public List<Boolean> sendMailGroup() {

        String subject = "ЖК Город. Обращение инициативной группы подъезда";
        String text = "Здравствуйте! \n\n Вы получили это письмо, т.к. зарегистрировались в шахматке. \n\n " +
                "Письмо рассылается по просьбе ИГ подъезда, просьба не отвечать на это письмо, оно отправлено роботом.\n\n" +

                "\n\n" +

                "Уважаемые собственники квартир 10 секции корпуса 1, \n\n" +
                "Заранее спасибо за понимание!\n\n" +
                "С Уважением,\n" +
                "Инициативная группа Вашего под'езда\n";

        List<String> mails = Arrays.asList("");


        return mailService.sendMail(mails, subject, text, false);
    }
}
