package edu.hypower.gatech.phidget.comm;

import java.io.*;
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;

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
	private final Long serverPort;
	private final String ipAddr;
	
	public SensorNodeClient(ArrayBlockingQueue<Float> sensorQ, String ipAddr, String sensorKey, String serverIpAddr, Long serverPort){
		this.sensorQ = sensorQ;
		this.sensorKey = sensorKey;
		this.serverIpAddr = serverIpAddr;
		this.serverPort = serverPort;
		this.ipAddr = ipAddr;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				Float newValue = sensorQ.take();
				sendData(newValue);
			} catch (InterruptedException e) {
				System.err.println("ERROR: SensorNodeClient interrupted.");
			}
		}
	}
	
	/**
	 * Takes a floating point data value and pacakges it into a JSON message for the
	 * DataCollectionServer.
	 * @param dataValue
	 */
	private final void sendData(Float dataValue){
		System.out.println("Sending new data! " + sensorKey + ": " + dataValue);
		
		String[] sensorKeyParts = sensorKey.split(".");
		/*
		 * JSON message format: { "node-ip-addr" : ipAddr, "sensor-type" : sensorKey name,
		 * 							"sensor-location" : int, "data-value" : float}
		 */
	}
	
//	public static void main(String argv[]) throws Exception {
//		String sentence;
//		String modifiedSentence;
//		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
//		Socket clientSocket = new Socket("localhost", 6789);
//		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
//		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//		sentence = inFromUser.readLine();
//		outToServer.writeBytes(sentence + '\n');
//		modifiedSentence = inFromServer.readLine();
//		System.out.println("FROM SERVER: " + modifiedSentence);
//		clientSocket.close();
//	}
	
}
