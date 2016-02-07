package edu.hypower.gatech.phidget;

import io.vertx.core.*;

public class DataCollectionVerticle extends AbstractVerticle {

	private final static String RECV = "dcv.recv";
	
	@Override
	public void start(Future<Void> startFuture) throws Exception {
		super.start(startFuture);
		System.out.println(this.getClass().getName() + " started.");
	}

}
