package com.myproject.triplounge.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

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

    }
}