package org.synyx.urlaubsverwaltung.mail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.synyx.urlaubsverwaltung.department.DepartmentService;
import org.synyx.urlaubsverwaltung.person.Person;
import org.synyx.urlaubsverwaltung.person.PersonService;
import org.synyx.urlaubsverwaltung.person.Role;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.synyx.urlaubsverwaltung.person.MailNotification.NOTIFICATION_EMAIL_APPLICATION_MANAGEMENT_APPLIED;
import static org.synyx.urlaubsverwaltung.person.MailNotification.NOTIFICATION_EMAIL_OVERTIME_MANAGEMENT_APPLIED;
import static org.synyx.urlaubsverwaltung.person.MailNotification.NOTIFICATION_EMAIL_PERSON_NEW_MANAGEMENT_ALL;
import static org.synyx.urlaubsverwaltung.person.Role.BOSS;
import static org.synyx.urlaubsverwaltung.person.Role.DEPARTMENT_HEAD;
import static org.synyx.urlaubsverwaltung.person.Role.SECOND_STAGE_AUTHORITY;
import static org.synyx.urlaubsverwaltung.person.Role.USER;

@ExtendWith(MockitoExtension.class)
class MailRecipientServiceImplTest {

    private MailRecipientServiceImpl sut;

    @Mock
    private PersonService personService;
    @Mock
    private DepartmentService departmentService;

    @BeforeEach
    void setUp() {
        sut = new MailRecipientServiceImpl(personService, departmentService);
    }


    @Test
    void ensureToReturnResponsibleManagerWithDepartments() {

        // given person of interest
        final Person person = new Person("person", "person", "person", "person@example.org");

        // given department head
        final Person departmentHead = new Person("departmentHead", "departmentHead", "departmentHead", "departmentHead@example.org");
        departmentHead.setPermissions(List.of(USER, DEPARTMENT_HEAD));
        when(personService.getActivePersonsByRole(DEPARTMENT_HEAD)).thenReturn(List.of(departmentHead));
        when(departmentService.isDepartmentHeadAllowedToManagePerson(departmentHead, person)).thenReturn(true);

        // given second stage
        final Person secondStage = new Person("secondStage", "secondStage", "secondStage", "secondStage@example.org");
        secondStage.setPermissions(List.of(USER, SECOND_STAGE_AUTHORITY));
        when(personService.getActivePersonsByRole(SECOND_STAGE_AUTHORITY)).thenReturn(List.of(secondStage));
        when(departmentService.isSecondStageAuthorityAllowedToManagePerson(secondStage, person)).thenReturn(true);

        final Person boss = new Person("boss", "boss", "senior", "boss@example.org");
        boss.setPermissions(List.of(USER, BOSS));
        when(personService.getActivePersonsByRole(BOSS)).thenReturn(List.of(boss));

        when(departmentService.getNumberOfDepartments()).thenReturn(1L);

        final List<Person> responsibleManagersOf = sut.getResponsibleManagersOf(person);
        assertThat(responsibleManagersOf)
            .containsExactly(departmentHead, secondStage, boss);
    }

    @Test
    void ensureToReturnOnlyBossAsResponsibleManagerWithoutDepartments() {

        // given person of interest
        final Person person = new Person("person", "person", "person", "person@example.org");

        final Person boss = new Person("boss", "boss", "senior", "boss@example.org");
        boss.setPermissions(List.of(USER, BOSS));
        when(personService.getActivePersonsByRole(BOSS)).thenReturn(List.of(boss));

        when(departmentService.getNumberOfDepartments()).thenReturn(0L);

        final List<Person> responsibleManagersOf = sut.getResponsibleManagersOf(person);
        assertThat(responsibleManagersOf)
            .containsExactly(boss);

        verify(personService, never()).getActivePersonsByRole(SECOND_STAGE_AUTHORITY);
        verify(personService, never()).getActivePersonsByRole(DEPARTMENT_HEAD);
    }

    @Test
    void getRecipientsOfInterestWithDepartmentsFilteredByEnabledMailNotification() {

        when(departmentService.getNumberOfDepartments()).thenReturn(1L);

        // given user application
        final Person normalUser = new Person("normalUser", "normalUser", "normalUser", "normalUser@example.org");
        normalUser.setId(1);
        normalUser.setPermissions(List.of(USER));

        // given boss all
        final Person bossAll = new Person("boss", "boss", "boss", "boss@example.org");
        bossAll.setId(2);
        bossAll.setPermissions(List.of(USER, BOSS));
        bossAll.setNotifications(List.of(NOTIFICATION_EMAIL_APPLICATION_MANAGEMENT_APPLIED));
        when(personService.getActivePersonsByRole(BOSS)).thenReturn(List.of(bossAll));

        // given department head
        final Person departmentHead = new Person("departmentHead", "departmentHead", "departmentHead", "departmentHead@example.org");
        departmentHead.setId(5);
        departmentHead.setPermissions(List.of(USER, DEPARTMENT_HEAD));
        departmentHead.setNotifications(List.of(NOTIFICATION_EMAIL_APPLICATION_MANAGEMENT_APPLIED));
        when(personService.getActivePersonsByRole(DEPARTMENT_HEAD)).thenReturn(List.of(departmentHead));
        when(departmentService.isDepartmentHeadAllowedToManagePerson(departmentHead, normalUser)).thenReturn(true);

        // given second stage
        final Person secondStage = new Person("secondStage", "secondStage", "secondStage", "secondStage@example.org");
        secondStage.setId(6);
        secondStage.setPermissions(List.of(USER, SECOND_STAGE_AUTHORITY));
        secondStage.setNotifications(List.of(NOTIFICATION_EMAIL_APPLICATION_MANAGEMENT_APPLIED));
        when(departmentService.isSecondStageAuthorityAllowedToManagePerson(secondStage, normalUser)).thenReturn(true);

        final Person secondStageWithoutMailtNotification = new Person("secondStage", "secondStage", "secondStage", "secondStage@example.org");
        secondStageWithoutMailtNotification.setId(7);
        secondStageWithoutMailtNotification.setPermissions(List.of(USER, SECOND_STAGE_AUTHORITY));
        secondStageWithoutMailtNotification.setNotifications(List.of());
        when(departmentService.isSecondStageAuthorityAllowedToManagePerson(secondStageWithoutMailtNotification, normalUser)).thenReturn(true);

        when(personService.getActivePersonsByRole(SECOND_STAGE_AUTHORITY)).thenReturn(List.of(secondStage, secondStageWithoutMailtNotification));

        final List<Person> recipientsForAllowAndRemind = sut.getRecipientsOfInterest(normalUser, NOTIFICATION_EMAIL_APPLICATION_MANAGEMENT_APPLIED);
        assertThat(recipientsForAllowAndRemind)
            .doesNotContain(secondStageWithoutMailtNotification)
            .containsOnly(bossAll, departmentHead, secondStage);
    }

