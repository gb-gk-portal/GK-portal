package ru.geekbrains.gkportal.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class FlatRegDTO {
    @NotNull(message = "Поле обязательно")
    @Range(min = 1, max = 99, message = "Значение 1-99")
    private Integer housingNumber;

    @NotNull(message = "Поле обязательно")
    @Range(min = 1, max = 200, message = "Значение 1-200")
    private Integer porchNumber;

    @NotNull(message = "Поле обязательно")
    @Range(min = 1, max = 99, message = "Значение 1-99")
    private Integer floorNumber;

    @NotNull(message = "Поле обязательно")
    @Range(min = 1, max = 99999, message = "Значение 1-99999")
    private Integer flatNumber;

    @NotNull(message = "Поле обязательно")
    @Range(min = 1, max = 20, message = "Значение 1-20 ")
    private Integer roomCount;
}
