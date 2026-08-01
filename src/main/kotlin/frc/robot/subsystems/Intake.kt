package frc.robot.subsystems

import com.revrobotics.spark.SparkLowLevel.MotorType
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.engine.SparkWrapper

object Intake : SubsystemBase() {
    object Constants {
        const val MOTOR_ID = 20
        const val POWER = 0.45
        const val OVERDRIVE_POWER = 1.0
    }

    private val motor: SparkWrapper =
        SparkWrapper(Constants.MOTOR_ID, MotorType.kBrushless) {
            idleMode(IdleMode.kCoast)
            smartCurrentLimit(40)
            voltageCompensation(12.0)
            inverted(true)
        }

    fun run(power: Double) {
        motor.set(power)
    }

    fun stop() {
        motor.stopMotor()
    }

    fun runCommand(power: () -> Double): Command =
        runEnd({ motor.set(power()) }, { motor.stopMotor() })
}
