package ru.geekbrains.gkportal.service.mail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.geekbrains.gkportal.entity.PropertyType;
import ru.geekbrains.gkportal.service.PropertyService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class MailPropertiesTest {
    private PropertyService propertyService;
    private static final Map<String, String> myMap = new HashMap<String, String>() {{
        put("default_encoding", "UTF-8");
        put("host", "smtp.gmail.com");
        put("port", "587");
        put("user_name", "d107ru@gmail.com");
        put("password", "");
        put("smtp_auth", "true");
        put("smtp_starttls_enable", "true");
        put("transport_protocol", "smtp");
        put("debug", "false");
    }};

    @Autowired
    public void setPropertyService(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @Test
    public void propertiesTest() {
        for (Map.Entry<String, String> m : myMap.entrySet()) {
            String tmp = propertyService.getPropertyValue(m.getKey(), PropertyType.MAIL);
            assertEquals(tmp, m.getValue());
        }
    }
}
