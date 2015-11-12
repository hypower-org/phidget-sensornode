package edu.hypower.gatech.phidget.sensor;

import java.util.concurrent.ArrayBlockingQueue;

import com.phidgets.InterfaceKitPhidget;

public class CurrentSensorReader extends SensorReader {

    public CurrentSensorReader(Integer location, String sensorKey, InterfaceKitPhidget interfaceKit,
            ArrayBlockingQueue<Float> q) {
        super(location, sensorKey, interfaceKit, q);
        // TODO Auto-generated constructor stub
    }

    //DC or AC current
    @Override
    public float convertFromRaw(int rawVal) {
        //DC return (float) (rawVal)/13.2 - 37.8787;
        //AC
        return (float) (rawVal * 0.04204);
    }

}
