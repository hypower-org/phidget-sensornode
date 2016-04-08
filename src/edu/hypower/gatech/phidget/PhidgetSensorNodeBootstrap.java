package edu.hypower.gatech.phidget;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;

public class PhidgetSensorNodeBootstrap {
	
	public static void main(String[] args){
		
		VertxOptions opts = new VertxOptions()
				.setWorkerPoolSize(Runtime.getRuntime().availableProcessors())
				.setClusterHost("10.0.0.100");
		// TODO: add config for clustering host/port, etc.
		
		Handler<AsyncResult<Vertx>> resultHandler = new Handler<AsyncResult<Vertx>>(){
			@Override
			public void handle(AsyncResult<Vertx> asyncRes) {
				if(asyncRes.succeeded()){
					System.out.println("Clustered vertx launched.");
					Vertx vertx = asyncRes.result();
					
					JsonObject config = vertx.fileSystem().readFileBlocking("sensornode.json").toJsonObject();
					
					String ip = config.getString("ip-address");
					
					vertx.deployVerticle(new PhidgetSensorNodeVerticle(config));

// Local testing!
//					vertx.eventBus().consumer("127.0.0.1.temperature.0", 
//							msg -> { System.out.println(msg.body()); });
//					
//					vertx.eventBus().consumer("127.0.0.1.humidity.1",
//							msg -> { System.out.println(msg.body()); });
				}
			}
		};
		Vertx.clusteredVertx(opts, resultHandler);
	}

}
