package com.myproject.triplounge

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.myproject.triplounge.data.StoryDataClass
import com.myproject.triplounge.databinding.FragmentStoryMainBinding
import com.myproject.triplounge.databinding.StoryMainRowBinding
import com.myproject.triplounge.repository.StoryRepository

class StoryMainFragment : Fragment() {

    lateinit var fragmentStoryMainBinding: FragmentStoryMainBinding
    lateinit var mainActivity: MainActivity
    val storyList = mutableListOf<StoryDataClass>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentStoryMainBinding = FragmentStoryMainBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        fragmentStoryMainBinding.run {

            StoryRepository.getStoryAll {
                for (i in it.result.children){
                    val storyIdx = i.child("storyIdx").value as Long
                    val userNickname = i.child("userNickname").value as String
                    val storyUploadDate = i.child("storyUploadDate").value as String
                    val storyImage = i.child("storyImage").value as String
                    val storyTitle = i.child("storyTitle").value as String
                    val storyText = i.child("storyText").value as String

                    val story = StoryDataClass(storyIdx, userNickname, storyUploadDate, storyImage, storyTitle, storyText)
                    storyList.add(story)
                }
                recyclerStoryMain.adapter?.notifyDataSetChanged()
            }

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
            var tvStoryTitle: TextView
            var tvStoryText: TextView
            var btnLike: Button
            var tvLikeCount: TextView

            init {
                tvUserNickname = storyMainRowBinding.tvUserNickname
                tvDate = storyMainRowBinding.tvDate
                ivUploaded = storyMainRowBinding.ivUploaded
                tvStoryTitle = storyMainRowBinding.tvStoryTitle
                tvStoryText = storyMainRowBinding.tvStoryText
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
            return storyList.size
        }

        override fun onBindViewHolder(holder: StoryMainViewHolder, position: Int) {
            holder.tvUserNickname.text = "unknown user_$position"
            holder.tvDate.text = storyList[position].storyUploadDate
            StoryRepository.getStoryImage(holder.ivUploaded, storyList[position].storyImage)
            holder.tvStoryTitle.text = storyList[position].storyTitle
            holder.tvStoryText.text = storyList[position].storyText
            holder.tvLikeCount.text = "liked : $position"
        }
    }

    override fun onResume() {
        super.onResume()
        storyList.clear()
    }
}