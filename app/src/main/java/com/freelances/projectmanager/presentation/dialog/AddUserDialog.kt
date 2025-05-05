package com.freelances.projectmanager.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.freelances.projectmanager.R
import com.freelances.projectmanager.databinding.DialogAddItemBinding
import com.freelances.projectmanager.model.Personal
import com.freelances.projectmanager.presentation.bases.BaseDialogFragment
import com.freelances.projectmanager.utils.ext.generateUniqueId
import com.freelances.projectmanager.utils.ext.safeClick
import com.freelances.projectmanager.utils.ext.showToast

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
    override fun updateUI(savedInstanceState: Bundle?) {
        binding.btnAdd.safeClick {
            if (checkEmpty()) {
                requireContext().showToast("input not blank !")
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

    private fun checkEmpty(): Boolean {
        val name = binding.etName.text.toString().trim()
        val date = binding.etDate.text.toString().trim()
        val sex = binding.etSex.text.toString().trim()
        val empty =
            name.isEmpty() || date.isEmpty() || sex.isEmpty()
        return empty
    }
}