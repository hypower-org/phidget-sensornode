package edu.hypower.gatech.phidget.comm;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

/**
 * The client side of the PhidgetSensorNode.
 * @author pjmartin
 *
 */
public class SensorNodeClient 
{
    public static void main(String[] args) throws Exception
    {
        // Loopback address: "127.0.0.1" need the ip address to connect
        // with server, the port number is 1234
        Socket socket = new Socket("127.0.0.1", 1234);
        
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Create a Blocking Queue array
        BlockingQueue queue = new ArrayBlockingQueue(1024);

        // Create a new queue for the data (producer class)
        Producer producer = new Producer(queue);

        // Start the thread for receiving data from Producer queue
        new Thread(producer).start();
        
        out.println(queue.take());
        out.println(queue.take());
        out.println(queue.take());

        out.close();
        socket.close();

    }
}
