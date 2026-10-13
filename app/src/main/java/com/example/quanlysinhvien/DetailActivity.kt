package com.example.quanlysinhvien

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var sinhVien: SinhVien? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        dbHelper = DatabaseHelper(this)

        // Lấy id sinh viên được truyền từ MainActivity
        val id = intent.getIntExtra("id_sinh_vien", -1)
        if (id == -1) {
            Toast.makeText(this, "Không tìm thấy sinh viên", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Lấy thông tin sinh viên từ CSDL
        sinhVien = dbHelper.getSinhVienById(id)

        // Hiển thị thông tin lên giao diện
        sinhVien?.let { sv ->
            findViewById<TextView>(R.id.txtHoTenDetail).text = sv.hoTen
            findViewById<TextView>(R.id.txtMSSVDetail).text = sv.mssv
            findViewById<TextView>(R.id.txtEmailDetail).text = sv.email
            // TODO: Load ảnh avatar
            findViewById<ImageView>(R.id.imgAvatarDetail)
                .setImageResource(android.R.drawable.ic_menu_gallery)
        }

        // Nút "Sửa"
        findViewById<Button>(R.id.btnSua).setOnClickListener {
            // Hiển thị hộp thoại xác nhận
            AlertDialog.Builder(this)
                .setMessage(R.string.xac_nhan_sua)
                .setPositiveButton(R.string.co) { _, _ ->
                    // Mở AddEditActivity ở chế độ SỬA (truyền id)
                    val intent = Intent(this, AddEditActivity::class.java)
                    intent.putExtra("id_sinh_vien", sinhVien?.id)
                    startActivity(intent)
                    finish() // Đóng DetailActivity để khi quay lại sẽ thấy dữ liệu mới
                }
                .setNegativeButton(R.string.khong, null)
                .show()
        }

        // Nút "Xóa"
        findViewById<Button>(R.id.btnXoa).setOnClickListener {
            // Hiển thị hộp thoại xác nhận
            AlertDialog.Builder(this)
                .setMessage(R.string.xac_nhan_xoa)
                .setPositiveButton(R.string.co) { _, _ ->
                    sinhVien?.let {
                        dbHelper.deleteSinhVien(it.id)
                        Toast.makeText(this, "Đã xóa sinh viên", Toast.LENGTH_SHORT).show()
                        finish() // Đóng DetailActivity, quay về MainActivity
                    }
                }
                .setNegativeButton(R.string.khong, null)
                .show()
        }
    }
}