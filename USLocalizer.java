package Localization;

import Localization.USLocalizer.LocalizationType;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.SampleProvider;

public class USLocalizer {
	public enum LocalizationType { FALLING_EDGE, RISING_EDGE };
	public static int ROTATION_SPEED = 100;

	private Odometer odo;
	private Navigation navi;
	private EV3LargeRegulatedMotor leftMotor, rightMotor;
	private SampleProvider usSensor;
	private float[] usData;
	private LocalizationType locType;
	private double critical = 35;
	
	public USLocalizer(Odometer odo, Navigation navi, SampleProvider usSensor, float[] usData, LocalizationType locType) {
		this.odo = odo;
		this.navi = navi;
		this.usSensor = usSensor;
		this.usData = usData;
		this.locType = locType;
	}
	
	public void doLocalization() {
		//ROTATION_SPEED = 50;
		EV3LargeRegulatedMotor[] motors = this.odo.getMotors();
		this.leftMotor = motors[0];
		this.rightMotor = motors[1];
		double [] pos = new double [3];
		double angleA, angleB, deltaAngle, heading;
		leftMotor.setSpeed(ROTATION_SPEED);

		rightMotor.setSpeed(ROTATION_SPEED);
		
		//get access to motors
		
		
		
		if (locType == LocalizationType.FALLING_EDGE) {
			// rotate the robot until it sees no wall
		
		
		
		
		leftMotor.forward();
		rightMotor.backward();
	
		while(true){
			if(getFilteredData() > 45.0){
				break;
			}
		}
		
		// keep rotating until the robot sees a wall, then latch the angle
		while(true){
			if(getFilteredData() <= 45.0){
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
			if(getFilteredData() > 45.0){
				break;
			}
		}
		
		// keep rotating until the robot sees a wall, then latch the angle
		while(true){
			if(getFilteredData() <= 45.0){
				angleB = odo.getAng();
				leftMotor.stop();
				rightMotor.stop();
				break;
			}
		}
			if (angleA > angleB){

				heading = 225 - (angleA + angleB)/2;

			}
		
			else{

				heading =  45 - (angleA + angleB)/2;

			}
			
			
			// possible code to calculate x,y from heading and view distance
			
					
			// update the odometer position (example to follow:)
			odo.setPosition(new double [] {0.0, 0.0, heading+angleB}, new boolean [] {true, true, true});
			navi.turnTo(0, true);
			System.out.println("FINISHEEEED");
		}
		 else {
			System.out.println( getFilteredData());
			/*
			 * The robot should turn until it sees the wall, then look for the
			 * "rising edges:" the points where it no longer sees the wall.
			 * This is very similar to the FALLING_EDGE routine, but the robot
			 * will face toward the wall for most of it.
			 */
			
			// rotate the robot until it sees the wall
			//leftMotor.forward();
		//	rightMotor.backward();
		
			
			// keep rotating until the robot sees no wall, then latch the angle
			leftMotor.forward();
			rightMotor.backward();
		
			while(true){
				if(getFilteredData() < 45.0){
					break;
				}
			}
			
			// keep rotating until the robot sees no wall, then latch the angle
			while(true){
				if(getFilteredData() >= 45.0){
					angleA = odo.getAng();
					leftMotor.stop();
					rightMotor.stop();
					break;
				}
				
}
			
			// switch direction and wait until it sees the wall
			leftMotor.backward();
			rightMotor.forward();
		
			while(true){
				if(getFilteredData() < 45.0){
					break;
				}
			}
			
			// keep rotating until the robot sees no wall, then latch the angle
			while(true){
				if(getFilteredData() >= 45.0){
					angleB = odo.getAng();
					leftMotor.stop();
					rightMotor.stop();
					break;
				}
			}

			// angleA is clockwise from angleB, so assume the average of the
			// angles to the right of angleB is 45 degrees past 'north'
			if(angleA > angleB){

				heading = 225 - (angleA + angleB)/2;

			}

			else{

				heading =  45 - (angleA + angleB)/2;

			}
			//heading = odo.getAng() + deltaAngle;
			
			// possible code to calculate x,y from heading and view distance
			
					
			// update the odometer position (example to follow:)
			odo.setPosition(new double [] {0.0, 0.0, heading+angleA+2}, new boolean [] {true, true, true});
			navi.turnTo(0, true);
			System.out.println("FINISHEEEED");
		}
	}
	
	private float getFilteredData() {		// filters out distances over 120cm and erroneous readings
		usSensor.fetchSample(usData, 0);
		float distance =100* usData[0];
		//if(distance > 120)
			//distance = 120;
				
		return distance;
	}
	void turnTo(double theta){
		//set rotational speed
		
		//leftMotor.setSpeed((int)ROTATION_SPEED);
		//rightMotor.setSpeed((int)ROTATION_SPEED);
		

		leftMotor.rotate(convertAngle(odo.getLeftRadius(), odo.getWidth(), theta), true);
		rightMotor.rotate(-convertAngle(odo.getLeftRadius(), odo.getWidth(), theta), false);
		//while(true){
			//if(!leftMotor.isMoving()&& !rightMotor.isMoving()){
				//break;
		//	}
			
		//}
		//leftMotor.endSynchronization();
	}
	//from lab2
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);

	}

	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));

	}

}
