package com.hitg.sensores

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs

class ShakeActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager

    private lateinit var accelerometer: Sensor

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shake)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) {
            Log.i("SENSOR", "ACELEROMETRO INDISPONÍVEL")
        } else {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        }
    }


    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    private fun printSensors() {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensorList: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
        for (sensor in sensorList) {
            Log.i(
                "SENSOR",
                "Nome: ${sensor.name} - Tipo ${sensor.type} - ${sensor.stringType} "
            )
        }
    }

    override fun onAccuracyChanged(sensorEvent: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
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
                    Toast.makeText(this, "Shake", Toast.LENGTH_SHORT).show()
                }
            }
            lastX = currentX
            lastY = currentY
            lastZ = currentZ
            itIsNotFirstTime = true
        }
    }
}