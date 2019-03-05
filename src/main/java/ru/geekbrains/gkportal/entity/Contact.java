package ru.geekbrains.gkportal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.geekbrains.gkportal.entity.questionnaire.QuestionnaireContactConfirm;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@Entity(name = "contact")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "questionnaireContactConfirm", callSuper = true)
@ToString(exclude = "questionnaireContactConfirm", callSuper = true)
public class Contact extends AbstractEntity {

//    @Valid
    @NotNull (message = "Тип контакта не может быть пустым")
    @ManyToOne
    @JoinColumn(name = "contact_type_id")
    private ContactType contactType;

    @Column(name = "first_name")
    @NotBlank(message = "Необходимо ввести имя.")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    @NotBlank(message = "Необходимо ввести фамилию.")
    private String lastName;

//    @Valid
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "contact_flat",
            joinColumns = @JoinColumn(name = "contact_id"),
            inverseJoinColumns = @JoinColumn(name = "flat_id"))
    private Collection<Flat> flats;

//    @Valid
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "contact")
    private Collection<Communication> communications;

//    @Valid
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "contact")
    private Collection<Ownership> ownerships;

//    @Valid
//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "contact_id")
//    private Collection<Ownership> realEstates;

//    @Valid
//todo  1 к многим !!!!
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "contact")
    @JoinColumn(name = "contact_id")
    @JsonIgnore
    private QuestionnaireContactConfirm questionnaireContactConfirm;
}
