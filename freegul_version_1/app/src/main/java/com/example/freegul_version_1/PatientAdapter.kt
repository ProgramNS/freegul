package com.example.freegul_version_1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.freegul_version_1.databinding.ItemPatientBinding
class PatientAdapter(private val patients: List<Patient>) : RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    class PatientViewHolder(val binding: ItemPatientBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val binding = ItemPatientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PatientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val patient = patients[position]
        holder.binding.tvDateTime.text = patient.dateTime
        holder.binding.tvName.text = patient.name
        holder.binding.tvAge.text = "${patient.age} tahun"
        holder.binding.tvBloodSugar.text = "${patient.bloodSugar} mg/dl"
        holder.binding.tvPhoneNumber.text = patient.phoneNumber // Menampilkan nomor telepon jika ada
    }

    override fun getItemCount(): Int {
        return patients.size
    }
}
