package Localization;

import Localization.USLocalizer.LocalizationType;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.SampleProvider;

public class USLocalizer {
	public enum LocalizationType { FALLING_EDGE, RISING_EDGE };
	public static double ROTATION_SPEED = 30;

	private Odometer odo;
	private Navigation navi;
	private EV3LargeRegulatedMotor leftMotor, rightMotor;
	private SampleProvider usSensor;
	private float[] usData;
	private LocalizationType locType;
	
	public USLocalizer(Odometer odo, Navigation navi, SampleProvider usSensor, float[] usData, LocalizationType locType) {
		this.odo = odo;
		this.navi = navi;
		this.usSensor = usSensor;
		this.usData = usData;
		this.locType = locType;
	}
	
	public void doLocalization() {
		double [] pos = new double [3];
		double angleA, angleB, deltaAngle, heading;
		
		//get access to motors
		EV3LargeRegulatedMotor[] motors = this.odo.getMotors();
		this.leftMotor = motors[0];
		this.rightMotor = motors[1];
		
		if (locType == LocalizationType.FALLING_EDGE) {
			// rotate the robot until it sees no wall
			leftMotor.forward();
			rightMotor.backward();
		
			while(true){
				if(getFilteredData() > 30.0){
					break;
				}
			}
			
			// keep rotating until the robot sees a wall, then latch the angle
			while(true){
				if(getFilteredData() <= 30.0){
					angleA = odo.getAng();
					leftMotor.stop();
					rightMotor.stop();
					break;
				}
			}
			
			// switch direction and wait until it sees no wall
			leftMotor.backward();
			rightMotor.forward();
		
			while(true){
				if(getFilteredData() > 30.0){
					break;
				}
			}
			
			// keep rotating until the robot sees a wall, then latch the angle
			while(true){
				if(getFilteredData() <= 30.0){
					angleB = odo.getAng();
					leftMotor.stop();
					rightMotor.stop();
					break;
				}
			}
			
			// angleA is clockwise from angleB, so assume the average of the
			// angles to the right of angleB is 45 degrees past 'north'
			deltaAngle = 45 - (angleA+angleB)/2;
			heading = odo.getAng() + deltaAngle;
			
			// possible code to calculate x,y from heading and view distance
			
					
			// update the odometer position (example to follow:)
			odo.setPosition(new double [] {0.0, 0.0, heading}, new boolean [] {true, true, true});
			
			
		} else {
			/*
			 * The robot should turn until it sees the wall, then look for the
			 * "rising edges:" the points where it no longer sees the wall.
			 * This is very similar to the FALLING_EDGE routine, but the robot
			 * will face toward the wall for most of it.
			 */
			
			//
			// FILL THIS IN
			//
		}
	}
	
	private float getFilteredData() {		// filters out distances over 120cm and erroneous readings
		usSensor.fetchSample(usData, 0);
		float distance = usData[0];
		if(distance > 120)
			distance = 120;
				
		return distance;
	}

}
