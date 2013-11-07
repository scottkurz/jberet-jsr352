/*
 * Copyright (c) 2012-2013 Red Hat, Inc. and/or its affiliates.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Cheng Fang - Initial API and implementation
 */

package org.jberet.se.test;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;

import org.jberet.runtime.JobExecutionImpl;
import org.junit.Assert;
import org.junit.Test;

public class Batchlet1Test {
    private static final String jobXmlName = "batchlet1.xml";
    private final JobOperator jobOperator = BatchRuntime.getJobOperator();

    @Test
    public void testBatchlet1() throws Exception {
        long jobExecutionId;
        jobExecutionId = startJobMatchEnd();
        jobExecutionId = startJobMatchNone();
        jobExecutionId = startJobMatchFail();
        jobExecutionId = restartJobMatchStop(jobExecutionId);
    }

    private Properties createParams(final String key, final String val) {
        final Properties params = new Properties();
        params.setProperty(key, val);
        return params;
    }

    private long startJobMatchNone() throws Exception {
        final Properties params = createParams("action", Batchlet1.ACTION_OTHER);
        //start the job and complete step1 and step2, not matching any transition element in step2
        params.setProperty("action", Batchlet1.ACTION_OTHER);
        System.out.printf("Start with params %s%n", params);
        final long jobExecutionId = jobOperator.start(jobXmlName, params);
        final JobExecutionImpl jobExecution = (JobExecutionImpl) jobOperator.getJobExecution(jobExecutionId);
        jobExecution.awaitTermination(JobExecutionImpl.JOB_EXECUTION_TIMEOUT_SECONDS_DEFAULT, TimeUnit.SECONDS);
        System.out.printf("JobExecution id: %s%n", jobExecution.getExecutionId());
        Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());
        Assert.assertEquals(BatchStatus.COMPLETED.name(), jobExecution.getExitStatus());

        Assert.assertEquals(2, jobExecution.getStepExecutions().size());
        Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getStepExecutions().get(0).getBatchStatus());
        Assert.assertEquals(BatchStatus.COMPLETED.name(), jobExecution.getStepExecutions().get(0).getExitStatus());
        Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getStepExecutions().get(1).getBatchStatus());
        Assert.assertEquals(Batchlet1.ACTION_OTHER, jobExecution.getStepExecutions().get(1).getExitStatus());
        return jobExecutionId;
    }

    private long startJobMatchEnd() throws Exception {
        //start the job and complete step1 and step2, matching <end> element in step2
        final Properties params = createParams("action", Batchlet1.ACTION_END);
        System.out.printf("Start with params %s%n", params);
        final long jobExecutionId = jobOperator.start(jobXmlName, params);
        final JobExecutionImpl jobExecution = (JobExecutionImpl) jobOperator.getJobExecution(jobExecutionId);
        jobExecution.awaitTermination(JobExecutionImpl.JOB_EXECUTION_TIMEOUT_SECONDS_DEFAULT, TimeUnit.SECONDS);
        System.out.printf("JobExecution id: %s%n", jobExecution.getExecutionId());
        Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());
        Assert.assertEquals(Batchlet1.ACTION_END, jobExecution.getExitStatus());

        Assert.assertEquals(2, jobExecution.getStepExecutions().size());
        Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getStepExecutions().get(0).getBatchStatus());
        Assert.assertEquals(BatchStatus.COMPLETED.name(), jobExecution.getStepExecutions().get(0).getExitStatus());
        Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getStepExecutions().get(1).getBatchStatus());
        Assert.assertEquals(Batchlet1.ACTION_END, jobExecution.getStepExecutions().get(1).getExitStatus());
        return jobExecutionId;
    }

    private long startJobMatchFail() throws Exception {
        //start the job and fail at the end of step2, matching <fail> element in step2
        final Properties params = createParams("action", Batchlet1.ACTION_FAIL);
        params.setProperty("action", Batchlet1.ACTION_FAIL);
        System.out.printf("Start with params %s%n", params);
        final long jobExecutionId = jobOperator.start(jobXmlName, params);
        final JobExecutionImpl jobExecution = (JobExecutionImpl) jobOperator.getJobExecution(jobExecutionId);
        jobExecution.awaitTermination(JobExecutionImpl.JOB_EXECUTION_TIMEOUT_SECONDS_DEFAULT, TimeUnit.SECONDS);
        System.out.printf("JobExecution id: %s%n", jobExecution.getExecutionId());
        Assert.assertEquals(BatchStatus.FAILED, jobExecution.getBatchStatus());
        Assert.assertEquals(Batchlet1.ACTION_FAIL, jobExecution.getExitStatus());  //set by <fail> element

        Assert.assertEquals(2, jobExecution.getStepExecutions().size());
        Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getStepExecutions().get(0).getBatchStatus());
        Assert.assertEquals(BatchStatus.COMPLETED.name(), jobExecution.getStepExecutions().get(0).getExitStatus());

        // <fail> element does not affect the already-completed step batchlet execution.
        // Although the job FAILED, but step2 still COMPLETED
        Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getStepExecutions().get(1).getBatchStatus());
        // step2 exit status from batchlet1.process method return value, not from <fail exit-status> element
        Assert.assertEquals(Batchlet1.ACTION_FAIL, jobExecution.getStepExecutions().get(1).getExitStatus());
        return jobExecutionId;
    }

    private long restartJobMatchStop(final long previousJobExecutionId) throws Exception {
        //restart the job and stop at the end of step2, matching <stop> element in step2.
        //next time this job execution is restarted, it should restart from restart-point step2
        final Properties params = createParams("action", Batchlet1.ACTION_STOP);
        System.out.printf("Restart with params %s%n", params);
        final long jobExecutionId = jobOperator.restart(previousJobExecutionId, params);
        final JobExecutionImpl jobExecution = (JobExecutionImpl) jobOperator.getJobExecution(jobExecutionId);
        jobExecution.awaitTermination(JobExecutionImpl.JOB_EXECUTION_TIMEOUT_SECONDS_DEFAULT, TimeUnit.SECONDS);
        System.out.printf("JobExecution id: %s%n", jobExecution.getExecutionId());
        Assert.assertEquals(BatchStatus.STOPPED, jobExecution.getBatchStatus());
        Assert.assertEquals(Batchlet1.ACTION_STOP, jobExecution.getExitStatus());

        Assert.assertEquals(1, jobExecution.getStepExecutions().size());
        // <stop> element does not affect the already-completed step batchlet execution.
        // Although the job STOPPED, but step2 still COMPLETED
        Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getStepExecutions().get(0).getBatchStatus());
        // step2 exit status from batchlet1.process method return value, not from <stop exit-status> element
        Assert.assertEquals(Batchlet1.ACTION_STOP, jobExecution.getStepExecutions().get(0).getExitStatus());
        return jobExecutionId;
    }
}
