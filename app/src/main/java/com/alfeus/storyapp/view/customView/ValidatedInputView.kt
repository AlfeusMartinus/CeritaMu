package com.alfeus.storyapp.view.customView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.alfeus.storyapp.R
import com.alfeus.storyapp.databinding.ViewValidatedInputBinding
import com.google.android.material.textfield.TextInputLayout

class ValidatedInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var binding: ViewValidatedInputBinding =
        ViewValidatedInputBinding.inflate(LayoutInflater.from(context), this)

    var validationType: ValidationType = ValidationType.NONE
        set(value) {
            field = value
            updateStartIcon()
        }

    private fun updateStartIcon() {
        when (validationType) {
            ValidationType.EMAIL -> {
                binding.inputLayout.startIconDrawable =
                    ContextCompat.getDrawable(context, R.drawable.ic_baseline_email_24)
            }
            ValidationType.PASSWORD -> {
                binding.inputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                binding.inputLayout.startIconDrawable =
                    ContextCompat.getDrawable(context, R.drawable.ic_baseline_lock_24)
                binding.inputEditText.inputType =
                    android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            else -> {
                binding.inputLayout.startIconDrawable = null
                binding.inputEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT
            }
        }
    }

    init {
        orientation = VERTICAL
        setupListeners()
    }

    private fun setupListeners() {
        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString()
                when (validationType) {
                    ValidationType.EMAIL -> validateEmail(input)
                    ValidationType.PASSWORD -> validatePassword(input)
                    else -> binding.inputLayout.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validateEmail(email: String) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputLayout.error = "Format email salah"
        } else {
            binding.inputLayout.error = null
        }
    }

    private fun validatePassword(password: String) {
        if (password.isNotEmpty() && password.length < 8) {
            binding.inputLayout.error = "Password minimal 8 karakter"
        } else {
            binding.inputLayout.error = null
        }
    }

    fun getInputText(): String {
        return binding.inputEditText.text.toString()
    }

    enum class ValidationType {
        NONE,
        EMAIL,
        PASSWORD
    }
}