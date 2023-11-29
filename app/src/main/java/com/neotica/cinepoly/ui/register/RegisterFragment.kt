package com.neotica.cinepoly.ui.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.neotica.cinepoly.R
import com.neotica.cinepoly.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.neotica.cinepoly.ui.customview.EmailCustomView
import com.neotica.cinepoly.ui.customview.PasswordCustomView

class RegisterFragment : Fragment(R.layout.fragment_register) {
    private lateinit var edtEmail: EmailCustomView
    private lateinit var edtPassword: PasswordCustomView
    private lateinit var btLogin: Button

    private val viewModel: RegisterViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentRegisterBinding.bind(view)

        binding.apply {
            edtEmail = etEmail
            edtPassword = etPassword
            btLogin = btnLogin
            btLogin.isEnabled = false
        }
        edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showButton()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showButton()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        register()
    }

    private fun showButton() {
        val binding = view?.let { FragmentRegisterBinding.bind(it) }
        binding.apply {
            btLogin.isEnabled = edtPassword.text?.toString()?.let { password ->
                edtEmail.text?.toString()?.let { email ->
                    viewModel.isValidEmail(email) && viewModel.isValidPassword(password)
                }
            } ?: false
        }
    }

    private fun register(){
        val binding = view?.let { FragmentRegisterBinding.bind(it) }
        binding?.btnLogin?.setOnClickListener {
            val userName = binding.etUsername.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val passwordConf = binding.etPasswordConfirmation.text.toString()
            lifecycleScope.launch {
                viewModel.registerUser(userName, email, password, passwordConf)
            }.run {
                findNavController().navigateUp()
            }

        }
    }
}