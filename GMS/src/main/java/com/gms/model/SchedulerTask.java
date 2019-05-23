package com.gms.model;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
 

public class SchedulerTask extends QuartzJobBean {

	private SensorTask sensorTask;
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		System.out.println("===========quartz=========");
		sensorTask.getSensorInfoFromFirebase();
	}

    public void setSensorTask(SensorTask sensorTask) {
        this.sensorTask = sensorTask;
    }
}
