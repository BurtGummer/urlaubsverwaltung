package org.synyx.urlaubsverwaltung.person.web;

public class PersonNotificationsDto {

    private Integer personId;

    private boolean all;

    private PersonNotificationDto applicationAppliedForManagement;
    private PersonNotificationDto applicationTemporaryAllowedForManagement;
    private PersonNotificationDto applicationAllowedForManagement;
    private PersonNotificationDto applicationCancellationForManagement;
    private PersonNotificationDto applicationAdaptedForManagement;
    private PersonNotificationDto applicationWaitingReminderForManagement;
    private PersonNotificationDto applicationCancellationRequestedForManagement;
    private PersonNotificationDto applicationAppliedAndChanges;
    private PersonNotificationDto applicationUpcoming;
    private PersonNotificationDto holidayReplacement;
    private PersonNotificationDto holidayReplacementUpcoming;
    private PersonNotificationDto personNewManagementAll;
    private PersonNotificationDto overtimeAppliedForManagement;
    private PersonNotificationDto overtimeAppliedByManagement;
    private PersonNotificationDto overtimeApplied;

    PersonNotificationsDto() {
        // ok
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public PersonNotificationDto getApplicationAppliedForManagement() {
        return applicationAppliedForManagement;
    }

    public void setApplicationAppliedForManagement(PersonNotificationDto applicationAppliedForManagement) {
        this.applicationAppliedForManagement = applicationAppliedForManagement;
    }

    public PersonNotificationDto getApplicationTemporaryAllowedForManagement() {
        return applicationTemporaryAllowedForManagement;
    }

    public void setApplicationTemporaryAllowedForManagement(PersonNotificationDto applicationTemporaryAllowedForManagement) {
        this.applicationTemporaryAllowedForManagement = applicationTemporaryAllowedForManagement;
    }

    public PersonNotificationDto getApplicationAllowedForManagement() {
        return applicationAllowedForManagement;
    }

    public void setApplicationAllowedForManagement(PersonNotificationDto applicationAllowedForManagement) {
        this.applicationAllowedForManagement = applicationAllowedForManagement;
    }

    public PersonNotificationDto getApplicationCancellationForManagement() {
        return applicationCancellationForManagement;
    }

    public void setApplicationCancellationForManagement(PersonNotificationDto applicationCancellationForManagement) {
        this.applicationCancellationForManagement = applicationCancellationForManagement;
    }

    public PersonNotificationDto getApplicationAdaptedForManagement() {
        return applicationAdaptedForManagement;
    }

    public void setApplicationAdaptedForManagement(PersonNotificationDto applicationAdaptedForManagement) {
        this.applicationAdaptedForManagement = applicationAdaptedForManagement;
    }

    public PersonNotificationDto getApplicationWaitingReminderForManagement() {
        return applicationWaitingReminderForManagement;
    }

    public void setApplicationWaitingReminderForManagement(PersonNotificationDto applicationWaitingReminderForManagement) {
        this.applicationWaitingReminderForManagement = applicationWaitingReminderForManagement;
    }

    public PersonNotificationDto getApplicationCancellationRequestedForManagement() {
        return applicationCancellationRequestedForManagement;
    }

    public void setApplicationCancellationRequestedForManagement(PersonNotificationDto applicationCancellationRequestedForManagement) {
        this.applicationCancellationRequestedForManagement = applicationCancellationRequestedForManagement;
    }

    public PersonNotificationDto getApplicationAppliedAndChanges() {
        return applicationAppliedAndChanges;
    }

    public void setApplicationAppliedAndChanges(PersonNotificationDto applicationAppliedAndChanges) {
        this.applicationAppliedAndChanges = applicationAppliedAndChanges;
    }

    public PersonNotificationDto getApplicationUpcoming() {
        return applicationUpcoming;
    }

    public void setApplicationUpcoming(PersonNotificationDto applicationUpcoming) {
        this.applicationUpcoming = applicationUpcoming;
    }

    public PersonNotificationDto getHolidayReplacement() {
        return holidayReplacement;
    }

    public void setHolidayReplacement(PersonNotificationDto holidayReplacement) {
        this.holidayReplacement = holidayReplacement;
    }

    public PersonNotificationDto getHolidayReplacementUpcoming() {
        return holidayReplacementUpcoming;
    }

    public void setHolidayReplacementUpcoming(PersonNotificationDto holidayReplacementUpcoming) {
        this.holidayReplacementUpcoming = holidayReplacementUpcoming;
    }

    public PersonNotificationDto getPersonNewManagementAll() {
        return personNewManagementAll;
    }

    public void setPersonNewManagementAll(PersonNotificationDto personNewManagementAll) {
        this.personNewManagementAll = personNewManagementAll;
    }

    public PersonNotificationDto getOvertimeAppliedForManagement() {
        return overtimeAppliedForManagement;
    }

    public void setOvertimeAppliedForManagement(PersonNotificationDto overtimeAppliedForManagement) {
        this.overtimeAppliedForManagement = overtimeAppliedForManagement;
    }

    public PersonNotificationDto getOvertimeAppliedByManagement() {
        return overtimeAppliedByManagement;
    }

    public void setOvertimeAppliedByManagement(PersonNotificationDto overtimeAppliedByManagement) {
        this.overtimeAppliedByManagement = overtimeAppliedByManagement;
    }

    public PersonNotificationDto getOvertimeApplied() {
        return overtimeApplied;
    }

    public void setOvertimeApplied(PersonNotificationDto overtimeApplied) {
        this.overtimeApplied = overtimeApplied;
    }
}
