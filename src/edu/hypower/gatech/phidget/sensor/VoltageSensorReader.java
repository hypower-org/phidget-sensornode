package edu.hypower.gatech.phidget.sensor;

import java.util.concurrent.ArrayBlockingQueue;

import com.phidgets.InterfaceKitPhidget;

public class VoltageSensorReader extends SensorReader {

	public VoltageSensorReader(Integer location, String sensorKey, InterfaceKitPhidget interfaceKit,
			ArrayBlockingQueue<Float> q) {
		super(location, sensorKey, interfaceKit, q);
		// TODO Auto-generated constructor stub
	}

	@Override
	public float convertFromRaw(int rawVal) {
      return (float) ((rawVal/200) - 2.5)/0.0681;
	}

}
