package org.synyx.urlaubsverwaltung.application.vacationtype;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface VacationTypeRepository extends JpaRepository<VacationTypeEntity, Integer> {

    List<VacationTypeEntity> findByActiveIsTrue();
}
