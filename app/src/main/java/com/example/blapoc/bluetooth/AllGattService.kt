package com.example.blapoc.bluetooth

import java.util.*
import kotlin.collections.HashMap


class AllGattServices {

    private val attributes: HashMap<String, String> = HashMap()
    var HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb"
    fun lookup(uuid: UUID): String {
        return lookup(uuid.toString(), "--")
    }

    fun lookup(uuid: String?, defaultName: String): String {
        val name = attributes[uuid]
        return name ?: defaultName
    }

    init {
        attributes["00001800-0000-1000-8000-00805f9b34fb"] = "Generic Access"
        attributes["00001801-0000-1000-8000-00805f9b34fb"] = "Generic Attribute"
        attributes["00001802-0000-1000-8000-00805f9b34fb"] = "Immediate Alert"
        attributes["00001803-0000-1000-8000-00805f9b34fb"] = "Link Loss"
        attributes["00001804-0000-1000-8000-00805f9b34fb"] = "Tx Power"
        attributes["00001805-0000-1000-8000-00805f9b34fb"] = "Current Time Service"
        attributes["00001806-0000-1000-8000-00805f9b34fb"] = "Reference Time Update Service"
        attributes["00001807-0000-1000-8000-00805f9b34fb"] = "Next DST Change Service"
        attributes["00001808-0000-1000-8000-00805f9b34fb"] = "Glucose"
        attributes["00001809-0000-1000-8000-00805f9b34fb"] = "Health Thermometer"
        attributes["0000180a-0000-1000-8000-00805f9b34fb"] = "Device Information"
        attributes["0000180d-0000-1000-8000-00805f9b34fb"] = "Heart Rate"
        attributes["0000180e-0000-1000-8000-00805f9b34fb"] = "Phone Alert Status Service"
        attributes["0000180f-0000-1000-8000-00805f9b34fb"] = "Battery Service"
        attributes["00001810-0000-1000-8000-00805f9b34fb"] = "Blood Pressure"
        attributes["00001811-0000-1000-8000-00805f9b34fb"] = "Alert Notification Service"
        attributes["00001812-0000-1000-8000-00805f9b34fb"] = "Human Interface Device"
        attributes["00001813-0000-1000-8000-00805f9b34fb"] = "Scan Parameters"
        attributes["00001814-0000-1000-8000-00805f9b34fb"] = "Running Speed and Cadence"
        attributes["00001815-0000-1000-8000-00805f9b34fb"] = "Automation IO"
        attributes["00001816-0000-1000-8000-00805f9b34fb"] = "Cycling Speed and Cadence"
        attributes["00001818-0000-1000-8000-00805f9b34fb"] = "Cycling Power"
        attributes["00001819-0000-1000-8000-00805f9b34fb"] = "Location and Navigation"
        attributes["0000181a-0000-1000-8000-00805f9b34fb"] = "Environmental Sensing"
        attributes["0000181b-0000-1000-8000-00805f9b34fb"] = "Body Composition"
        attributes["0000181c-0000-1000-8000-00805f9b34fb"] = "User Data"
        attributes["0000181d-0000-1000-8000-00805f9b34fb"] = "Weight Scale"
        attributes["0000181e-0000-1000-8000-00805f9b34fb"] = "Bond Management Service"
        attributes["0000181f-0000-1000-8000-00805f9b34fb"] = "Continuous Glucose Monitoring"
        attributes["00001820-0000-1000-8000-00805f9b34fb"] = "Internet Protocol Support Service"
        attributes["00001821-0000-1000-8000-00805f9b34fb"] = "Indoor Positioning"
        attributes["00001822-0000-1000-8000-00805f9b34fb"] = "Pulse Oximeter Service"
        attributes["00001823-0000-1000-8000-00805f9b34fb"] = "HTTP Proxy"
        attributes["00001824-0000-1000-8000-00805f9b34fb"] = "Transport Discovery"
        attributes["00001825-0000-1000-8000-00805f9b34fb"] = "Object Transfer Service"
        attributes["00001826-0000-1000-8000-00805f9b34fb"] = "Fitness Machine"
        attributes["00001827-0000-1000-8000-00805f9b34fb"] = "Mesh Provisioning Service"
        attributes["00001828-0000-1000-8000-00805f9b34fb"] = "Mesh Proxy Service"
        attributes["00001829-0000-1000-8000-00805f9b34fb"] = "Reconnection Configuration"


        // Sample Characteristics.
        attributes[HEART_RATE_MEASUREMENT] = "Heart Rate Measurement"
        attributes["00002a29-0000-1000-8000-00805f9b34fb"] = "Manufacturer Name String"
        attributes["00002a00-0000-1000-8000-00805f9b34fb"] = "Device Name"
        attributes["00002a01-0000-1000-8000-00805f9b34fb"] = "Appearance"
        attributes["00002a02-0000-1000-8000-00805f9b34fb"] = "Peripheral Privacy Flag"
        attributes["00002a03-0000-1000-8000-00805f9b34fb"] = "Reconnection Address"
        attributes["00002a04-0000-1000-8000-00805f9b34fb"] = "Manufacturer Name String"
        attributes["00002a05-0000-1000-8000-00805f9b34fb"] = "Service Changed"
        attributes["00002A06-0000-1000-8000-00805f9b34fb"] = "Alert level"
    }

    companion object {
        @kotlin.jvm.JvmField
        var CLIENT_CHARACTERISTIC_CONFIG: String = "00002902-0000-1000-8000-00805f9b34fb";
    }
}