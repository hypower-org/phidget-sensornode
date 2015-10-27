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

import edu.hypower.gatech.phidget.sensor.SensorReader;
import edu.hypower.gatech.phidget.sensor.temperatureSensorReader;

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
	private static final int Float = 0;
	private final HashMap<String, Runnable> sensorRuns = new HashMap<String, Runnable>();

	public PhidgetSensorNode(String pathToConfig) {

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
					String sensorKey = entry.getKey() + "." + location;

					System.out.println(sensorKey + ", updating every " + updatePeriod + "ms");

					// a buffer of one float
					rawDataMap.putIfAbsent(sensorKey, new ArrayBlockingQueue<Float>(1));

					// Build a SensorReader for each sensor configuration! Pass
					// in the reference to the data queue.
					// TODO: Need to create the particular sensor that matches
					// what is provided in the JSON
					// file -- Java use reflection. We may need to change class
					// names.
					Class<?> sensor = null;
					try {
						sensor = Class.forName(entry.getKey() + "SensorReader");
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Class[] param = { Integer.class, String.class, InterfaceKitPhidget.class,
							ArrayBlockingQueue.class };
					Constructor<?> cons = null;
					try {
						cons = sensor.getConstructor(param);
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					try {
						sensorRuns.put(sensorKey, (SensorReader) cons.newInstance(location, sensorKey, ikit,
								(ArrayBlockingQueue<Float>) rawDataMap.get(sensorKey)));
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					exec.scheduleAtFixedRate(sensorRuns.get(sensorKey), 0, updatePeriod, TimeUnit.MILLISECONDS);
				}
			} catch (PhidgetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 3 - Load parameters for the server

			// 4 - create the network clients that wait on new data to be
			// available in a blocking queue
			// The client runnable only executes when its assigned data is ready
			// for transmission.

		} catch (JsonProcessingException e) {
			System.err.println("JSON ERROR: " + e.getMessage());
			System.exit(-1);
		} catch (IOException e) {
			System.err.println("ERROR: " + e.getMessage());
			System.exit(-1);
		}

	}

	public static void main(String[] args) {

		final PhidgetSensorNode node = new PhidgetSensorNode("");

		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(rawDataMap);
		}
	}

}
