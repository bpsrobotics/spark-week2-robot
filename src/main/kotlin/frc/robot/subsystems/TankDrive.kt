package frc.robot.subsystems

import com.revrobotics.spark.SparkLowLevel.MotorType
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode
import com.revrobotics.spark.config.SparkMaxConfig
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.engine.DashboardNumber
import frc.robot.engine.SparkWrapper
import kotlin.math.abs
import kotlin.math.max

object TankDrive : SubsystemBase() {
    object Constants {
        const val LEFT_LEADER_ID = 10
        const val LEFT_FOLLOWER_ID = 11
        const val RIGHT_LEADER_ID = 12
        const val RIGHT_FOLLOWER_ID = 13
    }

    private fun SparkMaxConfig.defaults() = apply {
        idleMode(IdleMode.kBrake)
        smartCurrentLimit(60)
        voltageCompensation(12.0)
    }

    private val leftLeader =
        SparkWrapper(Constants.LEFT_LEADER_ID, MotorType.kBrushed) {
            defaults()
            inverted(true)
        }
    private val rightLeader =
        SparkWrapper(Constants.RIGHT_LEADER_ID, MotorType.kBrushed) {
            defaults()
            inverted(false)
        }
    private val leftFollower =
        SparkWrapper(Constants.LEFT_FOLLOWER_ID, MotorType.kBrushed) {
            defaults()
            follow(Constants.LEFT_LEADER_ID)
        }
    private val rightFollower =
        SparkWrapper(Constants.RIGHT_FOLLOWER_ID, MotorType.kBrushed) {
            defaults()
            follow(Constants.RIGHT_LEADER_ID)
        }

    private var speedFactor by DashboardNumber(1.0, "SmartDashboard/TankDrive", "speedFactor")

    fun arcadeDrive(forward: Double, rotation: Double) {
        val left = forward + rotation
        val right = forward - rotation
        val maxMag = max(abs(left), abs(right))
        val denom = if (maxMag > 1.0) maxMag else 1.0
        SmartDashboard.putNumber("TankDrive/arcadeDrive/left", left)
        SmartDashboard.putNumber("TankDrive/arcadeDrive/right", right)
        SmartDashboard.putNumber("TankDrive/arcadeDrive/maxMag", maxMag)
        leftLeader.set(left / denom * speedFactor)
        rightLeader.set(right / denom * speedFactor)
    }

    fun stop() {
        leftLeader.stopMotor()
        rightLeader.stopMotor()
    }
}
