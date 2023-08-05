package com.myproject.triplounge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.google.android.material.snackbar.Snackbar
import com.myproject.triplounge.data.UserDataClass
import com.myproject.triplounge.databinding.FragmentUserInfoModifyBinding
import com.myproject.triplounge.repository.UserRepository

class UserInfoModifyFragment : Fragment() {

    lateinit var fragmentUserInfoModifyBinding: FragmentUserInfoModifyBinding
    lateinit var mainActivity: MainActivity
    lateinit var userSex: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentUserInfoModifyBinding = FragmentUserInfoModifyBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        val userInfo = mutableListOf<UserDataClass>()
        val uid = UserRepository.getUserUid()

        fragmentUserInfoModifyBinding.run {

            radioGroupUserInfoModify.run {
                userSex = if (rbMale.isChecked) {
                    "Female"
                } else {
                    "Male"
                }
            }

            btnUserInfoModifyBirth.setOnClickListener {

            }

            toolbarUserInfoModify.run {

                title = "user_info_modify_fragment"
                inflateMenu(R.menu.user_info_modify_menu)

                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.USER_INFO_MODIFY_FRAGMENT)
                }

                setOnMenuItemClickListener {
                    UserRepository.getUserInfo(uid) {
                        for (i in it.result.children) {
                            val userIdx = i.child("userIdx").value as Long
                            val userUid = i.child("userUid").value as String
                            val userId = i.child("userId").value as String
                            val userPw = i.child("userPw").value as String
                            val userNickname = tiedtUserInfoModifyNickname.text.toString()
                            val userBirthDate = "testdate : 2023-01-01"
                            val phoneNumber = tiedtUserInfoModifyPhoneNumber.text.toString()

                            val userData = UserDataClass(userIdx, userUid, userId, userPw, userNickname, userSex, userBirthDate, phoneNumber)
                            UserRepository.modifyUserInfo(userData) {
                                UserRepository.setUserIdx(userData.userIdx) {
                                    mainActivity.replaceWithBundleFragment(MainActivity.STORY_MAIN_FRAGMENT, false, null)
                                    Snackbar.make(fragmentUserInfoModifyBinding.root, "Modify Success", Snackbar.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    true
                }
            }
        }

        return fragmentUserInfoModifyBinding.root
    }
}