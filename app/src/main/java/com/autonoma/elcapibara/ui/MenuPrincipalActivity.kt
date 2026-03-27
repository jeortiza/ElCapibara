package com.autonoma.elcapibara.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.autonoma.elcapibara.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class MenuPrincipalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        // --- SALUDO INTELIGENTE ---
        // 1. Enlazamos el texto del título en la pantalla
        val tvBienvenida = findViewById<android.widget.TextView>(R.id.tvBienvenidaMenu)

        // 2. Le preguntamos a Firebase quién es el usuario que acaba de entrar
        val usuarioActual = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser

        // 3. Rescatamos su nombre (si por alguna razón no tiene, pondrá "USUARIO" por defecto)
        val nombreCompleto = usuarioActual?.displayName ?: "USUARIO"

        // 4. (Opcional pero recomendado) Cortamos el nombre para usar solo el primer nombre y que no se desborde la pantalla
        val primerNombre = nombreCompleto.split(" ")[0].uppercase()

        // 5. Reescribimos el texto de la pantalla con el nombre real
        tvBienvenida.text = "BIENVENIDO(A)\n$primerNombre"
        // --------------------------

        // 1. Enlazamos los botones principales
        val btnProductos = findViewById<MaterialButton>(R.id.btnMenuProductos)
        val btnVentas = findViewById<MaterialButton>(R.id.btnMenuVentas)
        val btnInventario = findViewById<MaterialButton>(R.id.btnMenuInventario)
        val btnReportes = findViewById<MaterialButton>(R.id.btnMenuReportes)

        // 2. Programamos los clics de los botones principales
        btnProductos.setOnClickListener {
            Toast.makeText(this, "Módulo de Productos: Próximamente", Toast.LENGTH_SHORT).show()
        }

        btnVentas.setOnClickListener {
            Toast.makeText(this, "Módulo de Ventas: Próximamente", Toast.LENGTH_SHORT).show()
        }

        btnInventario.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnReportes.setOnClickListener {
            Toast.makeText(this, "Módulo de Reportes: Próximamente", Toast.LENGTH_SHORT).show()
        }

        // 3. Botón flotante de Cerrar Sesión (El que activa el menú emergente)
        val btnCerrarSesion = findViewById<ImageView>(R.id.btnCerrarSesionIcono)

        btnCerrarSesion.setOnClickListener { view ->
            // Creamos y mostramos el Popup
            val popup = PopupMenu(this, view)
            popup.menuInflater.inflate(R.menu.popup_cerrar_sesion, popup.menu)

            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.item_cerrar_sesion_popup -> {
                        // Lógica para salir de forma segura
                        FirebaseAuth.getInstance().signOut()
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }
}