package com.hitg.sensores

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hitg.sensores.sensor.Shake

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager

    private lateinit var shake: Shake

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        shake = Shake(sensorManager)
    }

    override fun onResume() {
        super.onResume()
        shake.register(this)
    }

    override fun onPause() {
        super.onPause()
        shake.unregisterListener(this)
    }

    private fun printSensors() {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensorList: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
        for (sensor in sensorList) {
            Log.i("SENSOR",
                    "Nome: ${sensor.name} - Tipo ${sensor.type} - ${sensor.stringType} ")
        }
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        shake.onSensorChanged(sensorEvent, this)
    }

    override fun onAccuracyChanged(sensorEvent: Sensor?, p1: Int) {
    }
}