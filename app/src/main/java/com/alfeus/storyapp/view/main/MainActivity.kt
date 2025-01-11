package com.alfeus.storyapp.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alfeus.storyapp.R
import com.alfeus.storyapp.adapter.StoryAdapter
import com.alfeus.storyapp.view.detail.DetailActivity
import com.alfeus.storyapp.view.login.LoginViewModel
import com.alfeus.storyapp.view.uploadStory.UploadActivity
import com.alfeus.storyapp.view.welcome.WelcomeActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var fabUpload: FloatingActionButton
    private val mainViewModel: MainViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Ceritamu"

        recyclerView = findViewById(R.id.rvReview)
        progressBar = findViewById(R.id.progressBar)
        fabUpload = findViewById(R.id.fabUpload)

        fabUpload.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }

        checkSession()
        setupRecyclerView()
        setupObservers()
    }

    private fun checkSession() {
        loginViewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                mainViewModel.fetchStories()
            }
        }
    }


    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        storyAdapter = StoryAdapter(emptyList()) { storyId ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("STORY_ID", storyId)
            startActivity(intent)
        }
        recyclerView.adapter = storyAdapter
    }

    private fun setupObservers() {
        mainViewModel.stories.observe(this) { stories ->
            if (stories != null) {
                storyAdapter.updateData(stories)
            }
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        mainViewModel.errorMessage.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            loginViewModel.logout()
            val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.fetchStories()
    }
}
