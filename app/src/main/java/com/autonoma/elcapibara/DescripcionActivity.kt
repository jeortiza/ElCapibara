package com.autonoma.elcapibara

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.autonoma.elcapibara.ui.LoginActivity // <-- Esto soluciona la letra roja

class DescripcionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_descripcion)

        // =========================================================
        // --- TEMPORIZADOR: SALTO AUTOMÁTICO AL LOGIN ---
        // =========================================================

        // Configuramos un tiempo de espera de 4 segundos (4000 milisegundos)
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({

            // 1. Preparamos el viaje hacia la pantalla de Login
            val intent = android.content.Intent(this, LoginActivity::class.java)

            // 2. Iniciamos el viaje
            startActivity(intent)

            // 3. Destruimos la pantalla de descripción
            finish()

        }, 4000) // <-- ¡Esta es la sintaxis correcta al final!
    }
}