package edu.hypower.gatech.phidget;

import edu.hypower.gatech.phidget.comm.ChannelNames;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;

/**
 * Class that spawns the DataCollectionVerticle.
 * @author pjmartin
 *
 */
public class DataPublisherBootstrap {
	private static final String address1 = "127.0.0.1.temperature.0";
	private static final String address2 = "127.0.0.1.humidity.1";

	public static void main(String[] args){

		VertxOptions opt = new VertxOptions()
				.setWorkerPoolSize(Runtime.getRuntime().availableProcessors());


		Handler<AsyncResult<Vertx>> resultHandler = new Handler<AsyncResult<Vertx>>(){

			@Override
			public void handle(AsyncResult<Vertx> result) {
				if(result.succeeded()){
					System.out.println("Clustered DataPublisher started.");
					Vertx vertx = result.result();



					Long sharedDataTimerId = vertx.setPeriodic(333, new Handler<Long>(){
						@Override
						public void handle(Long event) {

							LocalMap<String,Long>  map = vertx.sharedData().getLocalMap("SensorData");
							map.put("temp", System.currentTimeMillis());

						}
					});

					Long timerId = vertx.setPeriodic(1000, new Handler<Long>(){
						@Override
						public void handle(Long arg0) {
							JsonObject dataMsg1 = new JsonObject();
							JsonObject dataMsg2 = new JsonObject();

							//dataMsg.put("isLive", true);
							dataMsg1.put("tempTime", vertx.sharedData().getLocalMap("SensorData").get("temp"));
							dataMsg2.put("humdTime", vertx.sharedData().getLocalMap("SensorData").get("temp"));

							//JsonArray data = new JsonArray();
							//for(int i = 0; i < 4; i++){
							//	data.add(Math.random() * i);
							//}
							//dataMsg.put("data", data);
							//vertx.eventBus().publish(ChannelNames.DATA_COLLECTOR_RECV, dataMsg);
							vertx.eventBus().publish(address1, dataMsg1);
							vertx.eventBus().publish(address2, dataMsg2);
						}
					});

				}
				else{
					System.out.println("Failure: " + result.cause().getMessage());
				}
			}

		};
		Vertx.factory.clusteredVertx(opt, resultHandler);
	}
}