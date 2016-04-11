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

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.base.Optional;

import java.net.URI;
import java.util.Properties;

import org.novamedia.novamail.jobserver.runtime.JobScheduler;
import org.novamedia.novamail.jobserver.runtime.ConfigurationKeys;
import org.novamedia.novamail.jobserver.runtime.EmfService;

public class RESTServer extends AbstractIdleService {

    private static final Logger LOG = LoggerFactory.getLogger(RESTServer.class);
    private final Properties properties;
    private final JobScheduler jobScheduler;
    private final EmfService emfService;
    private volatile Optional<HttpServer> httpServer;

    public RESTServer(Properties properties, JobScheduler jobScheduler, EmfService emfService) {
        this.properties = properties;
        this.jobScheduler = jobScheduler;
        this.emfService = emfService;
    }

    @Override
    protected void startUp() throws Exception {
        // Server port
        int port = Integer.parseInt(
                properties.getProperty(ConfigurationKeys.REST_SERVER_PORT_KEY, ConfigurationKeys.DEFAULT_REST_SERVER_PORT));

        URI uri = URI.create(String.format("http://%s:%d",
                properties.getProperty(ConfigurationKeys.REST_SERVER_HOST_KEY, ConfigurationKeys.DEFAULT_REST_SERVER_HOST),
                port));

        final ResourceConfig resourceConfig = new ResourceConfig()
                .packages("org.novamedia.novamail.jobserver.rest")
                .register(ObjectMapperProvider.class)
                .register(JacksonFeature.class)
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(jobScheduler).to(JobScheduler.class);
                    }
                })
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(emfService).to(EmfService.class);
                    }
                });
                

        this.httpServer = Optional.of(GrizzlyHttpServerFactory.createHttpServer(uri, resourceConfig, false));

        LOG.info("Starting job submitter API");
        this.httpServer.get().start();
    }

    @Override
    protected void shutDown() throws Exception {
        if (this.httpServer.isPresent()) {
            LOG.info("Stopping job submitter API");
            this.httpServer.get().shutdown();
        }
    }
}
