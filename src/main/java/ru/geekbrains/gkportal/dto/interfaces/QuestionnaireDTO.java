package ru.geekbrains.gkportal.dto.interfaces;


import java.util.List;

/**
 * @author Yuriy Tilman
 */

public interface QuestionnaireDTO {

    String getUuid();

    String getName();

    String getDescription();

    List<QuestionDTO> getQuestions();

    boolean isUseRealEstate();

   //    @Column(name = "start_date")
//    @NotNull(message = "Couldn't be empty!")
//    private LocalDateTime from;
//
//    @Column(name = "end_date")
//    @NotNull(message = "Couldn't be empty!")
//    private LocalDateTime to;
//
//    @Column(name = "description")
//    @NotNull(message = "Couldn't be empty!")
//    private String description;
//
//    @Column(name = "open")
//    @NotNull(message = "Couldn't be empty!")
//    private boolean open;
//
//    @Column(name = "active")
//    @NotNull(message = "Couldn't be empty!")
//    private boolean active;
//
//    @Column(name = "in_build_num")
//    @NotNull(message = "Couldn't be empty!")
//    private boolean inBuildNum;
//
//    @Column(name = "use_real_estate")
//    @NotNull(message = "Couldn't be empty!")
//    private boolean useRealEstate;

}
