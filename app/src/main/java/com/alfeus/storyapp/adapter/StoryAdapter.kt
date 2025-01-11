package com.alfeus.storyapp.adapter

import androidx.core.util.Pair
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.alfeus.storyapp.R
import com.alfeus.storyapp.data.response.ListStoryItem
import com.alfeus.storyapp.view.detail.DetailActivity
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class StoryAdapter(
    private var stories: List<ListStoryItem>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    inner class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        private val descTextView: TextView = itemView.findViewById(R.id.textViewDescription)
        private val coverImageView: ShapeableImageView = itemView.findViewById(R.id.imageViewCover)

        fun bind(story: ListStoryItem) {
            nameTextView.text = story.name
            descTextView.text = story.description
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(coverImageView)

            // Tambahkan animasi shared element
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                    putExtra("STORY_ID", story.id)
                    putExtra("STORY_NAME", story.name)
                    putExtra("STORY_DESCRIPTION", story.description)
                    putExtra("STORY_PHOTO_URL", story.photoUrl)
                }

                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    Pair(coverImageView, "gambar"),
                    Pair(nameTextView, "teksUser"),
                    Pair(descTextView, "deskripsi")
                )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }

            itemView.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        animateZoomIn(coverImageView)
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        animateZoomOut(coverImageView)
                    }
                }
                false
            }

            itemView.setOnClickListener {
                onItemClick(story.id)
            }
        }

        private fun animateZoomIn(view: View) {
            view.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .alpha(0.8f)
                .setDuration(300)
                .start()
        }

        private fun animateZoomOut(view: View) {
            view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(300)
                .start()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount(): Int = stories.size

    fun updateData(newStories: List<ListStoryItem>) {
        stories = newStories
        notifyDataSetChanged()
    }
}
