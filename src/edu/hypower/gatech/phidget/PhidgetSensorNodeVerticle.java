package edu.hypower.gatech.phidget;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import edu.hypower.gatech.phidget.comm.SensorNodeClient;
import edu.hypower.gatech.phidget.sensor.SensorReader;
import io.vertx.core.*;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

/**
 * This class is the root of the vertx enabled phidget sensor node application. 
 * 
 * @author pmartin@hsc.edu
 *
 */
public class PhidgetSensorNodeVerticle extends AbstractVerticle {

	//@Override
	//public void start(Future<Void> startFuture) throws Exception {
		// TODO Auto-generated method stub
	//	super.start(startFuture);
//	}

	private final ConcurrentHashMap<String, BlockingQueue<Float>> rawDataMap = new ConcurrentHashMap<String, BlockingQueue<Float>>();
	// Maps sensors to their associated runnable task.
	private final HashMap<String, Runnable> sensorUpdateRunners = new HashMap<String, Runnable>();
	private final HashMap<String, Runnable> sensorClientRunners = new HashMap<String, Runnable>();

	private String nodeIpAddr;
	private boolean isReady = false;

	private final int TIMEOUT = 5000; // 5s wait time

	public PhidgetSensorNodeVerticle(String pathToConfig) {

		try {

			// Read in properties file, which is JSON
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(new File("sensornode.json"));
			nodeIpAddr = rootNode.get("ip-address").asText();
			System.out.println("Sensor node IP Address: " + nodeIpAddr);
			String serverIpAddr = rootNode.get("server-ip-addr").asText();
			Integer serverPort = rootNode.get("server-port").asInt();
			System.out.println("Data Collection server IP Address: " + serverIpAddr + ", port " + serverPort);

			System.out.println("Attaching the Interface Kit Phidget...");
			try {
				// Phidget initialization
				InterfaceKitPhidget ikit = new InterfaceKitPhidget();
				ikit.openAny();
				ikit.waitForAttachment(TIMEOUT);


				System.out.println("complete.");
				ikit.setRatiometric(true);


				ScheduledExecutorService schedExec = Executors
						.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
				ExecutorService exec = Executors.newCachedThreadPool();

				Iterator<Map.Entry<String, JsonNode>> iter = rootNode.get("sensors").fields();
				while (iter.hasNext()) {
					Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) iter.next();
					Integer location = entry.getValue().get("location").asInt();
					Integer updatePeriod = entry.getValue().get("update-period").asInt();
					String sensorStr = entry.getKey();
					String sensorKey = sensorStr + "." + location;

					String sensorName = Character.toUpperCase(sensorStr.charAt(0)) + sensorStr.substring(1, sensorStr.length());

					try {
						Class<?> sensor = Class
								.forName("edu.hypower.gatech.phidget.sensor." + sensorName + "SensorReader");
						Constructor<?> cons = sensor.getConstructor(Integer.class, String.class, InterfaceKitPhidget.class, ArrayBlockingQueue.class);

						System.out.println(sensorKey + ", updating every " + updatePeriod + "ms");

						rawDataMap.putIfAbsent(sensorKey, new ArrayBlockingQueue<Float>(1));
						sensorUpdateRunners.put(sensorKey, (SensorReader) cons.newInstance(location, sensorKey, ikit,
								(ArrayBlockingQueue<Float>) rawDataMap.get(sensorKey)));


						SensorNodeClient client = new SensorNodeClient((ArrayBlockingQueue<Float>) rawDataMap.get(sensorKey),
								nodeIpAddr, sensorKey, serverIpAddr, serverPort);

						sensorClientRunners.put(sensorKey, client);

						vertx.setPeriodic(updatePeriod, v -> vertx.eventBus().publish(sensorUpdateRunners.get(sensorKey).toString(), sensorClientRunners.get(sensorKey)));

						//schedExec.scheduleAtFixedRate(sensorUpdateRunners.get(sensorKey), 0, updatePeriod, TimeUnit.MILLISECONDS);
						exec.submit(sensorClientRunners.get(sensorKey));

						isReady = true;

					} catch (ClassNotFoundException cnfe){
						System.err.println("ERROR: " + sensorName + " not implemented.");

					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
				}
			} catch (PhidgetException e) {
				System.err.println("Error: Timeout ( " + TIMEOUT + "ms) reached. No phidget Interface Kit detected.");
			}


		} catch (JsonProcessingException e) {
			System.err.println("JSON ERROR: " + e.getMessage());
			System.exit(-1);
		} catch (IOException e) {
			System.err.println("ERROR: " + e.getMessage());
			System.exit(-1);
		}

	}

	public final ArrayList<String> getSensorNames(){
		final ArrayList<String> sensors = new ArrayList<String>();
		for(String s : rawDataMap.keySet()){
			sensors.add(s);
		}
		return sensors;
	}

	public boolean isReady() {
		return isReady;
	}

	public static void main(String[] args) {

		final PhidgetSensorNodeVerticle node = new PhidgetSensorNodeVerticle("");
		if(node.isReady()){
			while (true) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
