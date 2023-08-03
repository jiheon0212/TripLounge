package com.myproject.triplounge

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.myproject.triplounge.databinding.FragmentStoryMainBinding
import com.myproject.triplounge.databinding.StoryMainRowBinding
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class StoryMainFragment : Fragment() {

    lateinit var fragmentStoryMainBinding: FragmentStoryMainBinding
    lateinit var mainActivity: MainActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentStoryMainBinding = FragmentStoryMainBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        mainActivity.getData()

        fragmentStoryMainBinding.run {

            toolbarStoryMain.run {
                title = "todo_story_main_fragment"
                inflateMenu(R.menu.story_main_menu)
                setOnMenuItemClickListener {
                    mainActivity.replaceFragment(MainActivity.STORY_ADD_FRAGMENT, true)
                    false
                }
                recyclerStoryMain.run {
                    adapter = StoryMainAdapter()
                    layoutManager = LinearLayoutManager(mainActivity)
                }
            }

        }

        return fragmentStoryMainBinding.root
    }

    inner class StoryMainAdapter: RecyclerView.Adapter<StoryMainAdapter.StoryMainViewHolder>() {
        inner class  StoryMainViewHolder(storyMainRowBinding: StoryMainRowBinding): RecyclerView.ViewHolder(storyMainRowBinding.root) {

            var tvUserNickname: TextView
            var tvDate: TextView
            var ivUploaded: ImageView
            var tvUserText: TextView
            var btnLike: Button
            var tvLikeCount: TextView

            init {
                tvUserNickname = storyMainRowBinding.tvUserNickname
                tvDate = storyMainRowBinding.tvDate
                ivUploaded = storyMainRowBinding.ivUploaded
                tvUserText = storyMainRowBinding.tvUserText
                btnLike = storyMainRowBinding.btnLike
                btnLike.setOnClickListener {

                }
                tvLikeCount = storyMainRowBinding.tvLikeCount
                storyMainRowBinding.root.setOnClickListener {
                    mainActivity.replaceFragment(MainActivity.STORY_RESULT_FRAGMENT, true)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryMainViewHolder {

            val storyMainRowBinding = StoryMainRowBinding.inflate(layoutInflater)
            val storyMainViewHolder = StoryMainViewHolder(storyMainRowBinding)
            storyMainRowBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return storyMainViewHolder
        }

        override fun getItemCount(): Int {

            return mainActivity.storyList.size
        }

        override fun onBindViewHolder(holder: StoryMainViewHolder, position: Int) {

            holder.tvUserNickname.text = "testUser_$position"
            holder.tvDate.text =  mainActivity.storyList[position].postWriteDate
            mainActivity.getImage( mainActivity.storyList[position].postImage , holder.ivUploaded)
            holder.tvLikeCount.text = "testLike : $position"
            holder.tvUserText.text =  mainActivity.storyList[position].postTitle
            holder.tvUserText.append("\n${ mainActivity.storyList[position].postText}")
        }
    }

    override fun onResume() {
        super.onResume()
        mainActivity.getData()
        fragmentStoryMainBinding.recyclerStoryMain.adapter?.notifyDataSetChanged()
        Log.d("!!", "${mainActivity.storyList.size}")
    }


}