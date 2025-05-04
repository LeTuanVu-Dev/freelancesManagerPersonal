package com.freelances.projectmanager.utils.helper

import com.freelances.projectmanager.model.Personal
import com.freelances.projectmanager.utils.constant.Personals
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PersonalHelper {

    private val database = FirebaseDatabase.getInstance().getReference(Personals)

    fun addPersonal(personal: Personal, onComplete: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val key = database.push().key
            if (key == null) {
                onComplete(false)
                return@launch
            }
            val newPersonal = personal.copy(idPersonal = key)
            database.child(key).setValue(newPersonal)
                .addOnCompleteListener { task ->
                    onComplete(task.isSuccessful)
                }
        }
    }

    fun addPersonalList(personalList: List<Personal>, onComplete: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            var successCount = 0

            personalList.forEach { personal ->
                val key = database.push().key ?: return@forEach
                val newPersonal = personal.copy(idPersonal = key)

                database.child(key).setValue(newPersonal)
                    .addOnSuccessListener {
                        successCount++
                        if (successCount == personalList.size) {
                            onComplete(true)
                        }
                    }
                    .addOnFailureListener {
                        onComplete(false)
                    }
            }
        }
    }

    fun updatePersonal(personal: Personal, onComplete: (Boolean) -> Unit) {
        if (personal.idPersonal.isEmpty()) return onComplete(false)
        CoroutineScope(Dispatchers.IO).launch {
            database.child(personal.idPersonal).setValue(personal)
                .addOnCompleteListener { task ->
                    onComplete(task.isSuccessful)
                }
        }
    }

    fun deletePersonal(idPersonal: String, onComplete: (Boolean) -> Unit) {
        if (idPersonal.isEmpty()) return onComplete(false)
        CoroutineScope(Dispatchers.IO).launch {
            database.child(idPersonal).removeValue()
                .addOnCompleteListener { task ->
                    onComplete(task.isSuccessful)
                }
        }
    }

    fun getAllPersonals(onDataReceived: (List<Personal>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = snapshot.children.mapNotNull { it.getValue(Personal::class.java) }
                    onDataReceived(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    onDataReceived(emptyList())
                }
            })
        }
    }

    fun addRandomUsers(count: Int = 10, onComplete: (Boolean) -> Unit) {
        val names = listOf("An", "Bình", "Chi", "Dũng", "Hà", "Khanh", "Linh", "Minh", "Ngọc", "Phúc")
        val sexes = listOf("Nam", "Nữ")
        var successCount = 0

        repeat(count) {
            val name = names.random()
            val sex = sexes.random()
            val date = "${(1..28).random().toString().padStart(2, '0')}/0${(1..9).random()}/200${(0..9).random()}"
            val key = database.push().key ?: return@repeat
            val user = Personal(idPersonal = key, name = name, date = date, sex = sex)

            database.child(key).setValue(user)
                .addOnSuccessListener {
                    successCount++
                    if (successCount == count) {
                        onComplete(true)
                    }
                }
                .addOnFailureListener {
                    onComplete(false)
                }
        }
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
