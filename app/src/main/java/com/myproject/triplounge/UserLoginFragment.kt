package com.myproject.triplounge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.myproject.triplounge.data.UserDataClass
import com.myproject.triplounge.databinding.FragmentUserLoginBinding
import com.myproject.triplounge.repository.UserRepository

class UserLoginFragment : Fragment() {

    // todo : logout 버튼 만들기, uid를 auth로부터 받아와서 사용자 이름 처리하기
    lateinit var fragmentUserLoginBinding: FragmentUserLoginBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentUserLoginBinding = FragmentUserLoginBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        fragmentUserLoginBinding.run {

            toolbarUserLogin.run {
                title = "user_login_fragment"
            }

            btnUserLogin.setOnClickListener {

                val id = tiedtUserLoginId.text.toString()
                val pw = tiedtUserLoginPw.text.toString()

                UserRepository.loginUser(id, pw){

                    if (it.isSuccessful) {
                        mainActivity.replaceWithBundleFragment(MainActivity.STORY_MAIN_FRAGMENT, false, null)
                        Snackbar.make(fragmentUserLoginBinding.root, "Login Success", Snackbar.LENGTH_SHORT).show()
                    }
                    else {
                        Snackbar.make(fragmentUserLoginBinding.root, "Login Failed", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }

            btnUserLoginRegister.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.USER_REGISTER_FRAGMENT, true)
            }
        }

        return fragmentUserLoginBinding.root
    }

}