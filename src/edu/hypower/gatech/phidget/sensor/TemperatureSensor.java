/**
 * 
 */
package edu.hypower.gatech.phidget.sensor;

import java.util.concurrent.ConcurrentHashMap;

import com.phidgets.event.SensorChangeEvent;
import com.phidgets.event.SensorChangeListener;

/**
 * @author pjmartin
 *
 */
public class TemperatureSensor implements SensorChangeListener {

	private ConcurrentHashMap<String, Float> dataMap;

	public TemperatureSensor(ConcurrentHashMap<String, Float> map) {
		dataMap = map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.phidgets.event.SensorChangeListener#sensorChanged(com.phidgets.event.
	 * SensorChangeEvent)
	 */
	@Override
	public void sensorChanged(SensorChangeEvent arg0) {
		// TODO Auto-generated method stub

	}

}
