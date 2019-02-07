package ru.geekbrains.gkportal.dto;

import lombok.Data;
import ru.geekbrains.gkportal.entity.Flat;

import java.util.ArrayList;
import java.util.List;

@Data
public class Floor {
    private int number;
    List<Flat> flats = new ArrayList<>();
}
