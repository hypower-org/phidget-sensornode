package edu.hypower.gatech.phidget.comm;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The client side of the PhidgetSensorNode.
 * 
 * @author pjmartin
 *
 */
public class SensorNodeClient implements Runnable {

    private final ArrayBlockingQueue<Float> sensorQ;
    private final String sensorKey;
    private final String serverIpAddr;
    private final Integer serverPort;
    private final String ipAddr;

    public SensorNodeClient(ArrayBlockingQueue<Float> sensorQ, String ipAddr, String sensorKey, String serverIpAddr, Integer serverPort){
        this.sensorQ = sensorQ;
        this.ipAddr = ipAddr;
        this.sensorKey = sensorKey;
        this.serverIpAddr = serverIpAddr;
        this.serverPort = serverPort;
    }
    
    @Override
    public void run() {

        Socket socket;
        while(true){
            try {
                try {
                    socket = new Socket(serverIpAddr, serverPort);
                    OutputStream os = socket.getOutputStream();
                    ObjectOutputStream objOut = new ObjectOutputStream(os);

                    Float newValue = sensorQ.take();
                    /*
                     * JSON message format: { "node-ip-addr" : ipAddr, "sensor-type" : sensorKey name,
                     * 							"sensor-location" : int, "data-value" : float}
                     */
                    // Create ObjectMapper object. it is an reusable object
                    ObjectMapper mapper = new ObjectMapper();
                    // TODO: We should be using the Jackson library to build this packet up!
                    String jsonString = 
                    		"{\"node-ip-addr\":" + "\"" + ipAddr + "\""   
		                    + ",\"sensor-type\":" + "\""  + sensorKey.substring(0,sensorKey.length() - 2) + "\""
		                    + ",\"sensor-location\":" + sensorKey.substring(sensorKey.length() - 1,sensorKey.length()) + ",\"data-value\":" +  "\""  + Float.toString(newValue)+  "\"" + "}";

                    objOut.writeObject(jsonString);
                    objOut.flush();

                } catch (InterruptedException e) {
                	System.err.println("ERROR: SensorNodeClient interrupted.");
                }

            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            }
        }
    }
}
