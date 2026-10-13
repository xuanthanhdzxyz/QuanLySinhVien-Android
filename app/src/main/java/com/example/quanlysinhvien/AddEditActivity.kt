package com.example.quanlysinhvien

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class AddEditActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var sinhVien: SinhVien? = null
    private var isEditMode = false
    private lateinit var imgAvatar: ImageView
    private lateinit var edtAnhAvatar: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        dbHelper = DatabaseHelper(this)
        imgAvatar = findViewById(R.id.imgAvatar)
        edtAnhAvatar = findViewById(R.id.edtAnhAvatar)

        val id = intent.getIntExtra("id_sinh_vien", -1)

        if (id != -1) {
            isEditMode = true
            sinhVien = dbHelper.getSinhVienById(id)

            findViewById<TextView>(R.id.txtTieuDe).text = getString(R.string.sua_sinh_vien)

            sinhVien?.let { sv ->
                findViewById<EditText>(R.id.edtHoTen).setText(sv.hoTen)
                findViewById<EditText>(R.id.edtMSSV).setText(sv.mssv)
                findViewById<EditText>(R.id.edtEmail).setText(sv.email)
                edtAnhAvatar.setText(sv.anhAvatar)

                // Hiển thị ảnh cũ nếu có
                if (sv.anhAvatar.isNotEmpty()) {
                    loadAnhAvatar(sv.anhAvatar)
                }
            }
        } else {
            findViewById<TextView>(R.id.txtTieuDe).text = getString(R.string.them_sinh_vien)
        }

        // === NÚT QUAY LẠI ===
        findViewById<ImageView>(R.id.btnQuayLai).setOnClickListener {
            finish() // Đóng Activity, quay về trang trước (MainActivity)
        }

        // === TỰ ĐỘNG LOAD ẢNH KHI NHẬP LINK ===
        // Khi người dùng rời khỏi ô nhập link (mất focus), ảnh sẽ được load
        edtAnhAvatar.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val linkAnh = edtAnhAvatar.text.toString().trim()
                if (linkAnh.isNotEmpty()) {
                    loadAnhAvatar(linkAnh)
                }
            }
        }

        // === NÚT LƯU ===
        findViewById<Button>(R.id.btnLuu).setOnClickListener {
            val hoTen = findViewById<EditText>(R.id.edtHoTen).text.toString().trim()
            val mssv = findViewById<EditText>(R.id.edtMSSV).text.toString().trim()
            val email = findViewById<EditText>(R.id.edtEmail).text.toString().trim()
            val anhAvatar = edtAnhAvatar.text.toString().trim()

            if (hoTen.isEmpty() || mssv.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, R.string.nhap_day_du, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isEditMode) {
                AlertDialog.Builder(this)
                    .setMessage(R.string.xac_nhan_sua)
                    .setPositiveButton(R.string.co) { _, _ ->
                        sinhVien?.let {
                            it.hoTen = hoTen
                            it.mssv = mssv
                            it.email = email
                            it.anhAvatar = anhAvatar
                            dbHelper.updateSinhVien(it)
                            Toast.makeText(this, R.string.sua_thanh_cong, Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                    .setNegativeButton(R.string.khong, null)
                    .show()
            } else {
                val sv = SinhVien(
                    hoTen = hoTen,
                    mssv = mssv,
                    email = email,
                    anhAvatar = anhAvatar
                )
                dbHelper.insertSinhVien(sv)
                Toast.makeText(this, R.string.them_thanh_cong, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    // Hàm load ảnh từ link bằng Glide
    private fun loadAnhAvatar(url: String) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.bg_avatar_placeholder)
            .error(R.drawable.bg_avatar_placeholder)
            .into(imgAvatar)
    }
}