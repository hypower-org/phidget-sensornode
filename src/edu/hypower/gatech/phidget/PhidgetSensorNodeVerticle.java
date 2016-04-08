package edu.hypower.gatech.phidget;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;


/**
 * This class is the root of the vertx enabled phidget sensor node application. 
 * 
 * @author pmartin@hsc.edu
 *
 */
public class PhidgetSensorNodeVerticle extends AbstractVerticle {

	private final int TIMEOUT = 5000; // 5s wait time
	private final String SENSOR_DATA_MAP = "sensorDataMap";

	private final JsonObject sensorConfig;
	
	public PhidgetSensorNodeVerticle(JsonObject config){
		sensorConfig = config;
	}
	
	@Override
	public void start(Future<Void> startFuture) throws Exception {
		super.start(startFuture);
		System.out.println("Launched the Phidget Sensor Node!");
		
		InterfaceKitPhidget ikit = new InterfaceKitPhidget();
		ikit.openAny();
		ikit.waitForAttachment(TIMEOUT);
		System.out.println("complete.");
		ikit.setRatiometric(true);

		SharedData sd = vertx.sharedData();
		LocalMap<String,Float> dataMap = sd.getLocalMap(SENSOR_DATA_MAP);
		
		JsonObject sensorObj = sensorConfig.getJsonObject("sensors");
		
		// Load sensor data into maps on a 100 ms timer.
		vertx.setPeriodic(100, new Handler<Long>(){
			@Override
			public void handle(Long event) {
				for(String sensorName : sensorObj.fieldNames()){
					try{
						// TODO: Ugly! But just for testing vertx and phidgets!
						Integer location = sensorObj.getJsonObject(sensorName).getInteger("location");
						if(sensorName.compareTo("temperature") == 0){
							Float data = (float) ((ikit.getSensorValue(location) * 0.22222) - 61.11);
							dataMap.put(sensorName, data);
						}
						else if(sensorName.compareTo("humidity") == 0){
							Float data = (float) (( ikit.getSensorValue(location) * 0.1906 ) - 40.2);;
							dataMap.put(sensorName, data);
						}
					}catch(PhidgetException e){
						System.err.println("Error reading " + sensorName);
					}
				}
			}
		});
		
		// Send out data at required timing from config file.
		for(String sensorName : sensorObj.fieldNames()){
			Long updatePeriod = sensorObj.getJsonObject(sensorName).getLong("update-period");
			Integer location = sensorObj.getJsonObject(sensorName).getInteger("location");
			
			String sensorTopic = sensorConfig.getString("ip-address") + "." + sensorName + "." + location;
			System.out.println("Publishing to " + sensorTopic + " every " + updatePeriod + " ms");
			
			vertx.setPeriodic(updatePeriod, new Handler<Long>(){
				@Override
				public void handle(Long event) {
					Float data = dataMap.get(sensorName);
					vertx.eventBus().publish(sensorTopic, new JsonObject().put(sensorName, data));
				}
			});
		}
		

	}

}
