package edu.hypower.gatech.phidget;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class implements the startup process for a phidget-sensornode: 1) configuration file
 * interpretation, 2) sensor data collection configuration, 3) network setup (and discovery), 
 * 4) transmission (and receiving) of data.
 * 
 * @author pjmartin
 *
 */
public class PhidgetSensorNode {

	// Stores the raw data as a simple map from sensor name to its floating point value.
    final ConcurrentHashMap<String,Float> rawDataMap = new ConcurrentHashMap<String, Float>();

    public static void main(String[] args){
    	
    	/*
    	 * Sensor Node structure
    	 */
    	try {
			
        	// 1 - Read in properties file, which is JSON
        	ObjectMapper mapper = new ObjectMapper();
    		JsonNode rootNode = mapper.readTree(new File("sensornode.json"));
			System.out.println("Sensor node IP Address: " + rootNode.get("ip-address"));
			JsonNode sensorsNode = rootNode.get("sensors");
			
			
        	// 2 - Create a collection of runnables that collect data from the sensors; associate each with the update period value
        	
        	// 3 - submit all sensor collecting runnables to the timed executor
        	
        	// 4 - establish connection to the server
        	
		} catch (JsonProcessingException e) {
			System.err.println("JSON ERROR: " + e.getMessage());
			System.exit(-1);
		} catch (IOException e) {
			System.err.println("ERROR: " + e.getMessage());
			System.exit(-1);
		}
    	
    	
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

    	System.exit(0);
    }

}
