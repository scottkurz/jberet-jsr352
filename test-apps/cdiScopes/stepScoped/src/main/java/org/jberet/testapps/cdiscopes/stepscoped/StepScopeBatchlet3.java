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

import jakarta.batch.api.Batchlet;
import jakarta.batch.api.BatchProperty;
import jakarta.inject.Inject;
import javax.enterprise.context.Dependent;
import jakarta.batch.runtime.context.StepContext;
import jakarta.inject.Named;

import org.jberet.testapps.cdiscopes.commons.FooFieldTarget;
import org.jberet.testapps.cdiscopes.commons.FooMethodTarget;
import org.jberet.testapps.cdiscopes.commons.ScopeArtifactBase;

@Named("s3")
@Dependent
public class StepScopeBatchlet3 extends ScopeArtifactBase implements Batchlet {
    @Inject
    private Foo fooTypeTarget;

        @Inject
    @BatchProperty(name = "aa")
    String aa;

    @Inject
    StepContext stepCtx;

    @Override
    public String process() throws Exception {
        System.out.println("SKSK: in batchlet, aa = " + aa);
        System.out.println("step name = " + stepCtx.getStepName());
       fooTypeTarget.m1();
        return "OK";
    }
}
