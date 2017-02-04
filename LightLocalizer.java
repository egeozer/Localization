package Localization;

import lejos.hardware.Sound;
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
		Sound.setVolume(50);

		
	}
	
	public void doLocalization(Odometer odo, Navigation navi, SampleProvider colorSensor, float[] colorData) {
		this.odo = odo;
		this.navi = navi;
		this.colorSensor = colorSensor;
		this.colorData = colorData;
		EV3LargeRegulatedMotor[] motors = this.odo.getMotors();
		this.leftMotor = motors[0];
		this.rightMotor = motors[1];
		double pointA = 0;
		double pointB;
		odo.setPosition(new double [] {0.0, 0.0, 0.0}, new boolean [] {true, true, true});
		leftMotor.setSpeed(100);

		rightMotor.setSpeed(100);
		
		leftMotor.forward();
		rightMotor.forward();
		
		while(true){
			
			//sampleProvider=sensor.getRedMode();
			colorSensor.fetchSample(colorData, 0);
			if(colorData[0]<0.3){
				Sound.beep();
				navi.goForward(lightSensorDist);
			//	System.out.println("beeep");
				leftMotor.stop();
				rightMotor.stop();
				pointA = odo.getX();
	
				break;

			}
			
		
		}
		
		navi.turnImm(-77);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		navi.goForward(pointA+lightSensorDist);
		leftMotor.setSpeed(100);

		rightMotor.setSpeed(100);
		
		//System.out.println("REACHED");
		navi.turnImm(77);
		odo.setPosition(new double [] {0.0, 0.0, 0.0}, new boolean [] {true, true, true});
	
	}
		
}
		
		
	


