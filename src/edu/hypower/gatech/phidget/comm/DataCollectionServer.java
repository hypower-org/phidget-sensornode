package edu.hypower.gatech.phidget.comm;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.EOFException;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataCollectionServer implements Runnable {

	// Blocking queue of 2^8 floats - just because!
	public static final ArrayBlockingQueue<NodeData> dataQ = new ArrayBlockingQueue<NodeData>(256);
//	private BufferedWriter fileWriter;
	private final int port;
	private final ExecutorService exec = Executors.newCachedThreadPool();
	private ServerSocket socket;
	private boolean isStopped = false;

	public DataCollectionServer(int port){
		this.port = port;
		try {
			socket = new ServerSocket(this.port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Runnable dataWriter = new Runnable(){
			@Override
			public void run() {
				while(true){
					try {
						NodeData newNodeData = dataQ.take();
						System.out.println("Incoming data: " + newNodeData.getNodeId() + ", " + newNodeData.getDataType());
						String fileName = newNodeData.getNodeId() + "-" + newNodeData.getDataType() + ".csv";
						BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName, true));			
						fileWriter.write(Float.toString(newNodeData.getNodeDataVal()) + ",");
						fileWriter.flush();
						fileWriter.close();
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		exec.execute(dataWriter);
		exec.execute(this);
		
	}

	private final void handleClientConnection(Socket client) throws IOException {
		System.out.println("Received client data...");
		try {
			ObjectInputStream objIn = new ObjectInputStream(client.getInputStream());
			ObjectMapper mapper = new ObjectMapper();
			String ret = (String) objIn.readObject();
			JsonNode root = mapper.readTree(ret);
			float val = Float.parseFloat(root.get("data-value").asText());
			String ipAddr = root.get("node-ip-addr").asText();
			String type = root.get("sensor-type").asText();
			NodeData nd = new NodeData(ipAddr, type, val);
			dataQ.offer(nd);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
	}

	@Override
	public void run() {
		System.out.println("Data Collection Server started on port " + port);
		while(!isStopped){
			try{
				final Socket clientConn = socket.accept();
				Runnable r = new Runnable(){
					@Override
					public void run() {
						try {
							handleClientConnection(clientConn);
						} catch (IOException e) {
							System.err.println("Uh oh - IO exception on data input: " + e.getCause());
							// TODO: need more graceful shutdown of all these handlers!
//							dataQ.offer(Float.NEGATIVE_INFINITY);
						}
					}
				};

				exec.submit(r);
			} catch (IOException e) {
				System.err.print("SERVER ERROR: client connection error.");
			} 
		}
		try {
			System.out.println("Server stopped.");
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Ugly - make a future instead!
		System.exit(0);
	}

	public static void main(String[] args){

		// Main server logic in main; spawns the data handlers into the executor.
		int port = Integer.parseInt(args[0]);
		DataCollectionServer dcs = new DataCollectionServer(port);
	}
	
	private class NodeData {
		
		private final String nodeId;
		private final String dataType;
		private final Float nodeDataVal;
		
		public NodeData(String id, String type, Float data){
			nodeId = id;
			dataType = type;
			nodeDataVal = data;
		}

		public final String getNodeId() {
			return nodeId;
		}

		public final String getDataType() {
			return dataType;
		}

		public final Float getNodeDataVal() {
			return nodeDataVal;
		}
		
	}

}