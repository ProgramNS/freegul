package com.example.freegul_version_1

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.freegul_version_1.databinding.SearchPatientBinding

class SearchAdapter(
    private var patientList: List<Patient>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SearchAdapter.PatientViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(patient: Patient)
    }

    inner class PatientViewHolder(private val binding: SearchPatientBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(patient: Patient) {
            binding.nik.text = "NIK : " + patient.NIK
            binding.namaLengkap.text = "Nama Lengkap : " + patient.Nama_Lengkap
            binding.hp.text = "No HP : " + patient.No_Handphone
            binding.usia.text = "Usia : " + patient.Usia.toString()
            binding.pembuatan.text = "Tanggal Pembuatan : " + patient.Tanggal_Pembuatan
            binding.root.setOnClickListener {
                listener.onItemClick(patient)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val binding = SearchPatientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PatientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        holder.bind(patientList[position])
    }

    override fun getItemCount(): Int {
        return patientList.size
    }
}
