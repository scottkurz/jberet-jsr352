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

import javax.enterprise.context.Dependent;

import org.jberet.testapps.cdiscopes.commons.ScopeArtifactBase;

import jakarta.batch.api.BatchProperty;
import jakarta.batch.api.Batchlet;
import jakarta.batch.operations.JobOperator;
import jakarta.batch.runtime.BatchRuntime;
import jakarta.batch.runtime.context.JobContext;
import jakarta.batch.runtime.context.StepContext;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mypkg.BatchPropertyLiteral;

@Named("s3")
@Dependent
public class StepScopeBatchlet3 extends ScopeArtifactBase implements Batchlet {
    @Inject
    private Foo fooTypeTarget;

    StepScopeBatchlet3() {}
    String c1;
    String c2;

    public void setter(@BatchProperty(name="aa") String c1, @BatchProperty(name="aa") String aa) {
        this.c1 = c1;
        this.c2 = aa;
    }

    @Inject
    @BatchProperty(name = "aa")
    String aa;

    @Inject
    StepContext stepCtx;

    @Inject
    JobContext jc;
    
    //@Inject
    JobOperator jo;

    @Override
    public String process() throws Exception {

    	jo = BatchRuntime.getJobOperator();
		System.out.println("SKSK: In process, jo running = " + jo.getRunningExecutions(jc.getJobName()));
		System.out.println("SKSK: In process, jc exec id = " + jc.getExecutionId());


		
        System.out.println("SKSK: in batchlet, c1 = " + c1);
        System.out.println("SKSK: in batchlet, c2 = " + c2);
        System.out.println("SKSK: in batchlet, aa = " + aa);
        System.out.println("step name = " + stepCtx.getStepName());
        
		Instance<String> myBatchProp = CDI.current().select(String.class, new BatchPropertyLiteral("aa"));
		String mbpStr = myBatchProp.get();
	
        
        
       fooTypeTarget.m1();
        return "OK";
    }
}
