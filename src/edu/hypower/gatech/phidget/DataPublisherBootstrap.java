package edu.hypower.gatech.phidget;

import edu.hypower.gatech.phidget.comm.ChannelNames;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Class that spawns the DataCollectionVerticle.
 * @author pjmartin
 *
 */
public class DataPublisherBootstrap {

	public static void main(String[] args){
		
		VertxOptions opt = new VertxOptions()
							.setWorkerPoolSize(Runtime.getRuntime().availableProcessors());
		
		Handler<AsyncResult<Vertx>> resultHandler = new Handler<AsyncResult<Vertx>>(){

			@Override
			public void handle(AsyncResult<Vertx> result) {
				if(result.succeeded()){
					System.out.println("Clustered DataPublisher started.");
					Vertx vertx = result.result();
					
					Long timerId = vertx.setPeriodic(1000, new Handler<Long>(){
						@Override
						public void handle(Long arg0) {
							JsonObject dataMsg = new JsonObject();
							dataMsg.put("isLive", true);
							JsonArray data = new JsonArray();
							for(int i = 0; i < 10; i++){
								data.add(Math.random() * i);
							}
							dataMsg.put("data", data);
							vertx.eventBus().publish(ChannelNames.DATA_COLLECTOR_RECV, dataMsg);
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
