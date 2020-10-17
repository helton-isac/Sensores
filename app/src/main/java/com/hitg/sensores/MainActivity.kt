package com.hitg.sensores

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var stepCounter: Sensor
    private lateinit var stepDetector: Sensor
    private var stepDetect = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        checkPermissions()
    }

    private fun initSensors() {
        initStepCounter()
        initStepDetect()
    }

    private fun initStepCounter() {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) == null) {
            Log.i("SENSOR", "Sensor contador de passos indisponivel")
        } else {
            stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        }
    }

    private fun initStepDetect() {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) == null) {
            Log.i("SENSOR", "SENSOR detector de passos indisponivel")
        } else {
            stepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        }
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        p0?.let {
            when (it.sensor) {
                stepCounter -> {
                    Log.i("STEP", p0.values[0].toString())
                    //tvCounted.text = it.values[0].toString()
                }
                stepDetector -> {
                    stepDetect += it.values[0].toInt()

                    Log.i("STEP", p0.values[0].toString())
                    //tvDetected.text = stepDetect.toString()
                }
                else -> {

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (this::stepCounter.isInitialized)
            sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL)
        if (this::stepDetector.isInitialized)
            sensorManager.registerListener(this, stepDetector, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACTIVITY_RECOGNITION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Verifica o status da permissão
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACTIVITY_RECOGNITION
                    )
                ) {
                    // O usuário se recusa completamente a conceder permissão e geralmente solicita que o usuário entre na interface de configuração de permissão.
                    // O usuário irá precisar entrar na interface de configuração de permissão para abrir
                } else {
                    //Solicitação do consentimento do usuario
                    val permissions =
                        listOf(Manifest.permission.ACTIVITY_RECOGNITION)
                    ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 1);
                }
            } else {
                // Permissão concedida pelo usuário
                initSensors()
            }
        } else {
            initSensors()
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
        if (requestCode == 1) {
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // Permissão concedida
                    initSensors()
                } else {
                    // Permissão Negada
                    // application failed
                    Log.d("TAG", "[Permission]" + "ACTIVITY_RECOGNITION application failed")
                }
            }
        }
    }
}