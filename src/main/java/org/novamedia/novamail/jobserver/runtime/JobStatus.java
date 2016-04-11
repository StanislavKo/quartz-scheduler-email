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

public enum JobStatus {
    NONE(0, "none"),
    INITIALIZING(1, "initializing"),
    SCHEDULED(2, "scheduled"),
    PROCESSING(3, "processing"),
    PAUSED(4, "paused"),
    FINISHED(5, "finished"),
    CANCELLED(6, "cancelled"),
    WAITING(7, "waiting"),
    FAILED(8, "failed");

    private final int code;
    private final String name;

    JobStatus(final int code, final String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return name;
    }
}