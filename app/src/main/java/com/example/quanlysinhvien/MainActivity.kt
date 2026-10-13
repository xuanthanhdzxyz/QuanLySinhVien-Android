package com.example.quanlysinhvien

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: SinhVienAdapter
    private lateinit var recyclerView: RecyclerView
    private val danhSach = mutableListOf<SinhVien>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewSinhVien)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = SinhVienAdapter(danhSach) { sv ->
            showChiTietDialog(sv)
        }
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.btnThemSinhVien).setOnClickListener {
            val intent = Intent(this, AddEditActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadDanhSach()
    }

    private fun loadDanhSach() {
        danhSach.clear()
        danhSach.addAll(dbHelper.getAllSinhVien())
        adapter.notifyDataSetChanged()
    }

    private fun showChiTietDialog(sv: SinhVien) {
        // Tạo view tùy chỉnh cho Dialog
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_chi_tiet, null)

        val imgAvatar = dialogView.findViewById<ImageView>(R.id.imgAvatarDialog)
        val txtHoTen = dialogView.findViewById<TextView>(R.id.txtHoTenDialog)
        val txtMSSV = dialogView.findViewById<TextView>(R.id.txtMSSVDialog)
        val txtEmail = dialogView.findViewById<TextView>(R.id.txtEmailDialog)

        // Gán dữ liệu
        txtHoTen.text = sv.hoTen
        txtMSSV.text = sv.mssv
        txtEmail.text = sv.email

        // Load ảnh nếu có
        if (sv.anhAvatar.isNotEmpty()) {
            Glide.with(this)
                .load(sv.anhAvatar)
                .placeholder(R.drawable.bg_avatar_placeholder)
                .error(R.drawable.bg_avatar_placeholder)
                .into(imgAvatar)
        } else {
            // Nếu không có ảnh, hiển thị placeholder
            imgAvatar.setImageResource(R.drawable.bg_avatar_placeholder)
        }

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton(R.string.sua) { _, _ ->
                AlertDialog.Builder(this)
                    .setMessage(R.string.xac_nhan_sua)
                    .setPositiveButton(R.string.co) { _, _ ->
                        val intent = Intent(this, AddEditActivity::class.java)
                        intent.putExtra("id_sinh_vien", sv.id)
                        startActivity(intent)
                    }
                    .setNegativeButton(R.string.khong, null)
                    .show()
            }
            .setNegativeButton(R.string.xoa) { _, _ ->
                AlertDialog.Builder(this)
                    .setMessage(R.string.xac_nhan_xoa)
                    .setPositiveButton(R.string.co) { _, _ ->
                        dbHelper.deleteSinhVien(sv.id)
                        loadDanhSach()
                    }
                    .setNegativeButton(R.string.khong, null)
                    .show()
            }
            .setNeutralButton(R.string.dong, null)
            .show()
    }
}