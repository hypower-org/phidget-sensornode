package edu.hypower.gatech.phidget.comm;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.io.IOException;

//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;

import org.codehaus.jackson.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.*;

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
                    //					System.out.println("Sending new data! " + sensorKey + ": " + newValue);
                    //						String[] sensorKeyParts = sensorKey.split(".");
                    //					HashMap<String,Float> outMap = new HashMap<String,Float>();
                    //					outMap.put(sensorKey, newValue);
                    /*
                     * JSON message format: { "node-ip-addr" : ipAddr, "sensor-type" : sensorKey name,
                     * 							"sensor-location" : int, "data-value" : float}
                     */
                    // Create ObjectMapper object. it is an reusable object
                    ObjectMapper mapper = new ObjectMapper();
                    // Deserialize JSON to Object
                    String jsonString = "{\"node-ip-addr\":ipAddr,\"sensor-type\":sensorKey,\"sensor-location\": Integer.toString(sensorKey.substr(sensorKey.size()-1,1)),\"data-value\": Float.toString(sensorQ.take())}";

                    jsonString = mapper.writeValueAsString(jsonString);
                    objOut.writeObject(outMap);
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
    /*	
        public static void main(String argv[]) throws Exception {
        String sentence;
        String modifiedSentence;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = new Socket("localhost", 6789);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        sentence = inFromUser.readLine();
        outToServer.writeBytes(sentence + '\n');
        modifiedSentence = inFromServer.readLine();
        System.out.println("FROM SERVER: " + modifiedSentence);
        clientSocket.close();
        }
        */	
}
