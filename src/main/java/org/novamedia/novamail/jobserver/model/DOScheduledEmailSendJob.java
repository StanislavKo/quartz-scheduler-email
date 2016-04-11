package org.novamedia.novamail.jobserver.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "JOB_SCHEDULED_EMAIL_SEND")
@DiscriminatorValue("SES")
public class DOScheduledEmailSendJob extends DOJob {

}
