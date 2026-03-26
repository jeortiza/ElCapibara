package com.autonoma.elcapibara.repository

import com.autonoma.elcapibara.model.Producto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val productosRef = db.collection("productos")

    // 1. Crear (C)
    suspend fun agregarProducto(producto: Producto): Boolean {
        return try {
            val nuevoDocumento = productosRef.document()
            producto.id = nuevoDocumento.id
            nuevoDocumento.set(producto).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // 2. Leer (R)
    suspend fun obtenerProductos(): List<Producto> {
        return try {
            val resultado = productosRef.get().await()
            val lista = mutableListOf<Producto>()
            for (documento in resultado.documents) {
                val producto = documento.toObject(Producto::class.java)
                if (producto != null) {
                    producto.id = documento.id
                    lista.add(producto)
                }
            }
            lista
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // 3. Eliminar (D)
    suspend fun eliminarProducto(id: String): Boolean {
        return try {
            productosRef.document(id).delete().await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // 4. NUEVO: Actualizar (U)
    suspend fun actualizarProducto(producto: Producto): Boolean {
        return try {
            // Buscamos el documento por su ID y lo sobreescribimos con los datos nuevos
            productosRef.document(producto.id).set(producto).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}