package com.example.freegul_version_1

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.freegul_version_1.databinding.ItemPatientBinding

class PatientAdapter(
    private val patientList: List<Patient>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    inner class PatientViewHolder(private val binding: ItemPatientBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        @SuppressLint("SetTextI18n")
        fun bind(patient: Patient) {
            binding.tvNik.text = patient.NIK
            binding.tvName.text = patient.Nama_Lengkap
            binding.tvAge.text = "Umur: " + patient.Usia
            binding.tvDateTime.text = patient.Tanggal_Pembuatan
            binding.tvPhoneNumber.text = "No. HP: " + patient.No_Handphone
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
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
