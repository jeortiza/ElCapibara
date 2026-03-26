package com.autonoma.elcapibara.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autonoma.elcapibara.model.Producto
import com.autonoma.elcapibara.repository.ProductoRepository
import kotlinx.coroutines.launch

class ProductoViewModel : ViewModel() {

    private val repository = ProductoRepository()
    val productosLiveData = MutableLiveData<List<Producto>>()

    fun guardarNuevoProducto(nombre: String, precio: Double, stock: Int, fechaVencimiento: String, imagenUrl: String) {
        val productoAEmpacar = Producto(
            nombre = nombre,
            precio = precio,
            stock = stock,
            fechaVencimiento = fechaVencimiento,
            imagenUrl = imagenUrl
        )

        viewModelScope.launch {
            val exito = repository.agregarProducto(productoAEmpacar)
            if (exito) {
                obtenerListaDeProductos()
            }
        }
    }

    fun obtenerListaDeProductos() {
        viewModelScope.launch {
            val lista = repository.obtenerProductos()
            productosLiveData.postValue(lista)
        }
    }

    fun eliminarProducto(idProducto: String) {
        viewModelScope.launch {
            val exito = repository.eliminarProducto(idProducto)
            if (exito) {
                obtenerListaDeProductos()
            }
        }
    }

    // NUEVO: El gerente empaca el producto con su ID original y manda a actualizarlo
    fun actualizarProducto(idOriginal: String, nombre: String, precio: Double, stock: Int, fechaVencimiento: String, imagenUrl: String) {
        val productoActualizado = Producto(
            id = idOriginal, // <- MUY IMPORTANTE: Le pasamos su ID secreto para que no se pierda
            nombre = nombre,
            precio = precio,
            stock = stock,
            fechaVencimiento = fechaVencimiento,
            imagenUrl = imagenUrl
        )

        viewModelScope.launch {
            val exito = repository.actualizarProducto(productoActualizado)
            if (exito) {
                // Si se actualizó con éxito, recargamos la lista
                obtenerListaDeProductos()
            }
        }
    }
}