package com.example.quanlysinhvien

data class SinhVien(
    var id: Int = 0,           // ID tự tăng (khóa chính)
    var hoTen: String = "",    // Họ tên sinh viên
    var mssv: String = "",     // Mã số sinh viên
    var email: String = "",    // Email
    var anhAvatar: String = "" // Đường dẫn ảnh hoặc link ảnh
)