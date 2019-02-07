package ru.geekbrains.gkportal.dto;

import lombok.Data;

@Data
public class SystemUser {

    private String lastName;

    private String firstName;

    private String middleName;

    private String phoneNumber;

    private String email;

    private String pass;

    private String passRepeat;

    private Integer flatId; // пусть будет пока так, потом подумаем..

    private Integer roomCount;

    private Boolean HaveParkingPlace;




}
