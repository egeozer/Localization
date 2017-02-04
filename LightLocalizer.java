package Localization;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.SampleProvider;

public class LightLocalizer {
	private Odometer odo;
	private Navigation navi;
	private SampleProvider colorSensor;
	private float[] colorData;
	private EV3LargeRegulatedMotor leftMotor, rightMotor;
	double lightSensorDist = 2.0; 		//to be adjusted via testing
	double x, y, xTheta, yTheta;
	double eucDistance, heading;
	int axisCounter;
	
	
	public LightLocalizer(Odometer odo, Navigation navi, SampleProvider colorSensor, float[] colorData) {
		this.odo = odo;
		this.navi = navi;
		this.colorSensor = colorSensor;
		this.colorData = colorData;
	}
	
	public void doLocalization(Odometer odo, Navigation navi, SampleProvider colorSensor, float[] colorData) {
		this.odo = odo;
		this.colorSensor = colorSensor;
		this.colorData = colorData;
		EV3LargeRegulatedMotor[] motors = this.odo.getMotors();
		this.leftMotor = motors[0];
		this.rightMotor = motors[1];
		
		// drive to location listed in tutorial
		while( colorData[0] > 0.3){
		//	if(colorData[0] > 0.3){
				
			//}
			leftMotor.setSpeed(150);
			rightMotor.setSpeed(150);
			leftMotor.forward();
			rightMotor.forward();
		}
		
		navi.goForward(2);
		leftMotor.stop();
		rightMotor.stop();
		
		// start rotating and clock all 4 gridlines
		leftMotor.forward();
		rightMotor.backward();
		
		double angles[] = {0,0,0,0};		//angles{ +x, -y, -x, +y}
				
		while(axisCounter < 5){
			if(colorData[0] > 0.3){
				angles[axisCounter] = odo.getAng();
				axisCounter++;
			}
		}
		
		leftMotor.stop();
		rightMotor.stop();
				
		// do trig to compute (0,0) and 0 degrees
		xTheta = angles[3] - angles[1];
		yTheta = angles[4] - angles[2];
		
		x = -lightSensorDist * Math.cos(yTheta/2);
		y = -lightSensorDist * Math.cos(xTheta/2);
		
		// when done travel to (0,0) and turn to 0 degrees
		
		eucDistance = Math.sqrt( (Math.pow(x, 2) + Math.pow(y,2)) );
		heading = yTheta/2;  //see slide 27
		
		
		navi.goForward(eucDistance);
		
	}

}
