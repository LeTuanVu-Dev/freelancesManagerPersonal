package com.freelances.projectmanager.presentation.dialog

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.freelances.projectmanager.R
import com.freelances.projectmanager.databinding.DialogAddItemBinding
import com.freelances.projectmanager.model.Personal
import com.freelances.projectmanager.presentation.bases.BaseDialogFragment
import com.freelances.projectmanager.utils.ext.generateUniqueId
import com.freelances.projectmanager.utils.ext.safeClick
import com.freelances.projectmanager.utils.ext.showToast
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class AddUserDialog : BaseDialogFragment<DialogAddItemBinding>() {
    private var submitAction: ((Personal) -> Unit)? = null

    companion object {
        fun newInstance(
            submitAction: ((Personal) -> Unit)
        ): AddUserDialog {
            val dialog = AddUserDialog()
            dialog.submitAction = submitAction
            return dialog
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogAddItemBinding {
        return DialogAddItemBinding.inflate(inflater, container, false)
    }

    override fun getLayout(): Int {
        return R.layout.dialog_add_item
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun updateUI(savedInstanceState: Bundle?) {
        binding.btnAdd.safeClick {
            if (checkEmpty()) {
                requireContext().showToast("input not blank !")
                return@safeClick
            }

            if (!checkDate()){
                requireContext().showToast("date need >=18t !")
                return@safeClick
            }
            val name = binding.etName.text.toString().trim()
            val date = binding.etDate.text.toString().trim()
            val sex = binding.etSex.text.toString().trim()
            val idUser = generateUniqueId()
            val user = Personal(maNv = idUser, name = name, date = date, sex = sex)
            submitAction?.invoke(user)
            dismiss()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkDate(): Boolean {
        val dateStr = binding.etDate.text.toString().trim()
        if (dateStr.isEmpty()) return false

        return try {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val birthDate = LocalDate.parse(dateStr, formatter)
            val today = LocalDate.now()
            val age = Period.between(birthDate, today).years

            age >= 18
        } catch (e: Exception) {
            false // sai định dạng hoặc lỗi parse
        }
    }


    private fun checkEmpty(): Boolean {
        val name = binding.etName.text.toString().trim()
        val date = binding.etDate.text.toString().trim()
        val sex = binding.etSex.text.toString().trim()
        val chucVu = binding.etChucVu.text.toString().trim()
        val hsl = binding.etHsl.text.toString().trim()
        val lcb = binding.etLcb.text.toString().trim()
        val empty =
            name.isEmpty() || date.isEmpty() || sex.isEmpty() || chucVu.isEmpty() || hsl.isEmpty() || lcb.isEmpty()
        return empty
    }
}