package com.local.growkart.dashboard.profile

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.local.growkart.dashboard.model.User
import com.local.growkart.util.DatabaseUtil
import com.local.growkart.util.Error
import com.local.growkart.util.ToastUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PersonalDetailViewModel @Inject constructor(
    application: Application,
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val currentUser: FirebaseUser?
) : AndroidViewModel(application) {

    var isEdit = MutableLiveData<Boolean>()
    val user = MutableLiveData<User>()
    val error = MutableLiveData<Error>()
    val status = MutableLiveData<Status>()

    fun loadPersonalDetail() {
        if (currentUser != null) // logged in
            db.collection(DatabaseUtil.FireStore.Collections.PROFILE)
                .document(currentUser.phoneNumber ?: "")
                .get()
                .addOnSuccessListener { snapShot ->
                    var remoteUser: User
                    try {
                        remoteUser = snapShot.toObject<User>()!!
                    } catch (e: Throwable) {
                        remoteUser = User()
                    }
                    user.postValue(remoteUser)
                }
                .addOnFailureListener {
                    error.value = Error.FETCH_FAILURE
                }
    }

    fun onEdit(view: View) {
        isEdit.postValue(true)
    }

    fun onSave(view: View) {
        if (currentUser != null) {
            user.value?.apply {
                custId = currentUser.phoneNumber ?: ""
                db.collection(DatabaseUtil.FireStore.Collections.PROFILE)
                    .document(currentUser.phoneNumber ?: "")
                    .delete()
                db.collection(DatabaseUtil.FireStore.Collections.PROFILE)
                    .document(currentUser.phoneNumber ?: "")
                    .set(user.value ?: User())
                    .addOnSuccessListener {
                        isEdit.value = false
                        status.value = Status.SUCCESS
                        loadPersonalDetail()
                    }
                    .addOnFailureListener {
                        error.value = Error.UPDATE_FAILURE
                    }
            }
        } else {
            ToastUtil.showErrorInfo(getApplication(), "You are not Logged in yet. Please try Login")
        }
    }

    fun signOut(onSignOut: () -> Unit) {
        if (auth.currentUser != null) {
            auth.signOut()
            auth.addAuthStateListener {
                if (it.currentUser == null)
                    onSignOut()
            }
        } else
            onSignOut()
    }

    fun isUserLoggedIn() = auth.currentUser != null

    fun onGenderSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        val updatedUser = user.value?.apply {
            gender = when (pos) {
                1 -> "Male"
                2 -> "Female"
                else -> "Not Specified"
            }
        }
        user.value = updatedUser
    }
}


enum class Status {
    SUCCESS, ERROR, PENDING
}