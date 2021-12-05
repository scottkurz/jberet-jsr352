/*
 * Copyright (c) 2015-2017 Red Hat, Inc. and/or its affiliates.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.jberet.testapps.cdiscopes.stepscoped;

import jakarta.inject.Named;

import jakarta.batch.api.BatchProperty;
import jakarta.inject.Inject;

import javax.enterprise.context.Dependent;
import jakarta.batch.runtime.context.StepContext;
import org.jberet.cdi.StepScoped;
import org.jberet.testapps.cdiscopes.commons.StepNameHolder;

/**
 * This bean class is annotated with {@link StepScoped} at the type level,
 * so any injected instance will have step scope as defined in {@code StepScope}.
 */
@Named
@Dependent
public class Foo extends StepNameHolder {

    
        @Inject
    @BatchProperty(name = "aa")
    String aa;

        
    @Inject
    StepContext stepCtx;

        public void m1() {
            System.out.println("SKSK: in Foo, aa = " + aa);
                    System.out.println("step name = " + stepCtx.getStepName());
        }
}
