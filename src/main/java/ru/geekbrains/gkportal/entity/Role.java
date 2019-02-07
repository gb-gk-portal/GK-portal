package ru.geekbrains.gkportal.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Data
@Entity(name = "role")
@EqualsAndHashCode(callSuper = true)
public class Role extends AbstractEntity {

    @Column(name = "description")
    @NotNull(message = "Couldn't be empty!")
    private String description;

    public Role() {
    }

}
