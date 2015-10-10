package edu.hypower.gatech.phidget;

import java.io.IOException;
import java.io.*;
import java.util.Properties;
import java.util.Set;

public class SensorProps
{
    private final Properties configProp = new Properties();

    private SensorProps()
    {
        //Private constructor to restrict new instances
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("sensor.properties");//load from classpath
        System.out.println("Read all properties from file");
        try {
            configProp.load(in);
            //            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Bill Pugh Solution for singleton pattern
    private static class LazyHolder
    {
        private static final SensorProps INSTANCE = new SensorProps();
    }

    public static SensorProps getInstance()
    {
        return LazyHolder.INSTANCE;
    }

    public String getProperty(String key){
        return configProp.getProperty(key);
    }

    public Set<String> getAllProperties(){
        return configProp.stringPropertyNames();
    }

    public boolean containsKey(String key){
        return configProp.containsKey(key);
    }

    public void setProperty(String key, String val) {
        configProp.setProperty(key,val);
    }

    public void store(){
        File fout = new File("sensor.properties");
        try {
            OutputStream output = new FileOutputStream(fout);
            configProp.store(output,null);
            //            output.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }
}
