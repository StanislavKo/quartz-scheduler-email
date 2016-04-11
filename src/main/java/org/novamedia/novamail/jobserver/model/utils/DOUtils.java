package org.novamedia.novamail.jobserver.model.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.novamedia.novamail.jobserver.core.job.ScheduledEmailSendJob;
import org.novamedia.novamail.jobserver.model.DOJob;
import org.novamedia.novamail.jobserver.model.DOJobRecipient;
import org.novamedia.novamail.jobserver.model.DOScheduledEmailSendJob;
import org.novamedia.novamail.jobserver.runtime.JobStatus;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DOUtils {

	public static DOScheduledEmailSendJob getDOScheduledEmailSendJobv1(ScheduledEmailSendJob job, ObjectMapper mapper) throws JsonGenerationException, JsonMappingException, IOException {
		UUID scheduleId = UUID.randomUUID();
		DOScheduledEmailSendJob doJob = new DOScheduledEmailSendJob();
		doJob.setJobStatus(JobStatus.NONE.getCode());
		doJob.setJobUuid(scheduleId.toString());
		doJob.setJobVersion(1);
		StringWriter sw = new StringWriter();
		mapper.writeValue(sw, job);
		System.out.println("sw:" + sw.toString());
		doJob.setRaw(sw.toString().getBytes(Charset.forName("UTF-8")));
		return doJob;
	}
	
	public static Set<DOJobRecipient> getJobRecipients(DOJob doJob) {
		Set<DOJobRecipient> doJobRecipients = new HashSet<DOJobRecipient>();
		
		Integer recipientId = (int) (Math.random()*10);
		DOJobRecipient doJobRecipient = new DOJobRecipient();
		doJobRecipient.setJob(doJob);
		doJobRecipient.setRecipientId(recipientId);
		doJobRecipient.setStatus(1);
		doJobRecipients.add(doJobRecipient);
		
		doJobRecipient = new DOJobRecipient();
		doJobRecipient.setJob(doJob);
		doJobRecipient.setRecipientId(recipientId + 1);
		doJobRecipient.setStatus(1);
		doJobRecipients.add(doJobRecipient);
		
		return doJobRecipients;
	}

}
