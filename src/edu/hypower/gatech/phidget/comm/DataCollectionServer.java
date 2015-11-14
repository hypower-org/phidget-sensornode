package edu.hypower.gatech.phidget.comm;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.io.FileWriter;

public class DataCollectionServer {

	public static final ObjectInputStream handleClientConnection(Socket client){
		System.out.println("Received client data...");
		try {
			ObjectInputStream objIn = new ObjectInputStream(client.getInputStream());
			return objIn;
//			System.out.println(objIn.readObject());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Done.");
		return null;
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
			while(!isStopped){
				try{
					final Socket clientConn = socket.accept();
					Callable r = new Callable(){
						public ObjectInputStream call() {
							return handleClientConnection(clientConn);
						}
					};
					Future<ObjectInputStream> future = exec.submit(r);
					FileWriter writer = new FileWriter("test.csv");
					try {
//						writer.append(future.get().readObject().toString());
//						writer.flush();
						writer.write(future.get().readObject().toString());
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally{
						writer.close();
					}
				//	System.out.println(future.get().readObject());
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
