package ru.geekbrains.gkportal.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class House {
    private int number;
    private List<Porch> porchList = new ArrayList<>();
}
