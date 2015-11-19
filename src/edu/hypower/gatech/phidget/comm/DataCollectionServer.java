package edu.hypower.gatech.phidget.comm;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.io.FileWriter;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.*;
import org.codehaus.jackson.map.*;

public class DataCollectionServer {
	public static final ArrayBlockingQueue<Float> dataQ = new ArrayBlockingQueue<Float>(48);
	
	public static final String handleClientConnection(Socket client){
		System.out.println("Received client data...");
		try {
			ObjectInputStream objIn = new ObjectInputStream(client.getInputStream());
     ObjectMapper mapper = new ObjectMapper();
     String ret = (String) objIn.readObject();
      JsonNode root = mapper.readTree(ret);
      float val = Float.parseFloat(root.get("data-value").asText());
//			System.out.println(val);
//			HashMap<String, Float> dataMap = (HashMap<String, Float>) objIn.readObject();
//			for(String key: dataMap.keySet()){
//				System.out.println(key);
				dataQ.offer(val);
				return ret;
		//}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done.");
		return null ;
	}
	
	public static void main(String[] args){
	
		// Main server logic in main; spawns the data handlers into the executor.
		int port = Integer.parseInt(args[0]);
		ServerSocket socket;
		ExecutorService exec = Executors.newCachedThreadPool();
		
		boolean isStopped = false;
		try {
			socket = new ServerSocket(port);
			System.out.println("Data Collection Server started on port " + port);
			FileWriter writer = null;
			while(!isStopped){
				try{
					final Socket clientConn = socket.accept();
					Callable r = new Callable(){
						public String call() {
							String ret = handleClientConnection(clientConn);
//							System.out.println(ret + " queue size:" + dataQ.size());
							return ret;
						}
					};
					
					Runnable fileWriter = new Runnable() {
						public void run() {
							BufferedWriter writer = null;
							try {
								writer = new BufferedWriter(new FileWriter("test.csv", true));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								writer.write(Float.toString(dataQ.take())+",");
							} catch (IOException | InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								writer.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					exec.submit(r);
					exec.execute(fileWriter);
				} catch (IOException e) {
					System.err.print("SERVER ERROR: client connection error.");
				}
			}
			socket.close();
			
		} catch (IOException e) {
			System.err.print("SERVER ERROR: Failed to bind to port " + port);
			System.exit(-1);
		}
		
		
	}
	
}
