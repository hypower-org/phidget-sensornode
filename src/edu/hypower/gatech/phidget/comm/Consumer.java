package edu.hypower.gatech.phidget.comm;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Consumer implements Runnable
{
    protected ArrayBlockingQueue queue = null;
    public Consumer(ArrayBlockingQueue queue) 
    {
        this.queue = queue;
    }

    public void run() 
    {
        try {
           System.out.println(queue.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

