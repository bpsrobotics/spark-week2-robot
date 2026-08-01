package frc.robot

import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.InstantCommand

class Robot : TimedRobot() {
    var autoCommand: Command = InstantCommand()
    lateinit var robotContainer: RobotContainer
    val commandScheduler: CommandScheduler = CommandScheduler.getInstance()

    override fun robotInit() {
        robotContainer = RobotContainer()
    }

    override fun robotPeriodic() {
        commandScheduler.run()
    }

    override fun disabledInit() {}

    override fun disabledPeriodic() {}

    override fun autonomousInit() {
        autoCommand = Autos.autonomousCommand
        autoCommand.let { CommandScheduler.getInstance().schedule(autoCommand) }
    }

    override fun autonomousPeriodic() {}

    override fun teleopInit() {
        autoCommand.cancel()
    }

    override fun teleopPeriodic() {}

    override fun testInit() {
        CommandScheduler.getInstance().cancelAll()
    }

    override fun testPeriodic() {}

    override fun simulationInit() {}

    override fun simulationPeriodic() {}
}
