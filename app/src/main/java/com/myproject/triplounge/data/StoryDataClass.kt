package com.myproject.triplounge.data

import java.io.Serializable

data class StoryDataClass(var storyIdx: Long,
                          var userName: String?,
                          var storyUploadDate: String,
                          var storyImage: String,
                          var storyTitle: String,
                          var storyText: String): Serializable
