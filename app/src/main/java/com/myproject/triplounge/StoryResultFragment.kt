package com.myproject.triplounge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myproject.triplounge.databinding.FragmentStoryResultBinding

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

            toolbarStoryResult.run {

                title = "todo_story_result_fragment"
                inflateMenu(R.menu.story_result_mine_menu)

                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.STORY_RESULT_FRAGMENT)
                }

                setOnMenuItemClickListener {

                    when (it.itemId){

                        R.id.itemStoryResultDelete -> {

                        }
                        R.id.itemStoryResultModify -> {

                        }
                    }
                    false
                }
            }

        }

        return fragmentStoryResultBinding.root
    }
}