package org.novamedia.novamail.jobserver.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "JOB_RECIPIENT", uniqueConstraints = @UniqueConstraint(columnNames = { "JOB_ID", "RECIPIENT_ID" }) )
public class DOJobRecipient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "JOB_RECIPIENT_ID", nullable = false, updatable = false, unique = true)
	protected Integer jobRecipientId;

	@ManyToOne
	@JoinColumn(name="JOB_ID", foreignKey = @ForeignKey(name = "JOB_RECIPIENT_FKEY_JOB_ID") )
	private DOJob job;

	@Column(name = "RECIPIENT_ID", nullable = false)
	protected Integer recipientId;

	@Column(name = "STATUS", nullable = false)
	protected Integer status;

	public Integer getJobRecipientId() {
		return jobRecipientId;
	}

	public void setJobRecipientId(Integer jobRecipientId) {
		this.jobRecipientId = jobRecipientId;
	}

	public DOJob getJob() {
		return job;
	}

	public void setJob(DOJob job) {
		this.job = job;
	}

	public Integer getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(Integer recipientId) {
		this.recipientId = recipientId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
