/***************************************************************
 * Copyright (c) 2014, Novamedia Oü.
 * All rights reserved.
 *
 * No Part of this file may be reproduced, stored in a retrieval
 * system, or transmitted, in any form, or by any means,
 * electronic, mechanical, photocopying, recording,
 * or otherwise, without the prior consent of Novamedia Oü
 *
 * @Author Joona Savolainen <js@novamail.ee>
 **************************************************************/

package org.novamedia.novamail.jobserver.rest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.novamedia.novamail.jobserver.core.job.ScheduledEmailSendJob;
import org.novamedia.novamail.jobserver.model.DOJobRecipient;
import org.novamedia.novamail.jobserver.model.DOScheduledEmailSendJob;
import org.novamedia.novamail.jobserver.model.utils.DOUtils;
import org.novamedia.novamail.jobserver.runtime.EmfService;
import org.novamedia.novamail.jobserver.runtime.JobScheduler;
import org.novamedia.novamail.jobserver.runtime.JobSchedulerException;
import org.novamedia.novamail.jobserver.runtime.JobStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.print.attribute.standard.JobState;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.UUID;

@Path("jobScheduler")
public class JobSchedulerResource {

	private static final Logger log = Logger.getLogger(JobSchedulerResource.class);

    @Inject
    JobScheduler jobScheduler;

    @Inject
    EmfService emfService;

    private ObjectMapper mapperScheduledEmailSend = new ObjectMapper();
    
    @POST
    @Path("/scheduledEmailSendJob")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postScheduledEmailSendJob(ScheduledEmailSendJob job) throws IOException {
    	EntityManager em = null;
    	try {
        	// TODO : validate
        	
            em = emfService.getEm();
            System.out.println("emfService: " + em);
            DOScheduledEmailSendJob doJob = DOUtils.getDOScheduledEmailSendJobv1(job, mapperScheduledEmailSend);
            em.getTransaction().begin();
            em.persist(doJob);
            Set<DOJobRecipient> doJobRecipients = DOUtils.getJobRecipients(doJob);
            for (DOJobRecipient doJobRecipient : doJobRecipients) {
        		em.persist(doJobRecipient);
            }
            em.getTransaction().commit();

            try {
                jobScheduler.scheduleJob(job);
            } catch (JobSchedulerException e) {
            	log.error(e);
            }
            return Response.ok(doJob.getJobUuid()).build();
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		try {
                em.getTransaction().rollback();
    		} catch (Exception ex2) {
    		}
    		return Response.status(500).build();
    	}
    }

    @GET
    @Path("/unscheduleJob/{job_uid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unscheduleJob(@PathParam("job_uid") String jobUid) throws IOException {
        try {
            jobScheduler.unscheduleJob(UUID.fromString(jobUid));
            return Response.ok("ok").build();
        } catch (JobSchedulerException e) {
            return Response.status(500).build();
        }
    }
}
