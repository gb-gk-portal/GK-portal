package ru.geekbrains.gkportal.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity(name = "build_porch_config")
@IdClass(BuildPorchConfig.HousingPorchID.class)
public class BuildPorchConfig {

    @Id
    @Column(name = "housing")
    private Integer housingID;

    @Id
    @Column(name = "porch")
    private Integer porchID;

    @Column(name = "floors_count")
    @NotNull(message = "Couldn't be empty!")
    private Integer floorsCount;

    @Column(name = "flat_quantity_floor")
    @NotNull(message = "Couldn't be empty!")
    private Integer flatCountOnFloor;

    @Column(name = "flat_from_floor")
    @NotNull(message = "Couldn't be empty!")
    private Integer flatStartFromFloor;

    @Column(name = "flat_quantity_start_floor")
    @NotNull(message = "Couldn't be empty!")
    private Integer flatCountOnStartFloor;

    @Column(name = "record_built")
    @NotNull(message = "Couldn't be empty!")
    private boolean recordBuilt;

    @Column(name = "porch_num_from_right")
    @NotNull(message = "Couldn't be empty!")
    private boolean porchNumFromRight;

    @Column(name = "housing_num_from_right")
    @NotNull(message = "Couldn't be empty!")
    private boolean housingNumFromRight;

    @Column(name = "ident_flat_count")
    @NotNull(message = "Couldn't be empty!")
    private Integer flatOnPorchIdentCount;

    @Column(name = "build_housing")
    @NotNull(message = "Couldn't be empty!")
    private Integer buildHousing;

    @Column(name = "build_porch")
    @NotNull(message = "Couldn't be empty!")
    private Integer buildPorch;

    @Column(name = "all_flat_count")
    private Integer flatOnPorchCount;

    public BuildPorchConfig() {
    }

    @Embeddable
    public static class HousingPorchID implements Serializable {
        protected Integer housingID;
        protected Integer porchID;

        public HousingPorchID() {
        }

        public HousingPorchID(Integer housingID, Integer porchID) {
            this.housingID = housingID;
            this.porchID = porchID;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HousingPorchID otherId = (HousingPorchID) o;
            if (!housingID.equals(otherId.housingID)) return false;
            return porchID.equals(otherId.porchID);
        }

        @Override
        public int hashCode() {
            int result = housingID.hashCode();
            result = 31 * result + porchID.hashCode();
            return result;
        }
    }

}
