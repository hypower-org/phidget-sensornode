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
    public static void main(String[] args) throws IOException
    {
        //  Create a serversocket object and set it to the port number
        //  from the client
        ServerSocket serverSocket = new ServerSocket(1234);

        Socket clientSocket = serverSocket.accept();

        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // Create a string variable that is the data from the client
        String fromClient;
        Integer sum = 0;

        while((fromClient = in.readLine()) != null)
        {
            // Conver the string to integer value
            sum += Integer.valueOf(fromClient);
            System.out.println("Client Message: " + fromClient);
        }
        
        System.out.println("Sum = " + sum);

        in.close();
        clientSocket.close();
        serverSocket.close();
    }
}
