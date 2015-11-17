package edu.hypower.gatech.phidget.sensor;

import java.util.concurrent.ArrayBlockingQueue;

import com.phidgets.InterfaceKitPhidget;

public class LightSensorReader extends SensorReader {

	public LightSensorReader(Integer location, String sensorKey, InterfaceKitPhidget interfaceKit,
			ArrayBlockingQueue<Float> q) {
		super(location, sensorKey, interfaceKit, q);
		// TODO Auto-generated constructor stub
	}

  //unit luminosity
	@Override
	public float convertFromRaw(int rawVal) {
		return (float) ((float) (1.478777 * rawVal) + 33.67076);
	}

}
