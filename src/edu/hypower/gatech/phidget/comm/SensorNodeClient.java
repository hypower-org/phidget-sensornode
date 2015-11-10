package edu.hypower.gatech.phidget.comm;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.*;
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
        
        PrintWriter o = new PrintWriter(socket.getOutputStream(), true);
        
        // Jackson Library to pass strings
        for(Integer i = 0; i < 10; i++)
        {
            o.println(i.toString());
            Thread.sleep(899);
        }

        o.close();
        socket.close();
    }
}
