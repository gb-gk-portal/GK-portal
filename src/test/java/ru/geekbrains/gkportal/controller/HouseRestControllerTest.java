package ru.geekbrains.gkportal.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.geekbrains.gkportal.dto.FlatDTO;
import ru.geekbrains.gkportal.entity.Flat;
import ru.geekbrains.gkportal.repository.FlatRepository;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class HouseRestControllerTest {

    private HouseRestController houseRestController;
    private FlatRepository flatRepository;
    
    @Autowired
    public void setFlatRepository(FlatRepository flatRepository) {
        this.flatRepository = flatRepository;
    }

    @Autowired
    public void setHouseRestController(HouseRestController houseRestController) {
        this.houseRestController = houseRestController;
    }

    @Test
    public void changeFlatTest() {
        FlatDTO flatDTO = new FlatDTO();
        flatDTO.setId("mockID");
        MockHttpServletResponse response = new MockHttpServletResponse();
        int tmp = houseRestController.changeFlat(flatDTO, response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), tmp);
        Page<Flat> flatPage = flatRepository.findAll(PageRequest.of(0, 10));
        long count = flatPage.getTotalElements();
        /*Manipulate with real data. But just copy-paste. Better to imply copy() method flat <-> flatDTO somewhere*/
        if (count != 0) {
            Flat flat = flatPage.iterator().next();
            flatDTO.setId(flat.getUuid());
            flatDTO.setPorch(flat.getPorch());
            flatDTO.setFloor(flat.getFloor());
            flatDTO.setFlatNumber(flat.getFlatNumber());
            flatDTO.setRiser(flat.getRiser());
            flatDTO.setFlatNumberBuild(flat.getFlatNumberBuild());
            response.reset();
            tmp = houseRestController.changeFlat(flatDTO, response);
            assertEquals(HttpStatus.OK.value(), tmp);
        }
    }
}
