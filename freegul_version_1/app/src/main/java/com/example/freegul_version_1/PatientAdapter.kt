package com.example.freegul_version_1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.freegul_version_1.databinding.ItemPatientBinding

class PatientAdapter(private val patientList: List<Patient>) : RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    class PatientViewHolder(private val binding: ItemPatientBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(patient: Patient) {
//            binding.tvDateTime.text = patient.Tanggal
            binding.tvName.text = patient.Nama_Lengkap
            binding.tvAge.text = "Umur: " + patient.Usia.toString()
            binding.tvBloodSugar.text = patient.Nilai_Gula_Darah.toString() + "mg/dl"
            binding.tvPhoneNumber.text = "No. HP: " + patient.No_Handphone
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val binding = ItemPatientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PatientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        holder.bind(patientList[position])
    }

    override fun getItemCount(): Int = patientList.size
}
