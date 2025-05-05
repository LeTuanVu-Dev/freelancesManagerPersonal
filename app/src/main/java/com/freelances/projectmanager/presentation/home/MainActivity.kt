package com.freelances.`21A100100413_NgoAnhTuan_05052025`.presentation.home

import android.app.Activity
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.result.ActivityResult
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.freelances.projectmanager.databinding.ActivityMainBinding
import com.freelances.projectmanager.databinding.LayoutMoreBinding
import com.freelances.projectmanager.model.Personal
import com.freelances.projectmanager.presentation.adapters.PersonAdapter
import com.freelances.projectmanager.presentation.bases.BaseActivity
import com.freelances.`21A100100413_NgoAnhTuan_05052025`.presentation.dialog.AddUserDialog
import com.freelances.projectmanager.presentation.preview.PreviewPersonal
import com.freelances.projectmanager.utils.constant.KEY_DATA
import com.freelances.`21A100100413_NgoAnhTuan_05052025`.utils.ext.gone
import com.freelances.projectmanager.utils.ext.safeClick
import com.freelances.projectmanager.utils.ext.showToast
import com.freelances.`21A100100413_NgoAnhTuan_05052025`.utils.helper.PersonalHelper
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val personalHelper: PersonalHelper by inject()

    private val personalAdapter: PersonAdapter by lazy {
        PersonAdapter(::handleClickItem) { view, item ->
            showPopupMore(view, item)
        }
    }
    private var filteredList = arrayListOf<Personal>()
    private var listData = arrayListOf<Personal>()

    override fun isDisplayCutout(): Boolean {
        return true
    }

    override fun handleActivityResult(result: ActivityResult) {
        super.handleActivityResult(result)
        if (result.resultCode == Activity.RESULT_OK) {
            getAll()
        }
    }

    private fun handleClickItem(item: Personal) {
        this@MainActivity.navigateForResult(
            PreviewPersonal::class.java,
            bundle = bundleOf(
                KEY_DATA to item
            )
        )
    }

    override fun initViews() {
        lifecycleScope.launch {
//            addRandom()
        }
        getAll()
        initAction()
    }

    private fun setUpData(data: List<Personal>) {
        binding.recyclerView.apply {
            adapter = personalAdapter
        }
        personalAdapter.submitList(data)
    }

    private fun getAll() {
        lifecycleScope.launch {
            val data = personalHelper.getAllPersonals()
            Log.d(TAG, "getAll: $data")
            listData.clear()
            listData.addAll(data.reversed())
            setUpData(listData)
            binding.frLoading.gone()
        }
    }


    private fun showAddDialog() {
        val dialogInputNewDataListener = AddUserDialog.newInstance { item ->
            lifecycleScope.launch {
                if (personalHelper.addPersonal(item)){
                    getAll()
                }
            }
        }
        dialogInputNewDataListener.show(
            supportFragmentManager,
            "showAddDialog"
        )
    }

    private fun initAction() {
        binding.apply {

            frAdd.safeClick {
                showAddDialog()
            }

            edtInputSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    filterWithSearch(s.toString().trim())
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
        }
    }

    private fun filterWithSearch(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(listData)
        } else {
            val lowerCaseQuery = query.lowercase()
            for (item in listData) {
                if (item.name.lowercase().contains(lowerCaseQuery)) {
                    filteredList.add(item)
                }
            }
        }
        personalAdapter.submitList(filteredList.toMutableList())
    }

    private suspend fun addRandom() {
       val success =  personalHelper.addRandomUsers(10)
    }

    private fun showPopupMore(view: View, item: Personal) {
        val layoutInflater = LayoutInflater.from(this)
        val binding1 = LayoutMoreBinding.inflate(layoutInflater)
        val popupMenu = PopupWindow(this)
        popupMenu.contentView = binding1.root
        popupMenu.width = LinearLayout.LayoutParams.WRAP_CONTENT
        popupMenu.height = LinearLayout.LayoutParams.WRAP_CONTENT
        popupMenu.isFocusable = true
        popupMenu.isOutsideTouchable = true
        popupMenu.elevation = 100f

        popupMenu.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Đo kích thước của PopupWindow để tính toán chiều cao cần thiết
        binding1.root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val popupHeight = binding1.root.measuredHeight

        // Lấy vị trí của view gốc trên màn hình
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val yPos = location[1] + view.height // Vị trí y của view gốc
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        // Kiểm tra nếu không gian phía dưới không đủ để hiển thị toàn bộ PopupWindow
        if (yPos + popupHeight > screenHeight) {
            // Hiển thị PopupWindow phía trên view gốc nếu không đủ không gian bên dưới
            popupMenu.showAsDropDown(view, -200, -(popupHeight + view.height), Gravity.NO_GRAVITY)
        } else {
            // Hiển thị PopupWindow phía dưới view gốc nếu đủ không gian
            popupMenu.showAsDropDown(view, -200, 30, Gravity.NO_GRAVITY)
        }

        binding1.lnRemove.safeClick {
            lifecycleScope.launch {
                val delete = personalHelper.deletePersonal(item.maNv)
                if (delete){
                    val newList = listData.toMutableList().apply {
                        removeIf { it.maNv == item.maNv } // dùng điều kiện rõ ràng
                    }
                    listData = newList as ArrayList<Personal> // Cập nhật lại list gốc nếu bạn giữ bên ngoài adapter

                    runOnUiThread {
                        personalAdapter.submitList(newList)
                        showToast("delete done!")
                        binding.frLoading.gone()
                    }
                }
            }
            popupMenu.dismiss()
        }
        popupMenu.showAsDropDown(view, -200, 30, Gravity.NO_GRAVITY)
    }
}