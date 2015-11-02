package edu.hypower.gatech.phidget.comm;

import java.io.*;
import java.util.concurrent.*;

public class Producer implements Runnable 
{
    protected BlockingQueue queue = null;

    public Producer(BlockingQueue queue) 
    {
        this.queue = queue;
    }

    public void run() 
    {
        int sum = 0;
        try {
            for(int i = 0; i < 5; i++)
            {
                sum += i;
                queue.put(sum);
                Thread.sleep(3000);
            }     
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
