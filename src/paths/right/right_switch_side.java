package paths.right;

import org.usfirst.frc.team670.robot.Robot;
import org.usfirst.frc.team670.robot.commands.auto_specific.DropCube;
import org.usfirst.frc.team670.robot.commands.auto_specific.SpinIntake;
import org.usfirst.frc.team670.robot.commands.drive.Drive;
import org.usfirst.frc.team670.robot.commands.drive.Encoders_Drive;
import org.usfirst.frc.team670.robot.commands.drive.Pivot;
import org.usfirst.frc.team670.robot.commands.elevator.Encoders_Elevator;
import org.usfirst.frc.team670.robot.commands.intake.Deploy;
import org.usfirst.frc.team670.robot.constants.Field;
import org.usfirst.frc.team670.robot.constants.RoboConstants;
import org.usfirst.frc.team670.robot.constants.enums.ElevatorState;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class right_switch_side extends CommandGroup {

	public right_switch_side() {
		addSequential(new Drive(Field.DS_TO_SWITCH - Robot.length/2 + Field.SWITCH_WIDTH/2));
		addParallel(new Encoders_Elevator(ElevatorState.SWITCH));
		addSequential(new Pivot(-90));
		addSequential(new Drive(Field.SIDE_TO_SWITCH - Robot.width - Field.SIDE_TRIANGLE_WIDTH));
    	addSequential(new DropCube());
    	addSequential(new Drive(-(Field.SIDE_TO_SWITCH - Robot.width - Field.SIDE_TRIANGLE_WIDTH)));
	}
}
