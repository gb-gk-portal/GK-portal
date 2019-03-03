package ru.geekbrains.gkportal.entity.questionnaire;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.geekbrains.gkportal.entity.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Yuriy Tilman
 */

@Data
@Entity(name = "questionnaire")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Questionnaire extends AbstractEntity {

    @Column(name = "name")
    @NotNull(message = "Couldn't be empty!")
    private String name;

    @Column(name = "start_date")
    @NotNull(message = "Couldn't be empty!")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime from;

    @Column(name = "end_date")
    @NotNull(message = "Couldn't be empty!")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime to;

    @Column(name = "description")
    @NotNull(message = "Couldn't be empty!")
    private String description;

    @Column(name = "open")
    @NotNull(message = "Couldn't be empty!")
    private boolean open;

    @Column(name = "active")
    @NotNull(message = "Couldn't be empty!")
    private boolean active;

    @Column(name = "in_build_num")
    @NotNull(message = "Couldn't be empty!")
    private boolean inBuildNum;

    @Column(name = "use_real_estate")
    @NotNull(message = "Couldn't be empty!")
    private boolean useRealEstate;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "questionnaire_id")
    private List<Question> questions;


}