    @Test
    void getRecipientsOfInterestWithoutDepartments() {

        when(departmentService.getNumberOfDepartments()).thenReturn(0L);

        // given user application
        final Person normalUser = new Person("normalUser", "normalUser", "normalUser", "normalUser@example.org");
        normalUser.setId(1);

        // given boss all
        final Person bossAll = new Person("boss", "boss", "boss", "boss@example.org");
        bossAll.setId(2);
        bossAll.setPermissions(List.of(USER, BOSS));
        bossAll.setNotifications(List.of(NOTIFICATION_EMAIL_APPLICATION_MANAGEMENT_APPLIED));
        when(personService.getActivePersonsByRole(BOSS)).thenReturn(List.of(bossAll));

        final List<Person> recipientsForAllowAndRemind = sut.getRecipientsOfInterest(normalUser, NOTIFICATION_EMAIL_APPLICATION_MANAGEMENT_APPLIED);
        assertThat(recipientsForAllowAndRemind).containsOnly(bossAll);
    }

    @Test
    void getRecipientsOfInterestWithDepartmentsAndDistinctRecipients() {

        when(departmentService.getNumberOfDepartments()).thenReturn(1L);

        // given user application
        final Person normalUser = new Person("normalUser", "normalUser", "normalUser", "normalUser@example.org");
        normalUser.setId(1);

        // given boss all
        final Person bossAll = new Person("boss", "boss", "boss", "boss@example.org");
        bossAll.setId(2);
        bossAll.setNotifications(List.of(NOTIFICATION_EMAIL_APPLICATION_MANAGEMENT_APPLIED));
        when(personService.getActivePersonsByRole(BOSS)).thenReturn(List.of(bossAll));

        final List<Person> recipientsForAllowAndRemind = sut.getRecipientsOfInterest(normalUser, NOTIFICATION_EMAIL_APPLICATION_MANAGEMENT_APPLIED);
        assertThat(recipientsForAllowAndRemind).containsOnly(bossAll);
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"BOSS", "OFFICE", "DEPARTMENT_HEAD", "SECOND_STAGE_AUTHORITY"})
    void ensureToCallDatabaseForPersonsWithRoleIfNotificationIsValidForGivenRole(final Role role) {

        when(departmentService.getNumberOfDepartments()).thenReturn(1L);

        // given user application
        final Person normalUser = new Person("normalUser", "normalUser", "normalUser", "normalUser@example.org");
        normalUser.setId(1);

        sut.getRecipientsOfInterest(normalUser, NOTIFICATION_EMAIL_OVERTIME_MANAGEMENT_APPLIED);
        verify(personService).getActivePersonsByRole(role);
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"DEPARTMENT_HEAD", "SECOND_STAGE_AUTHORITY"})
    void ensureToNotCallDatabaseForPersonsWithoutRoleIfNotificationIsNotValidForDepartmentRoles(final Role role) {

        when(departmentService.getNumberOfDepartments()).thenReturn(1L);

        // given user application
        final Person normalUser = new Person("normalUser", "normalUser", "normalUser", "normalUser@example.org");
        normalUser.setId(1);

        sut.getRecipientsOfInterest(normalUser, NOTIFICATION_EMAIL_PERSON_NEW_MANAGEMENT_ALL);
        verify(personService, never()).getActivePersonsByRole(role);
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"OFFICE"})
    void ensureToNotCallDatabaseForPersonsWithoutRoleIfNotificationIsNotValidForRoleOrganisationRoles(final Role role) {

        when(departmentService.getNumberOfDepartments()).thenReturn(1L);

        // given user application
        final Person normalUser = new Person("normalUser", "normalUser", "normalUser", "normalUser@example.org");
        normalUser.setId(1);

        sut.getRecipientsOfInterest(normalUser, NOTIFICATION_EMAIL_APPLICATION_MANAGEMENT_APPLIED);
        verify(personService, never()).getActivePersonsByRole(role);
    }
}
