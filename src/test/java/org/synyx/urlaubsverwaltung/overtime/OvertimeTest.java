package org.synyx.urlaubsverwaltung.overtime;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.util.ReflectionUtils;
import org.synyx.urlaubsverwaltung.person.Person;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.synyx.urlaubsverwaltung.demodatacreator.DemoDataCreator.createPerson;
import static org.synyx.urlaubsverwaltung.person.MailNotification.NOTIFICATION_USER;
import static org.synyx.urlaubsverwaltung.person.Role.USER;


class OvertimeTest {

    @Test
    void ensureThrowsOnNullPerson() {
        LocalDate now = LocalDate.now(UTC);
        assertThatIllegalArgumentException().isThrownBy(() -> new Overtime(null, now, now, BigDecimal.ONE));
    }

    @Test
    void ensureThrowsOnNullStartDate() {
        Person person = createPerson();
        LocalDate now = LocalDate.now(UTC);

        assertThatIllegalArgumentException().isThrownBy(() -> new Overtime(person, null, now, BigDecimal.ONE));
    }

    @Test
    void ensureThrowsOnNullEndDate() {
        Person person = createPerson();
        LocalDate now = LocalDate.now(UTC);

        assertThatIllegalArgumentException().isThrownBy(() -> new Overtime(person, now, null, BigDecimal.ONE));
    }

    @Test
    void ensureThrowsOnNullNumberOfHours() {
        Person person = createPerson();
        LocalDate now = LocalDate.now(UTC);

        assertThatIllegalArgumentException().isThrownBy(() -> new Overtime(person, now, now, null));
    }

    @Test
    void ensureReturnsCorrectStartDate() {
        Person person = createPerson();
        LocalDate now = LocalDate.now(UTC);

        Overtime overtime = new Overtime(person, now, now.plusDays(2), BigDecimal.ONE);

        Assert.assertNotNull("Should not be null", overtime.getStartDate());
        Assert.assertEquals("Wrong start date", now, overtime.getStartDate());
    }

    @Test
    void ensureReturnsCorrectEndDate() {
        Person person = createPerson();
        LocalDate now = LocalDate.now(UTC);
        Overtime overtime = new Overtime(person, now.minusDays(2), now, BigDecimal.ONE);

        Assert.assertNotNull("Should not be null", overtime.getEndDate());
        Assert.assertEquals("Wrong end date", now, overtime.getEndDate());
    }

    @Test
    void ensureSetLastModificationDateOnInitialization() {
        Person person = createPerson();
        LocalDate now = LocalDate.now(UTC);

        Overtime overtime = new Overtime(person, now.minusDays(2), now.plusDays(4), BigDecimal.ONE);

        Assert.assertNotNull("Should not be null", overtime.getLastModificationDate());
        Assert.assertEquals("Wrong last modification date", now, overtime.getLastModificationDate());
    }

    @Test
    void ensureThrowsIfGettingStartDateOnACorruptedOvertime() throws IllegalAccessException {
        Person person = createPerson();
        LocalDate now = LocalDate.now(UTC);

        Overtime overtime = new Overtime(person, now.minusDays(2), now, BigDecimal.ONE);

        Field startDateField = ReflectionUtils.findField(Overtime.class, "startDate");
        startDateField.setAccessible(true);
        startDateField.set(overtime, null);

        assertThatIllegalStateException().isThrownBy(overtime::getStartDate);
    }

    @Test
    void ensureThrowsIfGettingEndDateOnACorruptedOvertime() throws IllegalAccessException {
        Person person = createPerson();
        LocalDate now = LocalDate.now(UTC);

        Overtime overtime = new Overtime(person, now.minusDays(2), now, BigDecimal.ONE);

        Field endDateField = ReflectionUtils.findField(Overtime.class, "endDate");
        endDateField.setAccessible(true);
        endDateField.set(overtime, null);

        assertThatIllegalStateException().isThrownBy(overtime::getEndDate);
    }

    @Test
    void ensureThrowsIfGettingLastModificationDateOnACorruptedOvertime() throws IllegalAccessException {

        Person person = createPerson();
        LocalDate now = LocalDate.now(UTC);

        Overtime overtime = new Overtime(person, now.minusDays(2), now, BigDecimal.ONE);

        Field lastModificationDateField = ReflectionUtils.findField(Overtime.class, "lastModificationDate");
        lastModificationDateField.setAccessible(true);
        lastModificationDateField.set(overtime, null);

        assertThatIllegalStateException().isThrownBy(overtime::getLastModificationDate);
    }

    @Test
    void ensureCallingOnUpdateChangesLastModificationDate() throws IllegalAccessException {

        Person person = createPerson();
        LocalDate now = LocalDate.now(UTC);

        Overtime overtime = new Overtime(person, now.minusDays(2), now, BigDecimal.ONE);

        // Simulate that the overtime record has been created to an earlier time
        Field lastModificationDateField = ReflectionUtils.findField(Overtime.class, "lastModificationDate");
        lastModificationDateField.setAccessible(true);
        lastModificationDateField.set(overtime, now.minusDays(3));

        Assert.assertEquals("Wrong initial last modification date", now.minusDays(3),
            overtime.getLastModificationDate());

        overtime.onUpdate();

        Assert.assertEquals("Last modification date should be set to now", now, overtime.getLastModificationDate());
    }

    @Test
    void ensureThrowsIfTryingToSetStartDateToNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Overtime().setStartDate(null));
    }

    @Test
    void ensureThrowsIfTryingToSetEndDateToNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Overtime().setEndDate(null));
    }

    @Test
    void ensureThrowsIfTryingToSetHoursToNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Overtime().setHours(null));
    }

    @Test
    void ensureThrowsIfTryingToSetPersonToNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Overtime().setPerson(null));
    }

    @Test
    void toStringTest() {
        final Person person = new Person("Theo", "Theo", "Theo", "Theo");
        person.setId(10);
        person.setPassword("Theo");
        person.setPermissions(List.of(USER));
        person.setNotifications(List.of(NOTIFICATION_USER));
        final Overtime overtime = new Overtime(person, LocalDate.MIN, LocalDate.MAX, BigDecimal.TEN);
        overtime.setId(1);

        final String overtimeToString = overtime.toString();
        assertThat(overtimeToString).isEqualTo("Overtime{id=1, startDate=-999999999-01-01, endDate=+999999999-12-31, hours=10, person=Person{id='10'}}");
    }
}
