package com.autonoma.elcapibara.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.autonoma.elcapibara.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class MenuPrincipalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        // Enlazamos los nuevos botones marrones de tu diseño
        val btnProductos = findViewById<MaterialButton>(R.id.btnMenuProductos)
        val btnVentas = findViewById<MaterialButton>(R.id.btnMenuVentas)
        val btnInventario = findViewById<MaterialButton>(R.id.btnMenuInventario)
        val btnReportes = findViewById<MaterialButton>(R.id.btnMenuReportes)

        // --- PROGRAMAMOS LOS CLICS ---

        // 1. Productos (Próximamente)
        btnProductos.setOnClickListener {
            Toast.makeText(this, "Módulo de Productos: Próximamente", Toast.LENGTH_SHORT).show()
        }

        // 2. Registrar Venta (Próximamente)
        btnVentas.setOnClickListener {
            Toast.makeText(this, "Módulo de Ventas: Próximamente", Toast.LENGTH_SHORT).show()
        }

        // 3. Inventario (¡Este sí nos lleva a la pantalla que ya hicimos!)
        btnInventario.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // 4. Reportes (Próximamente)
        btnReportes.setOnClickListener {
            Toast.makeText(this, "Módulo de Reportes: Próximamente", Toast.LENGTH_SHORT).show()
        }
    }

    // --- TEMPORALMENTE DESACTIVAMOS LOS TRES PUNTITOS ---
    // (Como el error dice que no encuentra R.menu.menu_principal,
    // lo mejor es comentar esto por ahora para que puedas correr la app.
    // Más adelante podemos crear el menú de cerrar sesión con calma).

    /*
    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_cerrar_sesion -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    */
}