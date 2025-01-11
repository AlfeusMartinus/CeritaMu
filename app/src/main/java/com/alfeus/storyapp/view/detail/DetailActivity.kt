package com.alfeus.storyapp.view.detail

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.alfeus.storyapp.data.response.StoryDetail
import com.alfeus.storyapp.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.sharedElementEnterTransition = TransitionInflater.from(this)
            .inflateTransition(android.R.transition.move)

        val storyName = intent.getStringExtra("STORY_NAME")
        val storyDescription = intent.getStringExtra("STORY_DESCRIPTION")
        val storyPhotoUrl = intent.getStringExtra("STORY_PHOTO_URL")

        binding.userName.text = storyName
        binding.eventDescription.text = storyDescription
        Glide.with(this)
            .load(storyPhotoUrl)
            .into(binding.eventImage)

        val storyId = intent.getStringExtra("STORY_ID") ?: return

        setupObservers()
        detailViewModel.fetchStoryDetail(storyId)

    }

    private fun setupObservers() {
        detailViewModel.storyDetail.observe(this) { story ->
            bindStoryData(story)
        }

        detailViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        detailViewModel.errorMessage.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bindStoryData(story: StoryDetail) {
        Glide.with(this)
            .load(story.photoUrl)
            .into(binding.eventImage)

        binding.userName.text = story.name
        binding.eventDescription.text = story.description
    }

}