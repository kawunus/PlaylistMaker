package com.example.playlistmaker.data.file_manager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

class FileManager(private val context: Context) {

    fun saveCoverToPrivateStorage(uri: Uri, fileName: String): String {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "covers")

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, fileName)

        val inputStream = context.contentResolver.openInputStream(uri)

        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.path
    }

    fun deleteCoverFromLocalStorage(fileName: String) {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "covers")
        val file = File(filePath, fileName)
        if (file.exists()) {
            file.delete()
        }
    }
}