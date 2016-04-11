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
package org.novamedia.novamail.jobserver.core.loader;

import java.util.Properties;

abstract public class QueueLoader {
    protected final Properties properties;

    public QueueLoader(Properties properties) {
        this.properties = properties;
    }
}
