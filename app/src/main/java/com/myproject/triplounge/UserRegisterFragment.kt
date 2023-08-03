package com.myproject.triplounge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.myproject.triplounge.databinding.FragmentUserRegisterBinding
import com.myproject.triplounge.repository.UserRepository

class UserRegisterFragment : Fragment() {

    lateinit var fragmentUserRegisterBinding: FragmentUserRegisterBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentUserRegisterBinding = FragmentUserRegisterBinding.inflate(layoutInflater)

        fragmentUserRegisterBinding.run {

            btnUserRegister.setOnClickListener {

                // todo : 유효성 검사 & id 중복 검사 dialog 생성하기
                val id = tiedtUserRegisterId.text.toString()
                val pw = tiedtUserRegisterPw.text.toString()

                UserRepository.createUser(id, pw){
                    if (it.isSuccessful) {
                        Snackbar.make(fragmentUserRegisterBinding.root, "Complete", Snackbar.LENGTH_SHORT).show()
                        mainActivity.replaceFragment(MainActivity.USER_LOGIN_FRAGMENT, true)
                    }
                    else {
                        Snackbar.make(fragmentUserRegisterBinding.root, "Failed", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        mainActivity = activity as MainActivity

        return fragmentUserRegisterBinding.root
    }
}