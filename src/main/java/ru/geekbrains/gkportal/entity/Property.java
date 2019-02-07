package ru.geekbrains.gkportal.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Entity(name = "properties")
@EqualsAndHashCode(callSuper = true)
public class Property extends AbstractEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    public Property() {
    }
}
