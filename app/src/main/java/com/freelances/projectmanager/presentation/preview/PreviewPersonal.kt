package com.freelances.projectmanager.presentation.preview

import com.freelances.projectmanager.databinding.ActivityPreviewBinding
import com.freelances.projectmanager.model.Personal
import com.freelances.projectmanager.presentation.bases.BaseActivity
import com.freelances.projectmanager.utils.constant.KEY_DATA
import com.freelances.projectmanager.utils.ext.safeClick
import com.freelances.projectmanager.utils.ext.showToast
import com.freelances.projectmanager.utils.helper.PersonalHelper
import org.koin.core.component.inject

class PreviewPersonal : BaseActivity<ActivityPreviewBinding>(ActivityPreviewBinding::inflate) {

    private val itemPreview by lazy {
        intent.extras?.getParcelable<Personal>(KEY_DATA)
    }

    override fun isDisplayCutout(): Boolean {
        return true
    }
    private val personalHelper: PersonalHelper by inject()

    override fun initViews() {
        itemPreview?.let {
            binding.etName.setText(it.name)
            binding.etSex.setText(it.sex)
            binding.etDate.setText(it.date)
        }

        binding.buttonBack.safeClick {
            finish()
        }

        binding.btnUpdate.safeClick {
            if (checkEmpty()) {
                showToast("input not blank !")
                return@safeClick
            }
            if (checkSameOldUser()) {
                showToast("information duplicates old information")
                return@safeClick
            }

            val name = binding.etName.text.toString().trim()
            val date = binding.etDate.text.toString().trim()
            val sex = binding.etSex.text.toString().trim()
            val idUser = itemPreview?.idPersonal.toString().trim()
            val personal = Personal(idPersonal = idUser, name = name, date = date, sex = sex)
            personalHelper.updatePersonal(personal) { success ->
                if (success) {
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }
    }

    private fun checkSameOldUser(): Boolean {
        val name = binding.etName.text.toString().trim()
        val date = binding.etDate.text.toString().trim()
        val sex = binding.etSex.text.toString().trim()
        val same = itemPreview?.name == name && itemPreview?.sex == sex && itemPreview?.date == date
        return same
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