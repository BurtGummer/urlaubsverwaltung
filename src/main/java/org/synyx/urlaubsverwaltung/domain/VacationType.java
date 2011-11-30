package org.synyx.urlaubsverwaltung.domain;

/**
 * @author  Johannes Reuter
 * @author  Aljona Murygina
 */
public enum VacationType {

    HOLIDAY("vac.holiday"),
    SPECIALLEAVE("vac.special"),
    UNPAIDLEAVE("vac.unpaid"),
    OVERTIME("vac.overtime");

    private String vacationTypeName;

    private VacationType(String vacationTypeName) {

        this.vacationTypeName = vacationTypeName;
    }

    public String getVacationTypeName() {

        return this.vacationTypeName;
    }
}
