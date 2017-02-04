package Localization;

import lejos.hardware.*;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.*;
import lejos.robotics.SampleProvider;

public class Lab4 {

	// Static Resources:
	// Left motor connected to output A
	// Right motor connected to output D
	// Ultrasonic sensor port connected to input S1
	// Color sensor port connected to input S2
	private static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	private static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
	private static final Port usPort = LocalEV3.get().getPort("S1");		
	private static final Port colorPort = LocalEV3.get().getPort("S2");		

	
	public static void main(String[] args) {
		
		//Setup ultrasonic sensor
		// 1. Create a port object attached to a physical port (done above)
		// 2. Create a sensor instance and attach to port
		// 3. Create a sample provider instance for the above and initialize operating mode
		// 4. Create a buffer for the sensor data
		@SuppressWarnings("resource")							    	// Because we don't bother to close this resource
		SensorModes usSensor = new EV3UltrasonicSensor(usPort);
		SampleProvider usValue = usSensor.getMode("Distance");			// colorValue provides samples from this instance
		float[] usData = new float[usValue.sampleSize()];				// colorData is the buffer in which data are returned
		
		//Setup color sensor
		// 1. Create a port object attached to a physical port (done above)
		// 2. Create a sensor instance and attach to port
		// 3. Create a sample provider instance for the above and initialize operating mode
		// 4. Create a buffer for the sensor data
		SensorModes colorSensor = new EV3ColorSensor(colorPort);
		SampleProvider colorValue = colorSensor.getMode("Red");			// colorValue provides samples from this instance
		float[] colorData = new float[colorValue.sampleSize()];			// colorData is the buffer in which data are returned
				
		// setup the odometer and display
		Odometer odo = new Odometer(leftMotor, rightMotor, 30, true);
		Navigation navi = new Navigation(odo);
		final TextLCD t = LocalEV3.get().getTextLCD();
				
		// start interface
		int buttonChoice;
		do {
			// clear the display
			t.clear();

			// tell the user to press a button to start the program
			t.drawString("<  Left  |  Right >", 0, 0);
			t.drawString("         |         ", 0, 1);
			t.drawString("  Light  |   US    ", 0, 2);
			t.drawString("Localizer|Localizer", 0, 3);
			t.drawString("         |         ", 0, 4);

			buttonChoice = Button.waitForAnyPress();
		} while (buttonChoice != Button.ID_LEFT
				&& buttonChoice != Button.ID_RIGHT);
				
		//initalize display
		LCDInfo lcd = new LCDInfo(odo);
		
		if (buttonChoice == Button.ID_RIGHT) {
			// perform the ultrasonic localization
			USLocalizer usl = new USLocalizer(odo, navi, usValue, usData, USLocalizer.LocalizationType.FALLING_EDGE); 	// locType is either RISING
			usl.doLocalization();																						// or FALLING
							
		} else { 
			// perform the light sensor localization
			LightLocalizer lsl = new LightLocalizer(odo, navi, colorValue, colorData);
			lsl.doLocalization(odo, navi, colorValue, colorData);
			}
												
			while (Button.waitForAnyPress() != Button.ID_ESCAPE);
			System.exit(0);		
	}
}
