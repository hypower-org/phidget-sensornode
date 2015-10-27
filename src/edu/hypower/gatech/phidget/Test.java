package edu.hypower.gatech.phidget;

import java.lang.reflect.Constructor;
import java.util.concurrent.ArrayBlockingQueue;

import com.phidgets.InterfaceKitPhidget;

public class Test {
	public static void main(String[] args) throws Exception {
		InterfaceKitPhidget ikit = new InterfaceKitPhidget();
		Class<?> sensor = Class.forName("edu.hypower.gatech.phidget.sensor.temperatureSensorReader");
		Class[] param = {Integer.class, String.class, InterfaceKitPhidget.class, ArrayBlockingQueue.class};
		Constructor<?> cons = sensor.getConstructor(param);
		ArrayBlockingQueue<Float> queue = new ArrayBlockingQueue<Float>(1);
		queue.offer(1.0f);
		cons.newInstance(0,"temperature.0",ikit,queue);
	}

}
