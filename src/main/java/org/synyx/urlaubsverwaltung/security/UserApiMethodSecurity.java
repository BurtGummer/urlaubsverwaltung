package org.synyx.urlaubsverwaltung.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.synyx.urlaubsverwaltung.department.DepartmentService;
import org.synyx.urlaubsverwaltung.person.Person;
import org.synyx.urlaubsverwaltung.person.PersonService;

import java.util.Optional;

import static org.synyx.urlaubsverwaltung.person.Role.DEPARTMENT_HEAD;
import static org.synyx.urlaubsverwaltung.person.Role.SECOND_STAGE_AUTHORITY;
import static org.synyx.urlaubsverwaltung.security.AuthenticationHelper.userName;

@Component
public class UserApiMethodSecurity {

    private final PersonService personService;
    private final DepartmentService departmentService;

    @Autowired
    public UserApiMethodSecurity(PersonService personService, DepartmentService departmentService) {
        this.personService = personService;
        this.departmentService = departmentService;
    }

    public boolean isInDepartmentOfSecondStageAuthority(Authentication authentication, Integer userId) {
        final Optional<Person> loggedInUser = personService.getPersonByUsername(userName(authentication));

        if (loggedInUser.isEmpty() || !loggedInUser.get().hasRole(SECOND_STAGE_AUTHORITY)) {
            return false;
        }

        final Optional<Person> person = personService.getPersonByID(userId);
        if (person.isEmpty()) {
            return false;
        }

        return departmentService.isSecondStageAuthorityAllowedToManagePerson(loggedInUser.get(), person.get());
    }

    public boolean isInDepartmentOfDepartmentHead(Authentication authentication, Integer userId) {
        final Optional<Person> loggedInUser = personService.getPersonByUsername(userName(authentication));

        if (loggedInUser.isEmpty() || !loggedInUser.get().hasRole(DEPARTMENT_HEAD)) {
            return false;
        }

        final Optional<Person> person = personService.getPersonByID(userId);
        if (person.isEmpty()) {
            return false;
        }

        return departmentService.isDepartmentHeadAllowedToManagePerson(loggedInUser.get(), person.get());
    }

    public boolean isSamePersonId(Authentication authentication, Integer userId) {

        final Optional<Person> person = personService.getPersonByID(userId);
        if (person.isEmpty()) {
            return false;
        }

        final String usernameToCheck = person.get().getUsername();
        return (usernameToCheck != null) && usernameToCheck.equals(userName(authentication));
    }
}
