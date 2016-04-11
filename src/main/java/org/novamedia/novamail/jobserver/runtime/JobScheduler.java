package org.novamedia.novamail.jobserver.runtime;

/**
 * ************************************************************
 * Copyright (c) 2015, Novamedia Oü.
 * All rights reserved.
 * <p/>
 * No Part of this file may be reproduced, stored in a retrieval
 * system, or transmitted, in any form, or by any means,
 * electronic, mechanical, photocopying, recording,
 * or otherwise, without the prior consent of Novamedia Oü
 *
 * @Author Joona Savolainen <js@novamail.ee>
 * ************************************************************
 */
import com.google.common.io.Closer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.collect.Maps;
import com.google.common.collect.HashBiMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.novamedia.novamail.jobserver.core.job.Job;

public class JobScheduler extends AbstractIdleService {

    private static final Logger LOG = LoggerFactory.getLogger(JobScheduler.class);

    public static final String JOB_SCHEDULER_KEY = "jobScheduler";
    public static final String JOB_ID_KEY = "jobUUID";
    public static final String JOB_KEY = "sendJob";
    public static final String JOB_LISTENER_KEY = "jobListener";

    // System configuration properties
    private final Properties properties;

    // A Quartz scheduler
    private final Scheduler scheduler;

    // Mapping between jobs to job listeners associated with them
    //private final Map<String, JobListener> jobListenerMap = Maps.newHashMap();

    // Mapping between send job name and schedule UUID
    private final HashBiMap<String, UUID> sendJobIdMap = HashBiMap.create();

    // A map for all scheduled jobs
    private final Map<UUID, JobKey> scheduledJobs = Maps.newHashMap();

    public JobScheduler(Properties properties) throws Exception  {
        this.properties = properties;
        this.scheduler = new StdSchedulerFactory().getScheduler();
    }

    public UUID scheduleJob(Job job) throws JobSchedulerException {
        if (this.sendJobIdMap.containsKey(job.toString())) {
            LOG.warn("Trying to schedule already scheduled job " + job);
            throw new JobSchedulerException("Job has already been scheduled");
        }

        UUID scheduleId = UUID.randomUUID();

        // Build a data map that gets passed to the job
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(JOB_SCHEDULER_KEY, this);
        jobDataMap.put(JOB_ID_KEY, scheduleId);
        jobDataMap.put(JOB_KEY, job);
        //jobDataMap.put(JOB_LISTENER_KEY, jobListener);

        JobDetail jobDetail = JobBuilder.newJob(QuartzJob.class)
                .withIdentity(job.toString())
                .usingJobData(jobDataMap)
                .build();

        try {
            // Schedule the Quartz job with a trigger built from the job configuration
            this.scheduler.scheduleJob(jobDetail, job.getTrigger());
        } catch (SchedulerException se) {
            LOG.error("Failed to schedule job " + job, se);
            throw new JobSchedulerException("Failed to schedule job " + job, se);
        }

        this.sendJobIdMap.put(job.toString(), scheduleId);
        this.scheduledJobs.put(scheduleId, jobDetail.getKey());

        return scheduleId;
    }

    public void unscheduleJob(Job sendJob) throws JobSchedulerException {
        if (this.sendJobIdMap.containsKey(sendJob.toString())) {
            unscheduleJob(this.sendJobIdMap.get(sendJob.toString()));
        }
    }

    public void unscheduleJob(UUID uid) throws JobSchedulerException {
        if (this.scheduledJobs.containsKey(uid)) {
            try {
                this.scheduler.deleteJob(this.scheduledJobs.remove(uid));
                this.sendJobIdMap.inverse().remove(uid);
            } catch (SchedulerException se) {
                LOG.error("Failed to unschedule and delete job " + uid, se);
                throw new JobSchedulerException("Failed to unschedule and delete job " + uid, se);
            }
        }
    }

    public void runJob(Job sendJob) {
        Closer closer = Closer.create();



    }

    @Override
    protected void startUp()
            throws Exception {
        LOG.info("Starting the job scheduler");

        this.scheduler.start();
    }

    @Override
    protected void shutDown()
            throws Exception {
        LOG.info("Stopping the job scheduler");

        this.scheduler.shutdown(true);
    }
    

	@DisallowConcurrentExecution
    public static class QuartzJob implements org.quartz.Job {

        @Override
        public void execute(JobExecutionContext context)
                throws JobExecutionException {
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            JobScheduler jobScheduler = (JobScheduler) dataMap.get(JOB_SCHEDULER_KEY);
            Job sendJob = (Job) dataMap.get(JOB_KEY);

            try {
                jobScheduler.runJob(sendJob);
            } catch (Throwable t) {
                throw new JobExecutionException(t);
            }
        }
    }
}
