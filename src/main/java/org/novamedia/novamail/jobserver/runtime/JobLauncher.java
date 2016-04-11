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
package org.novamedia.novamail.jobserver.runtime;

import org.novamedia.novamail.jobserver.core.job.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobLauncher {
    private static final Logger LOG = LoggerFactory.getLogger(JobLauncher.class);

    protected final Job sendJob;

    public JobLauncher(Job sendJob) {
        this.sendJob = sendJob;
    }



}
