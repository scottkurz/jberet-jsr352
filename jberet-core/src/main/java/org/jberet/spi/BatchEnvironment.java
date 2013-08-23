/*
 * Copyright (c) 2013 Red Hat, Inc. and/or its affiliates.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Cheng Fang - Initial API and implementation
 */
 
package org.jberet.spi;

import java.util.Properties;
import java.util.concurrent.ExecutorService;

public interface BatchEnvironment {
    /**
     * Gets the class loader suitable for loading application classes and batch artifacts.
     * @return an application class loader
     */
    ClassLoader getClassLoader();

    /**
     * Gets an implementation of ArtifactFactory appropriate for the current runtime environment.
     * @return an ArtifactFactory
     */
    ArtifactFactory getArtifactFactory();

    /**
     * Gets an ExecutorService appropriate for the current runtime environment.
     * @return an ExecutorService
     */
    ExecutorService getExecutorService();

    /**
     * Gets the UserTransaction
     * @return UserTransaction
     */
    javax.transaction.UserTransaction getUserTransaction();

    /**
     * Gets configuration data for batch container.
     * @return a key-value map of batch configuration
     */
    Properties getBatchConfigurationProperties();
}
