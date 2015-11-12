package edu.hypower.gatech.phidget.sensor;

import java.util.concurrent.ArrayBlockingQueue;

import com.phidgets.InterfaceKitPhidget;

public class MagneticSensorReader extends SensorReader{

	public MagneticSensorReader(Integer location, String sensorKey, InterfaceKitPhidget interfaceKit,
			ArrayBlockingQueue<Float> q) {
		super(location, sensorKey, interfaceKit, q);
		// TODO Auto-generated constructor stub
	}

  //to Gauss
	@Override
	public float convertFromRaw(int rawVal) {
		return (float) (500 - rawVal);
	}

}

