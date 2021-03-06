package com.hitg.sensores

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        doLoginFingerPrint()

        btLogin.setOnClickListener {
            doLoginFingerPrint()
        }
    }

    private fun doLoginFingerPrint() {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                authUser(executor)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {

            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {

            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {

            }
        }
    }

    private fun authUser(executor: Executor) {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Sensores")
            .setSubtitle("Autenticação biométrica")
            .setDescription("Utilize o sensor de digital para autenticar")
            .setDeviceCredentialAllowed(true)
            .build()
        val biometricPrompt =
            BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult
                    ) {
                        super.onAuthenticationSucceeded(result)
                        startActivity(
                            Intent(this@LoginActivity, MainActivity::class.java)
                        )
                        finish()
                    }

                    override fun onAuthenticationError(
                        errorCode: Int, errString: CharSequence
                    ) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(
                            applicationContext,
                            "Usuário sem permissão de acesso",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(
                            applicationContext,
                            "Falha na autenticação",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        biometricPrompt.authenticate(promptInfo)
    }
}