/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mybatch.runtime.context;

import java.util.Properties;
import javax.batch.runtime.context.JobContext;

public class JobContextImpl<T> extends BatchContextImpl<T> implements JobContext<T> {
    private long instanceId;
    private long executionId;
    private Properties properties;

    public JobContextImpl(String id, long instanceId, long executionId) {
        super(id);
        this.instanceId = instanceId;
        this.executionId = executionId;
    }

    public JobContextImpl(String id, long instanceId, long executionId, Properties p) {
        this(id, instanceId, executionId);
        this.properties = p;
    }

    @Override
    public long getInstanceId() {
        return instanceId;
    }

    @Override
    public long getExecutionId() {
        return executionId;
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties p) {
        this.properties = p;
    }

    public void addProperty(String key, String value) {
        properties.setProperty(key, value);
    }
}
