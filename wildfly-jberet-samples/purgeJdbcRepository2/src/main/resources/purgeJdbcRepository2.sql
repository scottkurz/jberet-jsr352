delete from STEP_EXECUTION where JOBEXECUTIONID in
    (select JOBEXECUTIONID from JOB_EXECUTION, JOB_INSTANCE
        where JOB_EXECUTION.JOBINSTANCEID = JOB_INSTANCE.JOBINSTANCEID and JOB_INSTANCE.JOBNAME = 'prepurge2');


delete from JOB_EXECUTION where JOBINSTANCEID in
    (select DISTINCT JOBINSTANCEID from JOB_INSTANCE where JOBNAME = 'prepurge2')
