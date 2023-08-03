package com.myproject.triplounge.repository

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.myproject.triplounge.data.StoryDataClass

class StoryRepository {

    companion object {

        fun setStoryIdx(storyIdx: Long, callback: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val storyIdxRef = database.getReference("StoryIdx")

            storyIdxRef.get().addOnCompleteListener {
                it.result.ref.setValue(storyIdx).addOnCompleteListener(callback)
            }
        }
        fun getStoryIdx(callback: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val storyIdxRef = database.getReference("StoryIdx")

            storyIdxRef.get().addOnCompleteListener(callback)
        }

        fun uploadStoryImage(uploadUri: Uri, fileName: String, callback: (Task<UploadTask.TaskSnapshot>) -> Unit) {
            val storage = FirebaseStorage.getInstance()
            val imageRef = storage.reference.child(fileName)

            imageRef.putFile(uploadUri).addOnCompleteListener(callback)
        }
        fun getStoryImage(imageView: ImageView, fileName: String) {
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference
            val pathRef = storageRef.child(fileName)

            pathRef.downloadUrl.addOnSuccessListener {
                Glide.with(imageView.context)
                    .load(it)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .into(imageView)
            }
        }

        fun addStory(storyDataClass: StoryDataClass, callback: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val storyDataRef = database.getReference("StoryData")

            storyDataRef.push().setValue(storyDataClass).addOnCompleteListener(callback)
        }
        fun getStoryAll(callback: (Task<DataSnapshot>) -> Unit) {

            val database = FirebaseDatabase.getInstance()
            val storyDataRef = database.getReference("StoryData")

            storyDataRef.get().addOnCompleteListener(callback)
        }
    }
}