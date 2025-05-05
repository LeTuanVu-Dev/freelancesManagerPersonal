package com.freelances.`21A100100413_NgoAnhTuan_05052025`.utils.helper

import com.freelances.projectmanager.model.Personal
import com.freelances.projectmanager.utils.constant.Personals
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PersonalHelper {

    private val database = FirebaseDatabase.getInstance().getReference(Personals)

    suspend fun addPersonal(personal: Personal): Boolean = withContext(Dispatchers.IO) {
        val key = database.push().key ?: return@withContext false
        val newPersonal = personal.copy(maNv = key)
        try {
            database.child(key).setValue(newPersonal).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun addPersonalList(personalList: List<Personal>): Boolean = withContext(Dispatchers.IO) {
        try {
            personalList.forEach { personal ->
                val key = database.push().key ?: return@withContext false
                val newPersonal = personal.copy(maNv = key)
                database.child(key).setValue(newPersonal).await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updatePersonal(personal: Personal): Boolean = withContext(Dispatchers.IO) {
        if (personal.maNv.isEmpty()) return@withContext false
        try {
            database.child(personal.maNv).setValue(personal).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deletePersonal(idPersonal: String): Boolean = withContext(Dispatchers.IO) {
        if (idPersonal.isEmpty()) return@withContext false
        try {
            database.child(idPersonal).removeValue().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getAllPersonals(): List<Personal> = withContext(Dispatchers.IO) {
        try {
            val snapshot = database.get().await()
            snapshot.children.mapNotNull { it.getValue(Personal::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addRandomUsers(count: Int = 10): Boolean = withContext(Dispatchers.IO) {
        val names = listOf("An", "Bình", "Chi", "Dũng", "Hà", "Khanh", "Linh", "Minh", "Ngọc", "Phúc")
        val sexes = listOf("Nam", "Nữ")
        val positions = listOf("Nhân viên", "Quản lý", "Trưởng phòng", "Kế toán", "Thư ký")

        try {
            repeat(count) {
                val name = names.random()
                val sex = sexes.random()
                val chucVu = positions.random()
                val hsl = (1.5 + Math.random() * 3.5).toString().take(4)
                val lcb = ((2_000_000..10_000_000).random()).toString()
                val date = "${(1..28).random().toString().padStart(2, '0')}/0${(1..9).random()}/200${(0..9).random()}"
                val key = database.push().key ?: return@withContext false

                val user = Personal(
                    maNv = key,
                    name = name,
                    date = date,
                    sex = sex,
                    chucVu = chucVu,
                    hsl = hsl,
                    lcb = lcb
                )
                database.child(key).setValue(user).await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        val instance by lazy { PersonalHelper() }
    }

}


/**
 * // Thêm
 * val person = Personal(name = "Nguyen Van A", date = "01/01/2000", sex = "Nam")
 * PersonalHelper.addPersonal(person) { success ->
 *     if (success) Log.d("Firebase", "Thêm thành công")
 * }
 *
 * // Sửa
 * val updated = person.copy(name = "Nguyen Van B")
 * PersonalHelper.updatePersonal(updated) { success ->
 *     if (success) Log.d("Firebase", "Cập nhật thành công")
 * }
 *
 * // Xóa
 * PersonalHelper.deletePersonal(person.idPersonal) { success ->
 *     if (success) Log.d("Firebase", "Xóa thành công")
 * }
 *
 * // Lấy tất cả
 * PersonalHelper.getAllPersonals { list ->
 *     list.forEach { Log.d("Firebase", it.toString()) }
 * }
 * */
