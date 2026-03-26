package com.autonoma.elcapibara.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.autonoma.elcapibara.R
import com.autonoma.elcapibara.model.Producto
// NUEVO: Importamos a nuestro fotógrafo
import com.bumptech.glide.Glide

class ProductoAdapter(
    private var listaProductos: List<Producto>,
    private val onEliminarClick: (String) -> Unit,
    private val onEditarClick: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvItemNombre)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvItemPrecio)
        val tvStock: TextView = itemView.findViewById(R.id.tvItemStock)
        val tvFecha: TextView = itemView.findViewById(R.id.tvItemFecha)
        val btnBorrar: Button = itemView.findViewById(R.id.btnBorrar)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)

        // NUEVO: Buscamos el recuadro gris que creaste en el diseño
        val ivImagen: ImageView = itemView.findViewById(R.id.ivItemImagen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaProductos.size
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = listaProductos[position]

        holder.tvNombre.text = producto.nombre
        holder.tvPrecio.text = "Precio: S/ ${producto.precio}"
        holder.tvStock.text = "Stock: ${producto.stock}"

        if (producto.fechaVencimiento.isNotEmpty()) {
            holder.tvFecha.text = "Vence: ${producto.fechaVencimiento}"
        } else {
            holder.tvFecha.text = "Vence: Sin fecha"
        }

        // NUEVO: La magia de la foto
        if (producto.imagenUrl.isNotEmpty()) {
            // Si el producto tiene un enlace, Glide va a internet, la descarga y la pega
            Glide.with(holder.itemView.context)
                .load(producto.imagenUrl)
                .into(holder.ivImagen)
        } else {
            // Si lo dejaron en blanco, limpiamos el recuadro para que quede gris
            holder.ivImagen.setImageDrawable(null)
        }

        holder.btnBorrar.setOnClickListener {
            onEliminarClick(producto.id)
        }

        holder.btnEditar.setOnClickListener {
            onEditarClick(producto)
        }
    }

    fun actualizarLista(nuevaLista: List<Producto>) {
        listaProductos = nuevaLista
        notifyDataSetChanged()
    }
}