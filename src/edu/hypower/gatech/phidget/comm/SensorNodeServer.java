package edu.hypower.gatech.phidget.comm;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

/**
 * The server side of the PhidgetSensorNode.
 * @author pjmartin
 *
 */
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
