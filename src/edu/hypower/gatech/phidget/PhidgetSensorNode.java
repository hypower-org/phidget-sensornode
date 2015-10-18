package edu.hypower.gatech.phidget;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class implements the startup process for a phidget-sensornode: 1) configuration file
 * interpretation, 2) network setup (and discovery), 3) transmission (and receiving) of data.
 * 
 * @author pjmartin
 *
 */
public class PhidgetSensorNode {

    final ConcurrentHashMap<String,Float> rawDataMap = new ConcurrentHashMap<String, Float>();

    public static void main(String[] args){
    	
    	/*
    	 * Sensor Node structure
    	 */
    	
    	// 1 - Read in properties file, which is JSON
    	
    	
    	// 2 - Create a collection of runnables that collect data from the sensors; associate each with the update period value
    	
    	// 3 - submit all sensor collecting runnables to the timed executor
    	
    	// 4 - establish connection to the server
    	
//        SensorProps prop = SensorProps.getInstance();
//
//        // Load the properties file into the SensorProps data structure. 
//        Set<String> propKeys = prop.getAllProperties();
//        Set<String>[] hashKeys = prop.getProperty(propKeys[0]).split(",");
//        String[] periods = prop.getProperty(propKeys[1]).split(",");
//        int numSensors = 0;
//        for(String key: hashKeys) 
//        {
//            if(key != "-1") 
//            {
//                numSensors++;
//
//
//            }
//        }
            //Set new property in
            //Verify new property
            //System.out.println(prop.getAllProperties());
            //prop.store();
    }

}
