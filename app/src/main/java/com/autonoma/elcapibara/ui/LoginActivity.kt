package com.autonoma.elcapibara.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.autonoma.elcapibara.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etCorreo: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnAcceder: MaterialButton
    private lateinit var tvRegistrarse: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializamos Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Conectamos las variables con los IDs del diseño XML
        etCorreo = findViewById(R.id.etCorreoLogin)
        etPassword = findViewById(R.id.etPasswordLogin)
        btnAcceder = findViewById(R.id.btnAcceder)
        tvRegistrarse = findViewById(R.id.tvRegistrarse)

        // Acción del botón ACCEDER
        btnAcceder.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (correo.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            iniciarSesion(correo, password)
        }

        // Acción de Registrarse
        tvRegistrarse.setOnClickListener {
            Toast.makeText(this, "Próximamente: Pantalla de Registro", Toast.LENGTH_SHORT).show()
        }
    }

    private fun iniciarSesion(correo: String, password: String) {
        auth.signInWithEmailAndPassword(correo, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "¡Bienvenido a El Capibara!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Error: Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
    }
}