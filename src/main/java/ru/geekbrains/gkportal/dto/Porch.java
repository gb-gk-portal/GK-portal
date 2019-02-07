package ru.geekbrains.gkportal.dto;

import lombok.Data;
import ru.geekbrains.gkportal.entities.BuildPorchConfig;

import java.util.ArrayList;
import java.util.List;

@Data
public class Porch {
    private BuildPorchConfig description;
    private List<Floor> floors = new ArrayList<>();

}
