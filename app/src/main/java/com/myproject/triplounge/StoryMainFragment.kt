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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.myproject.triplounge.data.StoryDataClass
import com.myproject.triplounge.databinding.FragmentStoryMainBinding
import com.myproject.triplounge.databinding.StoryMainRowBinding
import com.myproject.triplounge.repository.StoryRepository
import com.myproject.triplounge.repository.UserRepository

class StoryMainFragment : Fragment() {

    // todo : 사용자 uid별로 게시물에 저장하여 내 게시물 페이지에서 자신이 작성한 게시물을 볼 수 있도록 설정해주기

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
                    val userName = i.child("userName").value as String
                    val storyUploadDate = i.child("storyUploadDate").value as String
                    val storyImage = i.child("storyImage").value as String
                    val storyTitle = i.child("storyTitle").value as String
                    val storyText = i.child("storyText").value as String

                    val story = StoryDataClass(storyIdx, userName, storyUploadDate, storyImage, storyTitle, storyText)
                    storyList.add(story)
                }
                recyclerStoryMain.adapter?.notifyDataSetChanged()
            }

            toolbarStoryMain.run {
                title = "todo_story_main_fragment"
                inflateMenu(R.menu.story_main_menu)

                setOnMenuItemClickListener {

                    when (it.itemId) {

                        R.id.itemStoryMainUserModify -> {
                            mainActivity.replaceWithBundleFragment(MainActivity.USER_INFO_MODIFY_FRAGMENT, true, null)
                        }
                        R.id.itemStoryMainAdd -> {
                            mainActivity.replaceWithBundleFragment(MainActivity.STORY_ADD_FRAGMENT, true, null)
                        }

                    }

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

            var tvUserName: TextView
            var tvDate: TextView
            var ivUploaded: ImageView
            var tvStoryTitle: TextView
            var tvStoryText: TextView
            var btnLike: Button
            var tvLikeCount: TextView

            init {
                tvUserName = storyMainRowBinding.tvUserName
                tvDate = storyMainRowBinding.tvDate
                ivUploaded = storyMainRowBinding.ivUploaded
                tvStoryTitle = storyMainRowBinding.tvStoryTitle
                tvStoryText = storyMainRowBinding.tvStoryText
                btnLike = storyMainRowBinding.btnLike
                btnLike.setOnClickListener {

                }
                tvLikeCount = storyMainRowBinding.tvLikeCount
                storyMainRowBinding.root.setOnClickListener {
                    val storyData = storyList[adapterPosition]
                    val newBundle = Bundle()
                    newBundle.putSerializable("storyData", storyData)
                    mainActivity.replaceWithBundleFragment(MainActivity.STORY_RESULT_FRAGMENT, true, newBundle)
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
            holder.tvUserName.text = storyList[position].userName
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