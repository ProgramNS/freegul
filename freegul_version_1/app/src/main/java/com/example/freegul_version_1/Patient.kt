package com.example.freegul_version_1

import java.io.Serializable

data class Patient(
    val Jenis_Kelamin: String = "",
    val NIK: String = "",
    val Nama_Lengkap: String = "",
    val No_Handphone: String = "",
    val Tanggal_Pembuatan: String = "",
    val Usia: Int = 0,
    val Data_Gula_Darah: Map<String, Glucose> = emptyMap()
): Serializable

data class Glucose(
    val Nilai_Gula_Darah: Double = 0.0,
    val Tanggal_Pengecekan: String = ""
): Serializable
