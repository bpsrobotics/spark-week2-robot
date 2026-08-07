package frc.robot.input

import edu.wpi.first.math.MathUtil
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import frc.robot.subsystems.Intake
import frc.robot.subsystems.TankDrive
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sign

object OI : SubsystemBase() {
    object Constants {
        const val DRIVER_CONTROLLER_PORT = 0
    }

    private const val DEADZONE_THRESHOLD = 0.1

    private fun process(
        input: Double,
        deadzone: Double = DEADZONE_THRESHOLD,
        power: Double,
    ): Double {
        var output = 0.0

        if (deadzone != 0.0) {
            output = MathUtil.applyDeadband(input, deadzone)
        }

        if (power != 1.0) {
            output = output.absoluteValue.pow(power) * sign(output)
        }

        return output
    }

    val driverController = CommandXboxController(Constants.DRIVER_CONTROLLER_PORT)

    val forward
        get() = -process(driverController.leftY, power = 1.5)

    val rotation
        get() = process(driverController.leftX, power = 1.5)

    fun configureBindings() {
        val intakePower = {
            MathUtil.interpolate(
                Intake.Constants.POWER,
                Intake.Constants.OVERDRIVE_POWER,
                driverController.rightTriggerAxis,
            )
        }
        val outtakePower = {
            MathUtil.interpolate(
                -Intake.Constants.POWER,
                -Intake.Constants.OVERDRIVE_POWER,
                driverController.rightTriggerAxis,
            )
        }

        driverController.a().whileTrue(Intake.runCommand(intakePower))
        driverController.b().whileTrue(Intake.runCommand(outtakePower))

        val driveSpeedFactor = {
            MathUtil.interpolate(
                TankDrive.Constants.MAX_SPEED_FACTOR,
                TankDrive.Constants.MIN_SPEED_FACTOR,
                driverController.leftTriggerAxis,
            )
        }
        driverController
            .leftTrigger(0.01)
            .whileTrue(
                TankDrive.run {
                    TankDrive.arcadeDrive(forward, rotation * 0.5, driveSpeedFactor())
                }
            )
    }

    override fun periodic() {
        SmartDashboard.putNumber("OI/forward", forward)
        SmartDashboard.putNumber("OI/rotation", rotation)
    }
}
