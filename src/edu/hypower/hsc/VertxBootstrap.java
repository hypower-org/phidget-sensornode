package edu.hypower.hsc;

import java.util.ArrayList;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

public class VertxBootstrap {

	public static void main(String[] args){
		
		String clusterIp = new String();
		if(args.length == 1){
			clusterIp = args[0];
		}

		VertxOptions opts = new VertxOptions();
		opts.setWorkerPoolSize(Runtime.getRuntime().availableProcessors());
		if(!clusterIp.isEmpty()){
			opts.setClusterHost(clusterIp);
		}
		
		final ArrayList<String> deployIds = new ArrayList<String>();
		Handler<AsyncResult<Vertx>> startupHandler = new Handler<AsyncResult<Vertx>>(){
			@Override
			public void handle(AsyncResult<Vertx> r) {
				Vertx vertx = r.result();
				final String id;
				vertx.deployVerticle(new KernelVerticle(), 
						res -> { deployIds.add(res.result()); });

				vertx.setTimer(2000, new Handler<Long>(){
					@Override
					public void handle(Long event) {
						vertx.eventBus().publish(ChannelNames.KERNEL_CHAN, new JsonObject().put("start", true));
					}
					
				});
				
				vertx.setTimer(6000, new Handler<Long>(){
					@Override
					public void handle(Long event) {
						vertx.eventBus().publish(ChannelNames.KERNEL_CHAN, new JsonObject().put("start", false));
					}
				});
				
				MessageConsumer<JsonObject> cons = vertx.eventBus().consumer(ChannelNames.DATA_CHAN);
				cons.handler(new Handler<Message<JsonObject>>(){

					@Override
					public void handle(Message<JsonObject> msg) {
						System.out.println(msg.body());
					}
					
				});
				
				
			}
		};
		Vertx.factory.clusteredVertx(opts, startupHandler);
		
	}
	
}
