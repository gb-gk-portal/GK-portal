package ru.geekbrains.gkportal.controller;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.gkportal.dto.FlatDTO;
import ru.geekbrains.gkportal.service.HouseService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/rest")
public class HouseRestController {

    private static final Logger logger = Logger.getLogger(HouseRestController.class);

    private HouseService houseService;

    @Autowired
    public void setHouseService(HouseService houseService) {
        this.houseService = houseService;
    }

    @PutMapping(value = "/flat")
    public int changeFlat(@RequestBody FlatDTO flatDTO, HttpServletResponse response) {
        logger.debug("flatDTO: " + flatDTO);
        if (houseService.changeFlat(flatDTO) != null) {
            return HttpStatus.OK.value();
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return HttpStatus.BAD_REQUEST.value();
        }
    }

    @GetMapping(value = "/house/{houseNumber}/porches")
    @ResponseBody
    public List<Integer> getPorchNums(@PathVariable(name = "houseNumber") int housingId) {
        return houseService.getHousingPorchNumbers(housingId);
    }
}
