package edu.hypower.gatech.phidget;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.lang.reflect.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;

import edu.hypower.gatech.phidget.sensor.*;

/**
 * This class implements the startup process for a phidget-sensornode: 1)
 * configuration file interpretation, 2) sensor data collection configuration,
 * 3) network setup (and discovery), 4) transmission (and receiving) of data.
 * 
 * @author pjmartin
 *
 */
public class PhidgetSensorNode {

	// Stores the raw data as a simple map from sensor name to its floating
	// point value.
	// final ConcurrentHashMap<String, Float> rawDataMap = new
	// ConcurrentHashMap<String, Float>();
	private final static ConcurrentHashMap<String, BlockingQueue<Float>> rawDataMap = new ConcurrentHashMap<String, BlockingQueue<Float>>();
	private final HashMap<String, Runnable> sensorRuns = new HashMap<String, Runnable>();

	public PhidgetSensorNode(String pathToConfig) throws Exception {

		/*
		 * Sensor Node structure
		 */
		try {

			// 1 - Read in properties file, which is JSON
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(new File("sensornode.json"));
			System.out.println("Sensor node IP Address: " + rootNode.get("ip-address"));

			// Phidget initialization

			System.out.println("Attaching the Interface Kit Phidget...");
			try {
				InterfaceKitPhidget ikit = new InterfaceKitPhidget();
				ikit.openAny();
				ikit.waitForAttachment();
				System.out.println("complete.");
				ikit.setRatiometric(true);

				ScheduledExecutorService exec = Executors
						.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

				Iterator<Entry<String, JsonNode>> iter = rootNode.get("sensors").fields();
				while (iter.hasNext()) {
					Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) iter.next();
					Integer location = entry.getValue().get("location").asInt();
					Integer updatePeriod = entry.getValue().get("update-period").asInt();
					String sensorStr = entry.getKey();
					String sensorKey = sensorStr + "." + location;

					System.out.println(sensorKey + ", updating every " + updatePeriod + "ms");

					// a buffer of one float
					rawDataMap.putIfAbsent(sensorKey, new ArrayBlockingQueue<Float>(1));

					String sensorName = Character.toUpperCase(sensorStr.charAt(0)) + sensorStr.substring(1, sensorStr.length());
					try {
						Class<?> sensor = Class
								.forName("edu.hypower.gatech.phidget.sensor." + sensorName + "SensorReader");
						Constructor<?> cons = sensor.getConstructor(Integer.class, String.class, InterfaceKitPhidget.class, ArrayBlockingQueue.class);

						sensorRuns.put(sensorKey, (SensorReader) cons.newInstance(location, sensorKey, ikit,
								(ArrayBlockingQueue<Float>) rawDataMap.get(sensorKey)));

						exec.scheduleAtFixedRate(sensorRuns.get(sensorKey), 0, updatePeriod, TimeUnit.MILLISECONDS);

						// 3 - Load parameters for the server

						// 4 - create the network clients that wait on new data to be
						// available in a blocking queue
						// The client runnable only executes when its assigned data is ready
						// for transmission.
					} catch (ClassNotFoundException cnfe){
						System.err.println("ERROR: " + sensorName + " not implemented.");
						
					}
				}
			} catch (PhidgetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		} catch (JsonProcessingException e) {
			System.err.println("JSON ERROR: " + e.getMessage());
			System.exit(-1);
		} catch (IOException e) {
			System.err.println("ERROR: " + e.getMessage());
			System.exit(-1);
		}

	}

	public static void main(String[] args) {

		try {
			final PhidgetSensorNode node = new PhidgetSensorNode("");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Node running: " + rawDataMap.keySet());
		}
	}

}
