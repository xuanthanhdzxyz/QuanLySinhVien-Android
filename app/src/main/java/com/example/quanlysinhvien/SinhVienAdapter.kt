package com.example.quanlysinhvien

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SinhVienAdapter(
    private val danhSach: List<SinhVien>,
    private val onItemClick: (SinhVien) -> Unit
) : RecyclerView.Adapter<SinhVienAdapter.SinhVienViewHolder>() {

    inner class SinhVienViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgAvatar: ImageView = itemView.findViewById(R.id.imgAvatar)
        val txtAvatar: TextView = itemView.findViewById(R.id.txtAvatar)
        val txtHoTen: TextView = itemView.findViewById(R.id.txtHoTen)
        val txtMSSV: TextView = itemView.findViewById(R.id.txtMSSV)
        val txtEmail: TextView = itemView.findViewById(R.id.txtEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SinhVienViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sinh_vien, parent, false)
        return SinhVienViewHolder(view)
    }

    override fun onBindViewHolder(holder: SinhVienViewHolder, position: Int) {
        val sv = danhSach[position]
        holder.txtHoTen.text = sv.hoTen
        holder.txtMSSV.text = sv.mssv
        holder.txtEmail.text = sv.email

        // Nếu có link ảnh -> hiển thị ảnh, ngược lại hiển thị chữ cái đầu
        if (sv.anhAvatar.isNotEmpty()) {
            // Ẩn chữ cái, hiện ImageView
            holder.txtAvatar.visibility = View.GONE
            holder.imgAvatar.visibility = View.VISIBLE

            // Load ảnh bằng Glide
            Glide.with(holder.itemView.context)
                .load(sv.anhAvatar)
                .placeholder(R.drawable.bg_avatar_placeholder)
                .error(R.drawable.bg_avatar_placeholder)
                .into(holder.imgAvatar)
        } else {
            // Ẩn ImageView, hiện chữ cái
            holder.imgAvatar.visibility = View.GONE
            holder.txtAvatar.visibility = View.VISIBLE

            if (sv.hoTen.isNotEmpty()) {
                holder.txtAvatar.text = sv.hoTen[0].toString().uppercase()
            } else {
                holder.txtAvatar.text = "?"
            }
        }

        holder.itemView.setOnClickListener {
            onItemClick(sv)
        }
    }

    override fun getItemCount(): Int = danhSach.size
}