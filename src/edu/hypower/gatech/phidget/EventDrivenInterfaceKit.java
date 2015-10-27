package edu.hypower.gatech.phidget;

import com.phidgets.*;
import com.phidgets.event.*;

public class EventDrivenInterfaceKit implements RawDataListener
// public class EventDrivenInterfaceKit
{
	@Override
	public void rawData(RawDataEvent arg0) {
		// TODO Auto-generated method stub

	}

	public static void main(String args[]) throws Exception {

		final InterfaceKitPhidget ikit;
		ikit = new InterfaceKitPhidget();

		ikit.addAttachListener(new AttachListener() {
			public void attached(AttachEvent event) {
				int serialNumber = 0;
				String name = new String();
				try {
					serialNumber = ((Phidget) event.getSource()).getSerialNumber();
					name = ((Phidget) event.getSource()).getDeviceName();
				} catch (PhidgetException exception) {
					System.out.println(exception.getDescription());
				}
				System.out.println("Device" + name + " serial: " + Integer.toString(serialNumber));
			}
		});

		ikit.addSensorChangeListener(new SensorChangeListener() {
			public void sensorChanged(SensorChangeEvent sensorEvent) {
				if (sensorEvent.getIndex() == 0) {
					float currMotion = sensorEvent.getValue();
					System.out.println("Motion changed to " + Float.toString(currMotion));
					// could just do this to get raw value
					// System.out.println(sensorEvent);
				}
			}
		});

		ikit.openAny();
		ikit.waitForAttachment();
		Thread.sleep(500);

		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// ikit.close();
		// ikit = null;

		// TODO: remove listener

		// example on using manager, not so sure

		// Manager manager;
		// manager = new Manager();
		//
		// manager.addAttachListener(attach);
		//
		// manager.open();
		//
		// System.in.read();
		//
		// manager.close();
		//
		// manager = null;

	}

}
