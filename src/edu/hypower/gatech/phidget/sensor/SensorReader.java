package edu.hypower.gatech.phidget.sensor;

import java.util.concurrent.ArrayBlockingQueue;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;

public abstract class SensorReader implements Runnable {

	private final int location;
	private final String sensorKey;
	private final ArrayBlockingQueue<Float> sensorQ;
	private final InterfaceKitPhidget interfaceKit;

	
	
	public SensorReader(int location, String sensorKey, InterfaceKitPhidget interfaceKit, ArrayBlockingQueue<Float> q) {
		this.location = location;
		this.sensorKey = sensorKey;
		this.interfaceKit = interfaceKit;
		this.sensorQ = q;
	}

	@Override
	public final void run() {
		
		try {
			float data = this.convertFromRaw( interfaceKit.getSensorValue(location) );
			sensorQ.offer(data); // returns true if it worked, or false. Just ignore for now.
			
		} catch (PhidgetException e) {
			System.err.println("Error: cannot get sensor data.");
		}

	}

	public final int getLocation() {
		return location;
	}

	public final String getSensorKey() {
		return sensorKey;
	}

	/**
	 * SensorReaders must compute the
	 * @param rawVal
	 * @return
	 */
	public abstract float convertFromRaw(int rawVal);

}
