/***************************************************************
 * Copyright (c) 2015, Novamedia Oü.
 * All rights reserved.
 *
 * No Part of this file may be reproduced, stored in a retrieval
 * system, or transmitted, in any form, or by any means,
 * electronic, mechanical, photocopying, recording,
 * or otherwise, without the prior consent of Novamedia Oü
 *
 * @Author Joona Savolainen <js@novamail.ee>
 **************************************************************/
package org.novamedia.novamail.jobserver.runtime;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.PropertiesConfiguration;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ServiceManager;

import org.novamedia.novamail.jobserver.rest.RESTServer;

public class JobServer {

    private static final Logger LOG = LoggerFactory.getLogger(JobServer.class);

    private final ServiceManager serviceManager;

    public JobServer(Properties properties)
            throws Exception {

        EmfService emfService = new EmfService();
        JobScheduler jobScheduler = new JobScheduler(properties);
        RESTServer submitterServer = new RESTServer(properties, jobScheduler, emfService);
        DispatcherService dispatcherService = new DispatcherService(jobScheduler, emfService);

        this.serviceManager = new ServiceManager(Lists.newArrayList(
                jobScheduler, submitterServer, emfService, dispatcherService
        ));
    }

    /**
     * Start this scheduler daemon.
     */
    public void start() {
        // Add a shutdown hook so the task scheduler gets properly shutdown
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                LOG.info("Shutting down the job server daemon");
                try {
                    // Give the services 5 seconds to stop to ensure that we are responsive to shutdown requests
                    serviceManager.stopAsync().awaitStopped(5, TimeUnit.SECONDS);
                } catch (TimeoutException te) {
                    LOG.error("Timeout in stopping the service manager", te);
                }
            }
        });

        LOG.info("Starting the job server daemon");
        // Start the scheduler daemon
        this.serviceManager.startAsync();
    }

    public void stop() {
        this.serviceManager.stopAsync();
    }

    public static void main(String[] args)
            throws Exception {
//        if (args.length != 1) {
//            System.err.println(
//                    "Usage: JobServer <configuration properties file>");
//            System.exit(1);
//        }

        // Load default configuration properties
//        Properties properties = ConfigurationConverter.getProperties(new PropertiesConfiguration(args[0]));
        Properties properties = new Properties();

        // Start the job server daemon
        new JobServer(properties).start();

        try {
			Thread.currentThread().sleep(60*60*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}
