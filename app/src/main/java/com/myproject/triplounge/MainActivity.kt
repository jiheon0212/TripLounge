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
        val STORY_MAIN_FRAGMENT = "StoryMainFragment"
        val STORY_ADD_FRAGMENT = "StoryAddFragment"
        val STORY_RESULT_FRAGMENT = "StoryResultFragment"
    }
    val permissionList = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_MEDIA_LOCATION,
        Manifest.permission.INTERNET
    )
    val storyList = mutableListOf<StoryDataClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        requestPermissions(permissionList, 0)

        activityMainBinding.run {

        }

        setContentView(activityMainBinding.root)
    }

    fun replaceFragment(name: String, addToBackStack: Boolean){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        var newFragment = when(name){
            STORY_MAIN_FRAGMENT -> StoryMainFragment()
            STORY_ADD_FRAGMENT -> StoryAddFragment()
            STORY_RESULT_FRAGMENT -> StoryResultFragment()

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

    // todo : recyclerView에서 제대로 호출되지 않는 문제 해결하기
    fun getData() {
        storyList.clear()
        val database = FirebaseDatabase.getInstance()
        val storyDataRef = database.getReference("StoryData")
        thread {
            storyDataRef.get().addOnCompleteListener {
                for (i in it.result.children){
                    var postIdx = i.child("postIdx").value as Long
                    var userNickname = i.child("userNickname").value as String
                    var postWriteDate = i.child("postWriteDate").value as String
                    var postImage = i.child("postImage").value as String
                    var postTitle = i.child("postTitle").value as String
                    var postText = i.child("postText").value as String

                    val story = StoryDataClass(postIdx, userNickname, postWriteDate, postImage, postTitle, postText)
                    storyList.add(story)
                }
            }
        }
        // todo : 값이 3개인 이유 알아내기
    }

    fun getImage(fileName: String, imageView: ImageView) {
        val storage = FirebaseStorage.getInstance()
        val fileRef = storage.reference.child(fileName)

        fileRef.downloadUrl.addOnCompleteListener {
            thread {
                val url = URL(it.result.toString())
                val httpURLConnection = url.openConnection() as HttpURLConnection
                val bitmap = BitmapFactory.decodeStream(httpURLConnection.inputStream)

                runOnUiThread {
                    imageView.setImageBitmap(bitmap)
                }
            }
        }
    }
}

data class StoryDataClass(  var postIdx: Long,
                            var userNickname: String,
                            var postWriteDate: String,
                            var postImage: String,
                            var postTitle: String,
                            var postText: String)