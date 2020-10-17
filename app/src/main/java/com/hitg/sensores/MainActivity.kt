package com.hitg.sensores

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var tempSensor: Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) == null) {
            Log.i("SENSOR", "SENSOR DE TEMPERATURA INDISPONÍVEL")
        } else {
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
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
        sensorEvent?.let {
            Log.i("Valor do sensor", it.values[0].toString())
        }
    }

    override fun onAccuracyChanged(sensorEvent: Sensor?, p1: Int) {
    }
}