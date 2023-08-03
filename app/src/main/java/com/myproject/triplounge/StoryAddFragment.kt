package com.myproject.triplounge

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.myproject.triplounge.data.StoryDataClass
import com.myproject.triplounge.databinding.FragmentStoryAddBinding
import com.myproject.triplounge.repository.StoryRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StoryAddFragment : Fragment() {

    lateinit var fragmentStoryAddBinding: FragmentStoryAddBinding
    lateinit var mainActivity: MainActivity
    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    var uploadUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentStoryAddBinding = FragmentStoryAddBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        albumLauncher = albumSetting(fragmentStoryAddBinding.ivbtnStoryAddUpload)

        fragmentStoryAddBinding.run {

            // imagebtn click -> select image
            ivbtnStoryAddUpload.setOnClickListener {

                // get image from album
                val newIntent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                newIntent.setType("image/*")
                val mimeType = arrayOf("image/*")
                newIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
                albumLauncher.launch(newIntent)

                // get image from taking photo
                // todo : dialog 통해 사용자가 사진을 get image & create image 결정
            }
            toolbarStoryAdd.run {

                title = "todo_story_add_fragment"
                inflateMenu(R.menu.story_add_menu)
                setOnMenuItemClickListener {

                    StoryRepository.getStoryIdx {

                        // todo : 게시물 삭제 시 storyIdx 감소 메서드 제작
                        // class 매개 변수 setting
                        var storyIdx = it.result.value as Long
                        storyIdx++
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val writeDate = sdf.format(Date(System.currentTimeMillis()))
                        val title = tiedtStoryAddTitle.text.toString()
                        val text = tiedtStoryAddText.text.toString()
                        val fileName = if (uploadUri == null){
                            "None"
                        } else {
                            "images/img_${System.currentTimeMillis()}.jpg"
                        }
                        val storyDataClass = StoryDataClass(storyIdx, "unknown user", writeDate, fileName, title, text)

                        // storage image upload method 제작
                        StoryRepository.addStory(storyDataClass) {

                            StoryRepository.setStoryIdx(storyIdx) {

                                if (uploadUri != null) {
                                    StoryRepository.uploadStoryImage(uploadUri!!, fileName){
                                        Snackbar.make(fragmentStoryAddBinding.root, "Image Uploaded", Snackbar.LENGTH_SHORT).show()
                                        mainActivity.replaceFragment(MainActivity.STORY_MAIN_FRAGMENT, true)
                                    }
                                }
                                else {
                                    Snackbar.make(fragmentStoryAddBinding.root, "Image is Null", Snackbar.LENGTH_SHORT).show()
                                    mainActivity.replaceFragment(MainActivity.STORY_MAIN_FRAGMENT, true)
                                }
                            }
                        }
                    }
                    true
                }
            }
        }

        return fragmentStoryAddBinding.root
    }

    // todo : ablumSetting method 학습
    fun albumSetting(previewImageView: ImageView) : ActivityResultLauncher<Intent>{

        val albumContract = ActivityResultContracts.StartActivityForResult()
        val albumLauncher = registerForActivityResult(albumContract){

            if(it.resultCode == AppCompatActivity.RESULT_OK){

                // 선택한 이미지에 접근할 수 있는 Uri 객체를 추출한다.
                if(it.data?.data != null){

                    uploadUri = it.data?.data

                    // 안드로이드 10 (Q) 이상이라면...
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){

                        // 이미지를 생성할 수 있는 디코더를 생성한다.
                        val source = ImageDecoder.createSource(mainActivity.contentResolver, uploadUri!!)
                        // Bitmap객체를 생성한다.
                        val bitmap = ImageDecoder.decodeBitmap(source)

                        previewImageView.setImageBitmap(bitmap)
                    }
                    else {

                        // 컨텐츠 프로바이더를 통해 이미지 데이터 정보를 가져온다.
                        val cursor = mainActivity.contentResolver.query(uploadUri!!, null, null, null, null)
                        if(cursor != null){
                            cursor.moveToNext()

                            // 이미지의 경로를 가져온다.
                            val idx = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                            val source = cursor.getString(idx)

                            // 이미지를 생성하여 보여준다.
                            val bitmap = BitmapFactory.decodeFile(source)
                            previewImageView.setImageBitmap(bitmap)
                        }
                    }
                }
            }
        }

        return albumLauncher
    }
}