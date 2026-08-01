package frc.robot.engine

import edu.wpi.first.networktables.NetworkTableInstance
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class DashboardNumber(
    private val defaultValue: Double,
    tableName: String,
    entryName: String,
    isPersistent: Boolean = false,
) : ReadWriteProperty<Any?, Double> {
    private val entry = NetworkTableInstance.getDefault().getTable(tableName).getEntry(entryName)

    init {
        entry.setDefaultDouble(defaultValue)
        if (isPersistent) entry.setPersistent()
    }

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): Double =
        entry.getDouble(defaultValue)

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Double) {
        entry.setDouble(value)
    }
}
