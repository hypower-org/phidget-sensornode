package edu.hypower.gatech.phidget.comm;

import com.sun.corba.se.impl.orbutil.graph.NodeData;
import edu.hypower.gatech.phidget.DataCollectionVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ArrayBlockingQueue;

import static edu.hypower.gatech.phidget.comm.DataCollectionServer.dataQ;

/**
 * Created by parallella on 4/8/16.
 */
public class DataCollectionServerBootstrap {

    private static final String address1 = "127.0.0.1.temperature.0";
    private static final String address2 = "127.0.0.1.humidity.1";

    public static void main(String[] args) {
        VertxOptions opt = new VertxOptions().setWorkerPoolSize(Runtime.getRuntime().availableProcessors())
                .setClusterHost("10.0.0.2");


        Handler<AsyncResult<Vertx>> resultHandler = new Handler<AsyncResult<Vertx>>() {

            @Override
            public void handle(AsyncResult<Vertx> result) {
                if (result.succeeded()) {
                    System.out.println("Clustered DataCollector started.");
                    Vertx vertx = result.result();
                   // vertx.deployVerticle(address1);

                    vertx.eventBus().consumer(address1, message -> {
                        String mess = message.body().toString();
                        System.out.println("Received data " + mess);
                     });

                    vertx.eventBus().consumer(address2, message -> {
                        String mess = message.body().toString();
                        System.out.println("Received data " + mess);
                    });

                } else {
                    System.out.println("Failure: " + result.cause().getMessage());
                }
            }
        };
        Vertx.factory.clusteredVertx(opt, resultHandler);

    }



}
