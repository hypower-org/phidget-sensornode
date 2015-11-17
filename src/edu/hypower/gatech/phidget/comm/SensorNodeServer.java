package edu.hypower.gatech.phidget.comm;

import java.io.*;
import java.net.*;
<<<<<<< HEAD
import java.util.concurrent.*;
=======
>>>>>>> 7c696975d95b44ad7c9b3ceace618e97ce577124

/**
 * The server side of the PhidgetSensorNode.
 * 
 * @author pjmartin
 *
 */
<<<<<<< HEAD
public class SensorNodeServer 
{
    public static void main(String[] args) throws Exception
    {
        //  Create a serversocket object and set it to the port number
        //  from the client
        ServerSocket serverSocket = new ServerSocket(1234);

        Socket clientSocket = serverSocket.accept();

        BufferedReader i = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        ArrayBlockingQueue queue = new ArrayBlockingQueue(1024);

        Producer producer = new Producer(queue);

        Consumer consumer = new Consumer(queue);
=======
public class SensorNodeServer {
	public static void main(String argv[]) throws Exception {
		String clientSentence;
		String capitalizedSentence;
		ServerSocket welcomeSocket = new ServerSocket(6789);

		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			clientSentence = inFromClient.readLine();
			System.out.println("Received: " + clientSentence);
			capitalizedSentence = clientSentence.toUpperCase() + '\n';
			outToClient.writeBytes(capitalizedSentence);
		}
	}
>>>>>>> 7c696975d95b44ad7c9b3ceace618e97ce577124

        // Create a string variable that is the data from the client
        String fromClient;
        Thread.sleep(10000);
        while((fromClient = i.readLine()) != null)
        {
            new Thread(consumer).start();
            producer.push(fromClient);         
        }
        
        i.close();
        clientSocket.close();
        serverSocket.close();
    }
}
