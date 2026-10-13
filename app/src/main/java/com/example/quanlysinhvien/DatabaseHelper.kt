package com.example.quanlysinhvien

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "QuanLySinhVien.db"
        private const val DATABASE_VERSION = 1

        // Tên bảng và các cột
        const val TABLE_NAME = "sinh_vien"
        const val COL_ID = "id"
        const val COL_HO_TEN = "ho_ten"
        const val COL_MSSV = "mssv"
        const val COL_EMAIL = "email"
        const val COL_ANH_AVATAR = "anh_avatar"
    }

    // Hàm này được gọi khi database được tạo lần đầu
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_HO_TEN TEXT, " +
                "$COL_MSSV TEXT, " +
                "$COL_EMAIL TEXT, " +
                "$COL_ANH_AVATAR TEXT)"
        db?.execSQL(createTable)
    }

    // Hàm này được gọi khi nâng cấp version database
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // === CÁC HÀM CRUD ===

    // 1. THÊM sinh viên
    fun insertSinhVien(sv: SinhVien): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_HO_TEN, sv.hoTen)
            put(COL_MSSV, sv.mssv)
            put(COL_EMAIL, sv.email)
            put(COL_ANH_AVATAR, sv.anhAvatar)
        }
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    // 2. LẤY TẤT CẢ sinh viên
    fun getAllSinhVien(): List<SinhVien> {
        val list = mutableListOf<SinhVien>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val sv = SinhVien(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    hoTen = cursor.getString(cursor.getColumnIndexOrThrow(COL_HO_TEN)),
                    mssv = cursor.getString(cursor.getColumnIndexOrThrow(COL_MSSV)),
                    email = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)),
                    anhAvatar = cursor.getString(cursor.getColumnIndexOrThrow(COL_ANH_AVATAR))
                )
                list.add(sv)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    // 3. LẤY 1 sinh viên theo ID
    fun getSinhVienById(id: Int): SinhVien? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COL_ID = ?", arrayOf(id.toString()))
        var sv: SinhVien? = null

        if (cursor.moveToFirst()) {
            sv = SinhVien(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                hoTen = cursor.getString(cursor.getColumnIndexOrThrow(COL_HO_TEN)),
                mssv = cursor.getString(cursor.getColumnIndexOrThrow(COL_MSSV)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)),
                anhAvatar = cursor.getString(cursor.getColumnIndexOrThrow(COL_ANH_AVATAR))
            )
        }
        cursor.close()
        db.close()
        return sv
    }

    // 4. SỬA sinh viên
    fun updateSinhVien(sv: SinhVien): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_HO_TEN, sv.hoTen)
            put(COL_MSSV, sv.mssv)
            put(COL_EMAIL, sv.email)
            put(COL_ANH_AVATAR, sv.anhAvatar)
        }
        val rowsAffected = db.update(TABLE_NAME, values, "$COL_ID = ?", arrayOf(sv.id.toString()))
        db.close()
        return rowsAffected
    }

    // 5. XÓA sinh viên
    fun deleteSinhVien(id: Int): Int {
        val db = writableDatabase
        val rowsAffected = db.delete(TABLE_NAME, "$COL_ID = ?", arrayOf(id.toString()))
        db.close()
        return rowsAffected
    }
}