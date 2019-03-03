package ru.geekbrains.gkportal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.geekbrains.gkportal.service.MailService;
import ru.geekbrains.gkportal.controller.TestControllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-secret.properties")
public class MailTest {

    private MailService mailService;

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestControllers adminController;

    @Test
    public void testPageTest() throws Exception {
        mockMvc.perform(get("/test/mymail"))
               .andDo(print());
              // .andExpect(xpath("/html/body").string("200"));

        // .andExpect(authenticated());
                //.andExpect(xpath("/html/body/div[2]/h1").string("admin"));
    }

    @Test
    public void sendEmail(){
        mailService.sendMail("ShangareevRR@mail.ru", "тестовое сообщение", "мой тест", true);
        Assert.assertTrue(true);
    }


}
