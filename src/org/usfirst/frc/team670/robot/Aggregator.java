package org.usfirst.frc.team670.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 * Program to consolidate all sensor information - one class to retrieve information from
 * 
 * @author vsharma
 *
 */
public class Aggregator extends Thread{
	
	// Sensors
	private AHRS navXMicro;
	private NetworkTable driverstation, knuckles;
	private double angle = 0;
	private AnalogInput ultrasonic;

	
	//Booleans
	private boolean isNavXConnected, encodersConnected, elevatorEncoders, ultrasonicConnected;
	private boolean sendDataToDS;
	
	public Aggregator(){
		sendDataToDS = true;
		
	    driverstation = NetworkTable.getTable("driverstation");
		knuckles = NetworkTable.getTable("vision");
		
		//Check the navXMicro is plugged in
	    try {
			navXMicro = new AHRS(RobotMap.navXPort);
			isNavXConnected = true;
		} catch (RuntimeException ex) {
			isNavXConnected = false;
			DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
			navXMicro = null;
		}
	    
	    try {
	    	new AnalogInput(RobotMap.ultrasonicAnalogPort);
	    	ultrasonicConnected = true;
	    }catch(RuntimeException ex) {
	    	ultrasonicConnected = false;
	    	DriverStation.reportError("Error instantiating ultrasonic: " + ex.getMessage(), true);
	    	ultrasonic = null;
	    }
	    	    
	    new Thread(new Runnable() {
	        @Override
	        public void run() {
	        	while(true)
	        	{
	        		if(sendDataToDS && System.currentTimeMillis()%50==0)
	        		{
	        			driverstation.putString("operator_state", Robot.oi.getOS().toString());
	        			driverstation.putString("driver_state", Robot.oi.getDS().toString());
			        	driverstation.putDouble("time_left", DriverStation.getInstance().getMatchTime());
			        	driverstation.putString("alliance", DriverStation.getInstance().getAlliance().toString());
			        	driverstation.putDouble("voltage", DriverStation.getInstance().getBatteryVoltage());
			        	driverstation.putBoolean("navX", isNavXConnected);
			        	driverstation.putBoolean("encoders", encodersConnected);
			        	driverstation.putBoolean("isIntakeOpen", Robot.intake.isIntakeOpen());
			        	driverstation.putBoolean("isIntakeDeployed", Robot.intake.isIntakeDeployed());
			        	angle = knuckles.getNumber("angle", 0);
	        		}
	        	}
	        }
	    }).start();
	}
	
	public double getDistanceIntakeInches()
	{
		if(ultrasonic!=null)
			return ultrasonic.getValue()/19.6;
		else
			return 0;
	}
	
	public double getAngle()
	{
		return angle;
	}
	
	public void reset() {
		if (isNavXConnected())
			navXMicro.reset();
	}

	public boolean isNavXConnected() {
		try {
			navXMicro = new AHRS(RobotMap.navXPort);
			isNavXConnected = true;
		} catch (RuntimeException ex) {
			isNavXConnected = false;
			DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
			navXMicro = null;
		}
		return isNavXConnected;
	}

	public double getYaw() {
		if (isNavXConnected())
			return navXMicro.getYaw();
		return -1;
	}
	
	public void zeroYaw()
	{
		if(isNavXConnected())
			navXMicro.zeroYaw();
	}

	public double getTilt() {
		if (isNavXConnected())
			return navXMicro.getAngle();
		return -1;	}

	public double getVelocityY() {
		if (isNavXConnected())
			return navXMicro.getVelocityY();
		return -1;	}

	public double getDisplacementX() {
		if (isNavXConnected())
			return navXMicro.getDisplacementX();
		return -1;
	}
	
	public double getDisplacementY() {
		if (isNavXConnected())
			return navXMicro.getDisplacementY();
		return -1;
	}
	
	public double getDisplacementZ() {
		if (isNavXConnected())
			return navXMicro.getDisplacementZ();
		return -1;
	}
	
	public String toString()
	{
		return "";
	}
	
	public String getVal(String data) {

		int lastLessThan = data.lastIndexOf('<');
		int lastGreaterThan = data.lastIndexOf('>');

		if (lastGreaterThan != -1 && lastLessThan != -1) {
			if (lastLessThan < lastGreaterThan) {
				return data.substring(lastLessThan + 1, lastGreaterThan);
			} else if (data.lastIndexOf('<', lastGreaterThan) != -1) {
				return data.substring(data.lastIndexOf('<', lastGreaterThan) + 1, data.lastIndexOf('>'));
			}
		}
		return "-1";
	}

	public void areEncodersWorking(boolean b) {
		encodersConnected = b;
	}

	public void areElevatorEncodersWorking(boolean b) {
		elevatorEncoders = b;
	}
	
}

