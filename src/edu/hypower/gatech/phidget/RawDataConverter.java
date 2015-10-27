package edu.hypower.gatech.phidget;

/*
 * This class implements the Phidget raw data formulas from documentation at www.phidgets.com.
 * 
 * @author Patrick J.Martin
 */

public class RawDataConverter {

	/*
	 * Converts raw sensor value into temperature in celsius.
	 */
	public static final float temperatureCelsius(int rawVal) {
		return (float) ((rawVal * 0.22222) - 61.11);
	}

	public static final float toFarenheit(float tempC) {
		return (tempC * 1.8f) + 32f;
	}

	/*
	 * Converts a raw sensor value into relative humidity in %.
	 */
	public static final float relativeHumidity(int rawVal) {
		return (float) ((rawVal * 0.1906) - 40.2);
	}
}
