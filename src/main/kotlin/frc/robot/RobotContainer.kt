package frc.robot

import frc.robot.input.OI
import frc.robot.subsystems.TankDrive

class RobotContainer {
    init {
        TankDrive.defaultCommand = TankDrive.run {
            TankDrive.arcadeDrive(OI.forward, OI.rotation * 0.5)
        }

        Autos.addAutos()

        OI.configureBindings()
    }
}
