package ru.geekbrains.gkportal.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.UUID;

@Data
@MappedSuperclass
public class AbstractEntity implements Serializable {

    @Id
    @Column(name = "id")
    private String uuid = UUID.randomUUID().toString();

}
