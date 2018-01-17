package org.usfirst.frc.team670.robot.commands.components;

import org.usfirst.frc.team670.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class NavX_Pivot extends Command {

	private double finalAngle, startAngle, angle;
	private double percentComplete;
	private int numTimesIsFinished;

	/**
	 * 
	 * 
	 * @param angle
	 *            The angle to turn, positive is right, negative is left.
	 */
	public NavX_Pivot(double angle) {
		this.angle = angle;
		numTimesIsFinished = 0;
		requires(Robot.driveBase);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		this.startAngle = getYaw();
		this.finalAngle = startAngle + angle;
		this.numTimesIsFinished = 0;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double speed = 0;
		percentComplete = Math.abs((angle - yawRemaining()) / (angle));

		if (percentComplete <= 0.363) {
			speed = -2.3 * 2.3 * (percentComplete * percentComplete) + 0.8;
		} else {
			speed = 0.1;
		} 
		if (percentComplete > 1.0){
			speed = -speed;
		}
		System.out.println("YawRemaining: " + yawRemaining());
//		if(checkOverRotation()){
//			speed = -speed; //Changing speed to reverse if it is over
//		}
		
		if (angle > 0){
			Robot.driveBase.drive(speed, -speed);
		}
		else{
			Robot.driveBase.drive(-speed, speed);
		}

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		if (isInThreshold()) {
			numTimesIsFinished++;
//			if (numTimesIsFinished > 4) {
				System.out.println("Finished");
				return true;
//			}
		} else if (numTimesIsFinished > 0) {
			numTimesIsFinished = 0;
		}
		return false;
	}

	private boolean isInThreshold(){
//		System.out.println(Math.abs(finalAngle - getYaw()));
		return (Math.abs(finalAngle - getYaw()) <= 0.5);
	}
	
	// Called once after isFinished returns true
	protected void end() {
		Robot.driveBase.drive(0, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		Robot.driveBase.drive(0, 0);
	}

	private double getYaw() {
		return Robot.sensors.getYaw() + 180;
	}

	private double yawRemaining() {
		double yaw = getYaw();
		if (angle > 0 && yaw < (startAngle - 10/* Margin of Error Value */)) { // IF
																				// WE
																				// WANT
																				// TO
																				// TURN
																				// 360
																				// THIS
																				// WON't
																				// WORK
			return finalAngle - yaw - 360;
		} else if (angle < 0
				&& yaw > (startAngle + 10/* Margin of Error Value */)) {
			return finalAngle - yaw + 360;
		}
		return finalAngle - yaw;
	}

}