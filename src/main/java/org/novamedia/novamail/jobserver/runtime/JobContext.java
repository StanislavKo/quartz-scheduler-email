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

import java.util.UUID;

public class JobContext {
    private final UUID jobId;
    private final Job job;

    public JobContext(UUID jobId, Job job) {
        this.jobId = jobId;
        this.job = job;
    }
}
