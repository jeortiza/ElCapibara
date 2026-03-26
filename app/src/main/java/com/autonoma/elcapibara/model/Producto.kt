package com.autonoma.elcapibara.model

data class Producto(
    var id: String = "",
    var nombre: String = "",
    var precio: Double = 0.0,
    var stock: Int = 0,
    var fechaVencimiento: String = "",
    var imagenUrl: String = ""
)
