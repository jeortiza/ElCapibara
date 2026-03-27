package com.autonoma.elcapibara.ui

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.autonoma.elcapibara.R
import com.autonoma.elcapibara.adapter.ProductoAdapter
import com.autonoma.elcapibara.viewmodel.ProductoViewModel
import com.google.android.material.button.MaterialButton
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ProductoViewModel
    private lateinit var adapter: ProductoAdapter
    private var idProductoEnEdicion: String? = null

    // ==========================================
    // NUEVOS SUPERPODERES: FIREBASE STORAGE Y CÁMARA
    // ==========================================

    // 1. Conectamos con nuestro archivador de Firebase
    private val storageRef = FirebaseStorage.getInstance().reference

    // 2. Lanzador para abrir la GALERÍA
    private val abrirGaleriaLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            subirFotoAFirebase(uri, null) // Si eligió foto, la subimos
        }
    }

    // 3. Lanzador para abrir la CÁMARA
    private val abrirCamaraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        if (bitmap != null) {
            subirFotoAFirebase(null, bitmap) // Si tomó la foto, la subimos
        }
    }
    // ==========================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // =======================================================
        // --- ACTUALIZADO: BOTÓN "VOLVER" (AHORA SÓLIDO) ---
        // =======================================================
        // Ojo: Cambiamos 'TextView' por 'MaterialButton' y 'tvVolverMenu' por 'btnVolverMenu'
        val btnVolver = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnVolverMenu)

        btnVolver.setOnClickListener {
            finish() // Cierra esta pantalla y revela el Menú
        }
        // =======================================================

        viewModel = ViewModelProvider(this)[ProductoViewModel::class.java]

        // --- AQUÍ ESTÁ LA MAGIA: ACTUALIZAMOS LOS NOMBRES (IDs) AL NUEVO DISEÑO ---
        val etNombre = findViewById<EditText>(R.id.etNombreProducto)
        val etPrecio = findViewById<EditText>(R.id.etPrecioProducto)
        val etStock = findViewById<EditText>(R.id.etStockProducto)
        val etFecha = findViewById<EditText>(R.id.etFechaVencimiento)
        val etImagen = findViewById<EditText>(R.id.etUrlImagen)

        // Ojo: Ahora usamos MaterialButton para que respete tu diseño con bordes redondeados
        val btnGuardar = findViewById<MaterialButton>(R.id.btnGuardarProducto)
        val btnSeleccionarFoto = findViewById<MaterialButton>(R.id.btnTomarFoto)

        val rvProductos = findViewById<RecyclerView>(R.id.rvInventario)
        // -------------------------------------------------------------------------

        rvProductos.layoutManager = LinearLayoutManager(this)

        adapter = ProductoAdapter(
            listaProductos = emptyList(),
            onEliminarClick = { idProductoSecreto ->
                viewModel.eliminarProducto(idProductoSecreto)
                Toast.makeText(this, "Eliminando producto...", Toast.LENGTH_SHORT).show()
            },
            onEditarClick = { productoQueVamosAEditar ->
                etNombre.setText(productoQueVamosAEditar.nombre)
                etPrecio.setText(productoQueVamosAEditar.precio.toString())
                etStock.setText(productoQueVamosAEditar.stock.toString())
                etFecha.setText(productoQueVamosAEditar.fechaVencimiento)
                etImagen.setText(productoQueVamosAEditar.imagenUrl)

                idProductoEnEdicion = productoQueVamosAEditar.id
                btnGuardar.text = "ACTUALIZAR PRODUCTO" // En mayúsculas para tu nuevo diseño
            }
        )
        rvProductos.adapter = adapter

        viewModel.productosLiveData.observe(this) { listaNueva ->
            adapter.actualizarLista(listaNueva)
        }

        viewModel.obtenerListaDeProductos()

        // ==========================================
        // ACCIÓN DE TU BOTÓN "FOTO" (AHORA MARRÓN)
        // ==========================================
        btnSeleccionarFoto.setOnClickListener {
            // Creamos un menú emergente con dos opciones
            val opciones = arrayOf("📷 Tomar una foto", "🖼️ Elegir de la galería")

            val builder = AlertDialog.Builder(this)
            builder.setTitle("¿De dónde sacamos la foto?")
            builder.setItems(opciones) { _, seleccion ->
                when (seleccion) {
                    0 -> abrirCamaraLauncher.launch(null)      // Abre la cámara
                    1 -> abrirGaleriaLauncher.launch("image/*") // Abre la galería
                }
            }
            builder.show()
        }
        // ==========================================

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val precioTexto = etPrecio.text.toString()
            val stockTexto = etStock.text.toString()
            val fecha = etFecha.text.toString()
            val imagen = etImagen.text.toString()

            if (nombre.isNotEmpty() && precioTexto.isNotEmpty() && stockTexto.isNotEmpty()) {
                val precio = precioTexto.toDouble()
                val stock = stockTexto.toInt()

                if (idProductoEnEdicion == null) {
                    viewModel.guardarNuevoProducto(nombre, precio, stock, fecha, imagen)
                    Toast.makeText(this, "Guardando producto...", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.actualizarProducto(idProductoEnEdicion!!, nombre, precio, stock, fecha, imagen)
                    Toast.makeText(this, "Actualizando producto...", Toast.LENGTH_SHORT).show()
                    idProductoEnEdicion = null
                    btnGuardar.text = "GUARDAR PRODUCTO" // Volvemos al texto original
                }

                etNombre.text.clear()
                etPrecio.text.clear()
                etStock.text.clear()
                etFecha.text.clear()
                etImagen.text.clear()
            } else {
                Toast.makeText(this, "Por favor, llena los datos principales", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ==========================================
    // FUNCIÓN MÁGICA PARA SUBIR LA FOTO A FIREBASE
    // ==========================================
    private fun subirFotoAFirebase(uri: Uri?, bitmap: Bitmap?) {
        Toast.makeText(this, "Subiendo foto a la nube... ☁️", Toast.LENGTH_LONG).show()

        // Creamos un nombre único para la foto
        val nombreArchivo = "productos/foto_${System.currentTimeMillis()}.jpg"
        val archivoRef = storageRef.child(nombreArchivo)

        if (uri != null) {
            // OPCIÓN A: Si eligió una foto de la GALERÍA
            archivoRef.putFile(uri).addOnSuccessListener {
                archivoRef.downloadUrl.addOnSuccessListener { linkDeDescarga ->
                    // TAMBIÉN ACTUALIZAMOS EL ID AQUÍ ABAJO (etUrlImagen)
                    findViewById<EditText>(R.id.etUrlImagen).setText(linkDeDescarga.toString())
                    Toast.makeText(this, "¡Foto lista! ✅", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error al subir la foto ❌", Toast.LENGTH_SHORT).show()
            }

        } else if (bitmap != null) {
            // OPCIÓN B: Si tomó una foto nueva con la CÁMARA
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            archivoRef.putBytes(data).addOnSuccessListener {
                archivoRef.downloadUrl.addOnSuccessListener { linkDeDescarga ->
                    // TAMBIÉN ACTUALIZAMOS EL ID AQUÍ ABAJO (etUrlImagen)
                    findViewById<EditText>(R.id.etUrlImagen).setText(linkDeDescarga.toString())
                    Toast.makeText(this, "¡Foto lista! ✅", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error al subir la foto ❌", Toast.LENGTH_SHORT).show()
            }
        }
    }
}