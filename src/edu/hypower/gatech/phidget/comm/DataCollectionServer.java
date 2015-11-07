package edu.hypower.gatech.phidget.comm;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataCollectionServer {

	public static final void handleClientConnection(Socket client){
		System.out.println("Received client data...");
		try {
			ObjectInputStream objIn = new ObjectInputStream(client.getInputStream());
			System.out.println(objIn.readObject());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done.");
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
					Runnable r = new Runnable(){
						public void run() {
							handleClientConnection(clientConn);
						}
					};
					exec.execute(r);
					
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
