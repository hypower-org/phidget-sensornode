package edu.hypower.gatech.phidget.comm;

import java.io.*;
import java.util.concurrent.*;

public class Producer 
{
    protected ArrayBlockingQueue queue = null;

    public Producer(ArrayBlockingQueue queue) 
    {
        this.queue = queue;
    }

    public void push(String s)
    {
        try {
            queue.put(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } 
    }
}
