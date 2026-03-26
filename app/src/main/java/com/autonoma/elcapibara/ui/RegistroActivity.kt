package com.autonoma.elcapibara.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.autonoma.elcapibara.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegistroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    // Agregamos las variables para los nuevos campos
    private lateinit var etNombres: TextInputEditText
    private lateinit var etCorreo: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etRepetirPassword: TextInputEditText
    private lateinit var btnCrearCuenta: MaterialButton
    private lateinit var tvVolverLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        auth = FirebaseAuth.getInstance()

        // Enlazamos con los nuevos IDs del diseño XML
        etNombres = findViewById(R.id.etNombresRegistro)
        etCorreo = findViewById(R.id.etCorreoRegistro)
        etPassword = findViewById(R.id.etPasswordRegistro)
        etRepetirPassword = findViewById(R.id.etRepetirPassword)
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta)
        tvVolverLogin = findViewById(R.id.tvVolverLogin)

        btnCrearCuenta.setOnClickListener {
            val nombres = etNombres.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val repetirPassword = etRepetirPassword.text.toString().trim()

            // 1. Validar que no haya campos vacíos
            if (nombres.isEmpty() || correo.isEmpty() || password.isEmpty() || repetirPassword.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. Validar que las contraseñas sean idénticas
            if (password != repetirPassword) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 3. Validar longitud de la contraseña
            if (password.length < 6) {
                Toast.makeText(this, "La contraseña debe tener 6 caracteres o más", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Si todo está correcto, creamos el usuario
            crearNuevoUsuario(correo, password, nombres)
        }

        tvVolverLogin.setOnClickListener {
            finish()
        }
    }

    private fun crearNuevoUsuario(correo: String, password: String, nombres: String) {
        auth.createUserWithEmailAndPassword(correo, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Si se crea con éxito, le asignamos el "Nombre" a su perfil de Firebase
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(nombres)
                        .build()

                    user?.updateProfile(profileUpdates)?.addOnCompleteListener {
                        Toast.makeText(this, "¡Cuenta creada con éxito, $nombres!", Toast.LENGTH_SHORT).show()
                        finish() // Regresamos al Login
                    }
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}