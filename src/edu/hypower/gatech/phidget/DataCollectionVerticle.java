package edu.hypower.gatech.phidget;

import edu.hypower.gatech.phidget.comm.ChannelNames;
import io.vertx.core.*;

public class DataCollectionVerticle extends AbstractVerticle {

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		super.start(startFuture);
		
		vertx.eventBus().consumer(ChannelNames.DATA_COLLECTOR_RECV, message -> {
			System.out.println("Received data " + message.body());
		});
		
		System.out.println(this.getClass().getName() + " started.");
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		System.out.println(this.getClass().getName() + " stopped.");
	}

}
