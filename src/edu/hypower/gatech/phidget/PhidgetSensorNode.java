package edu.hypower.gatech.phidget;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;

/**
 * This class implements the startup process for a phidget-sensornode: 1) configuration file
 * interpretation, 2) sensor data collection configuration, 3) network setup (and discovery), 
 * 4) transmission (and receiving) of data.
 * 
 * @author pjmartin
 *
 */
public class PhidgetSensorNode {

    public static void main(String[] args){

        // Stores the raw data as a simple map from sensor name to its floating point value.
        final ConcurrentHashMap<String, Float> rawDataMap = new ConcurrentHashMap<String, Float>();
        final HashMap<String, Runnable> sensorRuns = new HashMap<String, Runnable>();

        /*
         * Sensor Node structure
         */
        try {

            // 1 - Read in properties file, which is JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(new File("sensornode.json"));
            System.out.println("Sensor node IP Address: " + rootNode.get("ip-address"));
            
            // Phidget initialization
            startInterfaceKit();
            
            Iterator<Entry<String, JsonNode>> iter = rootNode.get("sensors").fields();
            while(iter.hasNext()){
            	Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) iter.next();
            	String sensorName = entry.getKey();
            	Integer location = entry.getValue().get("location").asInt();
            	Integer updatePeriod = entry.getValue().get("update-period").asInt();
            	System.out.println(sensorName + "." + location + ", updating every " + updatePeriod + "ms");
            	sensorRuns.put(sensorName + "." + location, new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						
					}
            		
            	});
            }
            
            
            // 2 - Sensor data setup

            // 3 - establish connection to the collection server
            
            // 4 - spawn the collection of runnables that send data to the server using the specified update rate.

        } catch (JsonProcessingException e) {
            System.err.println("JSON ERROR: " + e.getMessage());
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
            System.exit(-1);
        }

        System.exit(0);
    }
    
    final static void startInterfaceKit(){
		System.out.println("Attaching the Interface Kit Phidget...");
		InterfaceKitPhidget ikit;
		try {
			ikit = new InterfaceKitPhidget();
			ikit.openAny();
			ikit.waitForAttachment();
			System.out.println("complete.");
			ikit.setRatiometric(true);

		} catch (PhidgetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
