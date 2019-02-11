package ru.geekbrains.gkportal.dto;


import ru.geekbrains.gkportal.entity.ContactType;

public interface SystemAccountDTO {

    String getFirstName();

    String getLastName();

    String getMiddleName();

    String getEmail();

    String getPhoneNumber();

    ContactType getContactType();
}
