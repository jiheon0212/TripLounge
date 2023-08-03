package com.myproject.triplounge

import android.Manifest
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.myproject.triplounge.databinding.ActivityMainBinding
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding
    companion object{
        val USER_LOGIN_FRAGMENT = "UserLoginFragment"
        val USER_REGISTER_FRAGMENT = "UserRegisterFragment"
        val STORY_MAIN_FRAGMENT = "StoryMainFragment"
        val STORY_ADD_FRAGMENT = "StoryAddFragment"
        val STORY_RESULT_FRAGMENT = "StoryResultFragment"
    }
    val permissionList = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_MEDIA_LOCATION,
        Manifest.permission.INTERNET
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        requestPermissions(permissionList, 0)

        activityMainBinding.run {
            replaceFragment(USER_LOGIN_FRAGMENT, false)
        }

        setContentView(activityMainBinding.root)
    }

    fun replaceFragment(name: String, addToBackStack: Boolean){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        var newFragment = when(name){
            STORY_MAIN_FRAGMENT -> StoryMainFragment()
            STORY_ADD_FRAGMENT -> StoryAddFragment()
            STORY_RESULT_FRAGMENT -> StoryResultFragment()
            USER_LOGIN_FRAGMENT -> UserLoginFragment()
            USER_REGISTER_FRAGMENT -> UserRegisterFragment()

            else -> Fragment()
        }

        fragmentTransaction.replace(R.id.fcvMain, newFragment)

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(name)
        }
        fragmentTransaction.commit()
    }

    fun removeFragment(name:String){
        supportFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}