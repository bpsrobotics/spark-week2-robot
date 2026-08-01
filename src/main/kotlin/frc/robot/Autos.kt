package frc.robot

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.InstantCommand

object Autos {
    private var autoCommandChooser: SendableChooser<Command> = SendableChooser()
    val autonomousCommand: Command
        get() = autoCommandChooser.selected

    fun addAutos() {
        autoCommandChooser.setDefaultOption("No Auto", InstantCommand())
        SmartDashboard.putData("Auto Chooser", autoCommandChooser)
    }
}
