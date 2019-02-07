package ru.geekbrains.gkportal.entity;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Data
@Entity(name = "communication")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Communication extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "communication_type_id")
    @NotNull(message = "Communication type s.b. selected!")
    private CommunicationType communicationType;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    @NotNull(message = "Contact s.b. selected!")
    private Contact contact;

    @Column(name = "identify")
    @NotNull(message = "Couldn't be empty!")
    private String identify;

    @Column(name = "description")
    @NotNull(message = "Couldn't be empty!")
    private String description;

    @Column(name = "confirmed")
    @NotNull(message = "Couldn't be empty!")
    private boolean confirmed;

    @Column(name = "confirm_code_date")
    @UpdateTimestamp
//    @NotNull(message = "Couldn't be empty!")
    private LocalDateTime confirmCodeDate;

    @Column(name = "confirm_code")
    private String confirmCode;

}
