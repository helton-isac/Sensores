package com.hitg.sensores.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.util.Log

class FlipDetection(private val sensorManager: SensorManager) {

    private lateinit var gravitySensor: Sensor
    //private lateinit var mediaPlayer: MediaPlayer

    init {
        //mediaPlayer = MediaPlayer.create(this, R.raw.super_mario_theme)
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) == null) {
            Log.i("SENSOR", "SENSOR DE GRAVIDADE INDISPONÍVEL")
        } else {
            gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        }
    }

    fun onSensorChanged(p0: SensorEvent?) {
        p0?.let {
            if (p0.values[2] < -9.7) {
                //if (mediaPlayer.isPlaying) mediaPlayer.pause()
            } else {
                //if (!mediaPlayer.isPlaying) mediaPlayer.start()
            }
        }
    }
}