package com.example.demo.file

data class DownloadFile(
    val fileName: String,
    val contentType: String,
    val bytes: ByteArray
)