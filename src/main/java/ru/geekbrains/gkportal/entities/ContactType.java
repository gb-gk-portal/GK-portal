package ru.geekbrains.gkportal.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Data
@Table(name = "contact_type")
public class ContactType {

    @Id
    @Column(name = "id")
    private String uuid = UUID.randomUUID().toString();

    @Column(name = "description")
    @NotNull(message = "Couldn't be empty!")
    private String description;

    public ContactType() {
    }

}
