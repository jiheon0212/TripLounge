package com.myproject.triplounge.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.myproject.triplounge.data.UserDataClass

class UserRepository {

    // todo : uid를 저장해서 사용자 db를 구축하고 내부에 사용자 전화번호 & 닉네임을 설정해서 mapping한다.
    companion object {

        fun createUser(id: String, pw: String, callback: (Task<AuthResult>) -> Unit) {
            val auth = FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(id, pw).addOnCompleteListener(callback)
        }
        fun loginUser(id: String, pw: String, callback: (Task<AuthResult>) -> Unit) {
            val auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(id, pw).addOnCompleteListener(callback)
        }

        fun getUserIdx(callback: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val userIdxRef = database.getReference("UserIdx")

            userIdxRef.get().addOnCompleteListener(callback)
        }
        fun setUserIdx(userIdx: Long, callback: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val userIdxRef = database.getReference("UserIdx")

            userIdxRef.get().addOnCompleteListener {
                it.result.ref.setValue(userIdx).addOnCompleteListener(callback)
            }
        }
        fun addUserInfo(userDataClass: UserDataClass, callback: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")

            userDataRef.push().setValue(userDataClass).addOnCompleteListener(callback)
        }
        // todo : modify 시 UserData uid가 중첩되어 생겨나는 현상 해결
        fun modifyUserInfo(userDataClass: UserDataClass, callback: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")

            userDataRef.orderByChild("userUid").equalTo(userDataClass.userUid).get().addOnCompleteListener {
                for (i in it.result.children) {
                    i.ref.removeValue()
                }
                it.result.ref.push().setValue(userDataClass).addOnCompleteListener(callback)
            }
        }
        fun deleteUserInfo() {
            
        }
        fun getUserInfo(uid: String?, callback: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")

            userDataRef.orderByChild("userUid").equalTo(uid).get().addOnCompleteListener(callback)
        }
        fun getUserUid(): String? {
            val auth = FirebaseAuth.getInstance()
            val uid = auth.uid

            return uid
        }
        fun getUserId(uid: String?, callback: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")

            userDataRef.orderByChild("userUid").equalTo(uid).get().addOnCompleteListener(callback)
        }

    }
}