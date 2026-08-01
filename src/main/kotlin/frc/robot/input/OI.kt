package frc.robot.input

import edu.wpi.first.math.MathUtil
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import frc.robot.subsystems.Intake
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sign

object OI : SubsystemBase() {
    object Constants {
        const val DRIVER_CONTROLLER_PORT = 0
        const val OPERATOR_CONTROLLER_PORT = 1
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

    @JvmName("process1")
    fun Double.process(deadzone: Double = DEADZONE_THRESHOLD, power: Double) =
        process(this, deadzone, power)

    val driverController = CommandXboxController(Constants.DRIVER_CONTROLLER_PORT)
    val operatorController = CommandXboxController(Constants.OPERATOR_CONTROLLER_PORT)

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
    }

    override fun periodic() {
        SmartDashboard.putNumber("OI/forward", forward)
        SmartDashboard.putNumber("OI/rotation", rotation)
    }
}
