package ru.geekbrains.gkportal.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.gkportal.service.MailService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestControllers {

    private MailService mailService;

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/email")
    public int sendMail() {
        mailService.sendRegistrationMail("admin@chertenok.ru");
        return HttpStatus.OK.value();
    }

    @GetMapping("/emailGroup")
    public int sendMailGroup() {

        String subject = "Собрание собственников 2 корп 5 секция";
        String text = "Здравствуйте! \n\n Планируем собрание собственников 26.01.2019 в 14:00 в холле. \n\n На собрании:\n" +
                " сбор средств на обшивку МОП,\n подписываем бумагу по поводу домофона и пломбировки счётчиков.\n\n" +
                " Пригласили Ларису Юрьевну для обсуждения вопросов к УК.\n\n" +
                "С Уважением, инициативная группа подъезда";
        List<String> mails = Arrays.asList(""
        );


        mailService.sendMail(mails, subject, text, false);


        return HttpStatus.OK.value();
    }


}
