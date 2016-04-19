package edu.hypower.hsc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

public class KernelVerticle extends AbstractVerticle {

	private Long taskId;

	@Override
	public void start() throws Exception {
		super.start();
		
		MessageConsumer<JsonObject> cons = vertx.eventBus().consumer(ChannelNames.KERNEL_CHAN);
		cons.handler(new Handler<Message<JsonObject>>(){
			@Override
			public void handle(Message<JsonObject> msg) {
				
				if(msg.body().getBoolean("start")){
					taskId = vertx.setPeriodic(500, new Handler<Long>(){
						@Override
						public void handle(Long event) {
							vertx.eventBus()
							.publish(ChannelNames.DATA_CHAN, new JsonObject().put("data", Math.random()));
						}
					});
				}
				else{
					vertx.cancelTimer(taskId);
				}
				
			}
		});
		
	}
	
}
