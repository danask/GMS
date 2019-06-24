package com.gms.model;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
 

public class SchedulerTask extends QuartzJobBean {

	private SensorTask sensorTask;
	private MotionSensorTask motionSensorTask;
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		System.out.println("===========scheduler start=========");
		sensorTask.getSensorInfoFromFirebase();
		motionSensorTask.getSensorInfoFromFirebase();
	}

    public void setSensorTask(SensorTask sensorTask) {
        this.sensorTask = sensorTask;
    }
    
    public void setMotionSensorTask(MotionSensorTask motionSensorTask) {
        this.motionSensorTask = motionSensorTask;
    }
}
