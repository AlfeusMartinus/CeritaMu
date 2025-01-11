package com.alfeus.storyapp.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.alfeus.storyapp.databinding.ActivityLoginBinding
import com.alfeus.storyapp.view.customView.ValidatedInputView
import com.alfeus.storyapp.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
        setupCustomViews()
    }

    private fun setupCustomViews() {
        binding.emailInputView.apply {
            validationType = ValidatedInputView.ValidationType.EMAIL
        }

        binding.passwordInputView.apply {
            validationType = ValidatedInputView.ValidationType.PASSWORD
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailInputView.getInputText()
            val password = binding.passwordInputView.getInputText()

            showLoading(true)
            loginUser(email, password)
            observeViewModel()
        }
    }

    private fun observeViewModel() {

        loginViewModel.getSession().observe(this) { session ->
            Log.d("MainActivity", "Token: ${session.token}")
        }

        loginViewModel.loginResult.observe(this) { result ->
            showLoading(false)

            result.onSuccess { response ->
                showAlertDialog(
                    title = "Login Berhasil",
                    message = "Selamat datang, ${response.loginResult.name}",
                    onPositiveButtonClick = {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                )
            }

            result.onFailure { error ->
                showAlertDialog(
                    title = "Login Gagal",
                    message = error.message ?: "Terjadi kesalahan, coba lagi.",
                    onPositiveButtonClick = {}
                )
            }
        }
    }

    private fun showAlertDialog(title: String, message: String, onPositiveButtonClick: () -> Unit) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                onPositiveButtonClick()
            }
            create()
            show()
        }
    }


    private fun showLoading(b: Boolean) {
        binding.progressBar.visibility = if (b) View.VISIBLE else View.GONE
        binding.loginButton.isEnabled = !b
    }

    private fun loginUser(email: String, password: String) {
        loginViewModel.login(email, password)
    }

    private fun playAnimation() {

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailInputView, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordInputView, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }

}