package com.alfeus.storyapp.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.alfeus.storyapp.databinding.ActivitySignupBinding
import com.alfeus.storyapp.view.customView.ValidatedInputView
import com.alfeus.storyapp.view.welcome.WelcomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
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
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailInputView.getInputText()
            val password = binding.passwordInputView.getInputText()

            showLoading(true)
            registerUser(name, email, password)
            observeViewModel()
        }

    }

    private fun observeViewModel() {
        authViewModel.registerResult.observe(this) { result ->
            showLoading(false)
            result.onSuccess { response ->
                showAlertDialog(
                    title = "Pendaftaran Berhasil",
                    message = response.message,
                    onPositiveButtonClick = {
                        // Navigasi ke WelcomeActivity
                        val intent = Intent(this, WelcomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                )
            }
//            result.onFailure { error ->
////                Toast.makeText(this, "Gagal: ${error.message}", Toast.LENGTH_SHORT).show()
//                showAlertDialog(
//                    title = "Pendaftaran Gagal",
//                    message = error.message ?: "Terjadi kesalahan, coba lagi.",
//                    onPositiveButtonClick = {
//                        val intent = Intent(this, WelcomeActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                    }
//                )
//            }
            result.onFailure { error ->
                val errorMessage = error.message ?: "Terjadi kesalahan, coba lagi."
                if (errorMessage == "Email is already taken") {
                    Toast.makeText(this, "Email sudah digunakan, gunakan email lain.", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("SignupError", errorMessage)
                }
            }
        }
    }

    private fun showAlertDialog(title: String, message: String?, onPositiveButtonClick: () -> Unit) {
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
        binding.signupButton.isEnabled = !b
    }

    private fun registerUser(name: String, email: String, password: String) {
        authViewModel.register(name, email, password)
    }

    private fun playAnimation() {

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailInputView, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordInputView, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }
}