package ru.geekbrains.gkportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.geekbrains.gkportal.dto.House;
import ru.geekbrains.gkportal.service.HouseService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private HouseService houseService;

    @Autowired
    public void setHouseService(HouseService houseService) {
        this.houseService = houseService;
    }

    @GetMapping("/showHouse/{number}")
    public String showHouse(@PathVariable(name = "number") int number, Model model){
        House house = houseService.build(number);
        model.addAttribute("house", house);
        return "edit-house-form";
    }
}
