package org.synyx.urlaubsverwaltung.mail;

import org.springframework.core.io.ByteArrayResource;
import org.synyx.urlaubsverwaltung.person.MailNotification;
import org.synyx.urlaubsverwaltung.person.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Mail {

    private final List<Person> mailAddressRecipients;
    private final boolean sendToTechnicalMail;

    private final String templateName;
    private final Map<String, Object> templateModel;

    private final String subjectMessageKey;
    private final Object[] subjectMessageArguments;

    private final List<MailAttachment> mailAttachments;

    Mail(List<Person> mailAddressRecipients, boolean sendToTechnicalMail,
         String templateName, Map<String, Object> templateModel, String subjectMessageKey,
         Object[] subjectMessageArguments, List<MailAttachment> mailAttachments) {
        this.mailAddressRecipients = mailAddressRecipients;
        this.sendToTechnicalMail = sendToTechnicalMail;
        this.templateName = templateName;
        this.templateModel = templateModel;
        this.subjectMessageKey = subjectMessageKey;
        this.subjectMessageArguments = subjectMessageArguments;
        this.mailAttachments = mailAttachments;
    }

    public Optional<List<Person>> getMailAddressRecipients() {
        return Optional.ofNullable(mailAddressRecipients);
    }

    public boolean isSendToTechnicalMail() {
        return sendToTechnicalMail;
    }

    public String getTemplateName() {
        return templateName;
    }

    public Map<String, Object> getTemplateModel() {
        return templateModel;
    }

    public String getSubjectMessageKey() {
        return subjectMessageKey;
    }

    public Object[] getSubjectMessageArguments() {
        return subjectMessageArguments;
    }

    public Optional<List<MailAttachment>> getMailAttachments() {
        return Optional.ofNullable(mailAttachments);
    }

    public static Mail.Builder builder() {
        return new Mail.Builder();
    }

    /**
     * Mail Builder for an easier way to create a mail
     */
    public static class Builder {

        private final List<Person> mailAddressRecipients = new ArrayList<>();
        private boolean sendToTechnicalMail;

        private String templateName;
        private Map<String, Object> templateModel = new HashMap<>();

        private String subjectMessageKey;
        private Object[] subjectMessageArguments;

        private List<MailAttachment> mailAttachments;

        public Mail.Builder withTechnicalRecipient(boolean sendToTechnicalMail) {
            this.sendToTechnicalMail = sendToTechnicalMail;
            return this;
        }

        public Mail.Builder withRecipient(final Person recipient) {
            withRecipient(List.of(recipient));
            return this;
        }

        public Mail.Builder withRecipient(final Person recipient, final MailNotification mailNotification) {
            withRecipient(List.of(recipient), mailNotification);
            return this;
        }

        public Mail.Builder withRecipient(final List<Person> recipients) {
            return withRecipient(recipients, null);
        }

        public Mail.Builder withRecipient(final List<Person> recipients, final MailNotification mailNotification) {
            recipients.stream()
                .filter(person -> mailNotification == null || person.hasNotificationType(mailNotification))
                .forEachOrdered(mailAddressRecipients::add);
            return this;
        }

        public Mail.Builder withTemplate(String templateName, Map<String, Object> templateModel) {
            this.templateName = templateName;
            this.templateModel = templateModel;
            return this;
        }

        public Mail.Builder withSubject(String subjectMessageKey, Object... subjectMessageArguments) {
            this.subjectMessageKey = subjectMessageKey;
            this.subjectMessageArguments = subjectMessageArguments;
            return this;
        }

        public Mail.Builder withAttachment(String name, ByteArrayResource attachment) {
            if (mailAttachments == null) {
                mailAttachments = new ArrayList<>();
            }

            this.mailAttachments.add(new MailAttachment(name, attachment));
            return this;
        }

        public Mail build() {
            return new Mail(mailAddressRecipients, sendToTechnicalMail,
                templateName, templateModel, subjectMessageKey, subjectMessageArguments,
                mailAttachments);
        }
    }
}
