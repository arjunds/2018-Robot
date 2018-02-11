package org.usfirst.frc.team670.robot.commands.actions.components;

import org.usfirst.frc.team670.robot.Robot;
import org.usfirst.frc.team670.robot.utilities.Constants;
import org.usfirst.frc.team670.robot.utilities.ElevatorState;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
/**
 * 
 * Uses a PID control loop plus the navX getDisplacement to move a given distance in feet
 * 
 * @author vsharma8363
 *
 */
public class Encoders_Elevator extends Command{
	
	private double ticksToTravel, minPercentOutput = 0.05, targetPulseHeight;
	private int numTimesMotorOutput;
	
	public Encoders_Elevator(ElevatorState state) {
		requires(Robot.elevator);
		if(state == ElevatorState.EXCHANGE)
			targetPulseHeight = Constants.elevatorPulseForExchange;
		else if(state.equals(ElevatorState.SWITCH))
			targetPulseHeight = Constants.elevatorPulseForSwitch;
		else if(state.equals(ElevatorState.SCALE))
			targetPulseHeight = Constants.elevatorPulseForScale;
		else if(state.equals(ElevatorState.SCALE))
			targetPulseHeight = Constants.elevatorPulseForDown;
		else
			targetPulseHeight = Constants.elevatorPulseForExchange;
		
 		//this.ticksToTravel = ((inches)/(Math.PI*Constants.DIAMETERinInchesDriveBase)) * Constants.drivebaseTickPerRotation;
		
		targetPulseHeight = targetPulseHeight - Robot.elevator.getCurrentPosition();
	}
	
	// Called just before this Command runs the first time
		protected void initialize() {
			Robot.elevator.initPID(Robot.elevator.getTalon());
		}

		// Called repeatedly when this Command is scheduled to run
		protected void execute() {
			Robot.elevator.getTalon().set(ControlMode.Position, ticksToTravel); 
		}

		// Make this return true when this Command no longer needs to run execute()
		protected boolean isFinished() 
		{
			if (Math.abs(Robot.elevator.getTalon().getMotorOutputPercent()) <= minPercentOutput)
				numTimesMotorOutput++;

			return (numTimesMotorOutput >= 100);
		}

		// Called once after isFinished returns true
		protected void end() {
			Robot.elevator.moveElevator(0);
		}

		// Called when another command which requires one or more of the same
		// subsystems is scheduled to run
		protected void interrupted() {
			end();
		}
	}