package com.myproject.triplounge

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import com.myproject.triplounge.data.StoryDataClass
import com.myproject.triplounge.databinding.FragmentStoryResultBinding
import com.myproject.triplounge.repository.StoryRepository

class StoryResultFragment : Fragment() {

    lateinit var fragmentStoryResultBinding: FragmentStoryResultBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentStoryResultBinding = FragmentStoryResultBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        fragmentStoryResultBinding.run {
            val storyData = arguments?.getSerializable("storyData") as StoryDataClass
            tvUserName2.text = storyData?.userName
            tvDate2.text = storyData?.storyUploadDate
            StoryRepository.getStoryImage(ivUploaded2, storyData?.storyImage!!)
            tvStoryTitle2.text = storyData?.storyTitle
            tvStoryText2.text = storyData?.storyText
            tvLikeCount2
            btnLike2.setOnClickListener {

            }

            toolbarStoryResult.run {

                title = "todo_story_result_fragment"
                inflateMenu(R.menu.story_result_mine_menu)

                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.STORY_RESULT_FRAGMENT)
                }

                setOnMenuItemClickListener {

                    when (it.itemId){

                        // todo : uid를 비교하여 글 작성자와 같은 사람일 경우에만 메뉴버튼 보이기 설정
                        R.id.itemStoryResultDelete -> {
                            StoryRepository.storyDelete(storyData) {
                                Snackbar.make(fragmentStoryResultBinding.root, "Story deleted", Snackbar.LENGTH_SHORT).show()
                                mainActivity.replaceFragment(MainActivity.STORY_MAIN_FRAGMENT, false)
                            }
                        }
                        R.id.itemStoryResultModify -> {
                            val newBundle = Bundle()
                            newBundle.putSerializable("storyModify", storyData)
                            mainActivity.replaceWithBundleFragment(MainActivity.STORY_MODIFY_FRAGMENT, true, newBundle)
                        }
                    }
                    false
                }
            }

        }

        return fragmentStoryResultBinding.root
    }
}