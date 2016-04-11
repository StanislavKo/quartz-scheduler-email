package org.novamedia.novamail.jobserver.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "JOB")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DISCRIMINATOR", discriminatorType = DiscriminatorType.STRING, length = 4)
public class DOJob {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "JOB_ID", nullable = false, updatable = false, unique = true)
	protected Integer jobId;

	@Column(name = "JOB_UUID", nullable = false, updatable = false, unique = true)
	protected String jobUuid;

	@Column(name = "JOB_VERSION", nullable = false)
	protected Integer jobVersion;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "RAW", length = Integer.MAX_VALUE, columnDefinition = "BLOB NOT NULL")
	private byte[] raw;

	@Column(name = "JOB_STATUS", nullable = false)
	protected Integer jobStatus;

	@OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
	protected Set<DOJobRecipient> jobRecipients = new HashSet<DOJobRecipient>();

	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public String getJobUuid() {
		return jobUuid;
	}

	public void setJobUuid(String jobUuid) {
		this.jobUuid = jobUuid;
	}

	public Integer getJobVersion() {
		return jobVersion;
	}

	public void setJobVersion(Integer jobVersion) {
		this.jobVersion = jobVersion;
	}

	public byte[] getRaw() {
		return raw;
	}

	public void setRaw(byte[] raw) {
		this.raw = raw;
	}

	public Integer getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(Integer jobStatus) {
		this.jobStatus = jobStatus;
	}

	public Set<DOJobRecipient> getJobRecipients() {
		return jobRecipients;
	}

	public void setJobRecipients(Set<DOJobRecipient> jobRecipients) {
		this.jobRecipients = jobRecipients;
	}

}
