package com.myproject.triplounge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.myproject.triplounge.data.StoryDataClass
import com.myproject.triplounge.databinding.FragmentStoryModifyBinding
import com.myproject.triplounge.repository.StoryRepository

class StoryModifyFragment : Fragment() {

    lateinit var fragmentStoryModifyBinding: FragmentStoryModifyBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentStoryModifyBinding = FragmentStoryModifyBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        fragmentStoryModifyBinding.run {

            val storyData = arguments?.getSerializable("storyData") as StoryDataClass

            toolbarStoryModify.run {

                title = "story_modify_fragment"
                inflateMenu(R.menu.story_modify_menu)
                setOnMenuItemClickListener {

                    // todo : img 와 나머지 값들 bundle에서 받아와 modifyStory에 담아주기
                    val storyTitle = tiedtStoryModifyTitle.text.toString()
                    val storyTest = tiedtStoryModifyText.text.toString()

                    // val modifyStory = StoryDataClass()

                    StoryRepository.storyModify(storyData) {
                        Snackbar.make(fragmentStoryModifyBinding.root, "Story modified", Snackbar.LENGTH_SHORT).show()
                        mainActivity.removeFragment(MainActivity.STORY_MODIFY_FRAGMENT)
                    }


                    false
                }
            }
        }

        return fragmentStoryModifyBinding.root
    }
}