package edu.hypower.gatech.phidget;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * Class that spawns the DataCollectionVerticle.
 * @author pjmartin
 *
 */
public class DataCollectorBootstrap {

	public static void main(String[] args){
		
		VertxOptions opt = new VertxOptions()
							.setWorkerPoolSize(Runtime.getRuntime().availableProcessors());
		
		Handler<AsyncResult<Vertx>> resultHandler = new Handler<AsyncResult<Vertx>>(){

			@Override
			public void handle(AsyncResult<Vertx> result) {
				if(result.succeeded()){
					System.out.println("Clustered DataCollector started.");
					Vertx vertx = result.result();
					vertx.deployVerticle(new DataCollectionVerticle());
				}
				else{
					System.out.println("Failure: " + result.cause().getMessage());
				}
			}
			
		};
		
		Vertx.factory.clusteredVertx(opt, resultHandler);
		
	}
	
	
}
