package edu.hypower.gatech.phidget;

import java.io.FileInputStream;
import java.util.Properties;

import edu.hypower.gatech.phidget.*;
/**
 * This class implements the startup process for a phidget-sensornode: 1) configuration file
 * interpretation, 2) network setup (and discovery), 3) transmission (and receiving) of data.
 * 
 * @author pjmartin
 *
 */
public class PhidgetSensorNode {

    public static void main(String[] args){
        SensorProps prop = SensorProps.getInstance();

        // Load the properties file into the SensorProps data structure. 
        System.out.println(prop.getAllProperties());
        //Set new property in
        if(!prop.containsKey("rate")) prop.setProperty("rate","100");
        //Verify new property
        System.out.println(prop.getAllProperties());
        prop.store();
    }

}
