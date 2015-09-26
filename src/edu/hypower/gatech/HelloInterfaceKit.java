package edu.hypower.gatech;

import com.phidgets.*;
import com.phidgets.event.*;

import java.util.concurrent.*;

public class HelloInterfaceKit {

	public static void main(String[] args){
		
		final long motionPeriod = 100;
		final long tempPeriod = 500;
		final long humidPeriod = 1000;

		final ConcurrentHashMap<String,Float> dataMap = new ConcurrentHashMap<String, Float>();
		
		try {
			final InterfaceKitPhidget ikit = new InterfaceKitPhidget();
			ikit.openAny();
			ikit.waitForAttachment();
			
			ScheduledExecutorService exec = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
			
			Runnable tempTask = new Runnable(){
				public void run() {
					// temp
					try {
						float currTemp = ikit.getSensorValue(0);
						dataMap.put("T", currTemp);
					} catch (PhidgetException e) {
						e.printStackTrace();
					}
				}
			};
			
			Runnable humidTask = new Runnable(){
				public void run() {
					// humidity
					try {
						float currHumid = ikit.getSensorValue(1);
						dataMap.put("H", currHumid);
					} catch (PhidgetException e) {
						e.printStackTrace();
					}
				}
				
			};
			
			Runnable motionTask = new Runnable(){
				public void run() {
					// motion
					try {
						float currMotion = ikit.getSensorValue(2);
						dataMap.put("M", currMotion);
					} catch (PhidgetException e) {
						e.printStackTrace();
					}
				}
			};
			exec.scheduleAtFixedRate(tempTask, 0, tempPeriod, TimeUnit.MILLISECONDS);
			exec.scheduleAtFixedRate(humidTask, 0, humidPeriod, TimeUnit.MILLISECONDS);
			exec.scheduleAtFixedRate(motionTask, 0, motionPeriod, TimeUnit.MILLISECONDS);

			while(true){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(dataMap);
			}
			
		} catch (PhidgetException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
