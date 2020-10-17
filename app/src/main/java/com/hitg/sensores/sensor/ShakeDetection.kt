package com.hitg.sensores.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.widget.Toast
import kotlin.math.abs

class ShakeDetection(private val sensorManager: SensorManager) {


    private lateinit var accelerometer: Sensor

    init {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) {
            Log.i("SENSOR", "ACELEROMETRO INDISPONÍVEL")
        } else {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        }
    }


    private var currentX: Float = 0.0f
    private var currentY: Float = 0.0f
    private var currentZ: Float = 0.0f

    private var lastX: Float = 0.0f
    private var lastY: Float = 0.0f
    private var lastZ: Float = 0.0f

    private var xDifference: Float = 0.0f
    private var yDifference: Float = 0.0f
    private var zDifference: Float = 0.0f

    private val shakeThreshold = 5.0f

    private var itIsNotFirstTime = false

    fun register(listener: SensorEventListener) {
        sensorManager.registerListener(
            listener, accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    fun unregisterListener(listener: SensorEventListener) {
        sensorManager.unregisterListener(listener)
    }

    fun onSensorChanged(sensorEvent: SensorEvent?, context: Context) {
        sensorEvent?.let {
            currentX = it.values[0]
            currentY = it.values[1]
            currentZ = it.values[2]
            if (itIsNotFirstTime) {
                xDifference = abs(lastX - currentX)
                yDifference = abs(lastY - currentY)
                zDifference = abs(lastZ - currentZ)
                if ((xDifference > shakeThreshold && yDifference > shakeThreshold) ||
                    (xDifference > shakeThreshold && zDifference > shakeThreshold) ||
                    (yDifference > shakeThreshold && zDifference > shakeThreshold)
                ) {
                    Toast.makeText(context, "Shake", Toast.LENGTH_SHORT).show()
                }
            }
            lastX = currentX
            lastY = currentY
            lastZ = currentZ
            itIsNotFirstTime = true
        }
    }
}