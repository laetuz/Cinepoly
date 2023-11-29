package com.neotica.cinepoly.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.neotica.cinepoly.MainActivity
import com.neotica.cinepoly.common.repeatCollectionOnCreated
import com.neotica.cinepoly.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.neotica.cinepoly.ui.customview.EmailCustomView
import com.neotica.cinepoly.ui.customview.PasswordCustomView

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var edtEmail: EmailCustomView
    private lateinit var edtPassword: PasswordCustomView
    private lateinit var btLogin: Button
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authCheck()
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
        login()
    }

    private fun showButton() {
        binding.apply {
            btLogin.isEnabled = edtPassword.text?.isNotEmpty() == true && edtEmail.text?.isNotEmpty() == true
        }
    }

    private fun authCheck() {
        val gotoMain = Intent(context, MainActivity::class.java)
        repeatCollectionOnCreated {
            viewModel.token.collect { token ->
                if (token != "") {
                    activity?.finish()
                    startActivity(gotoMain)
                }
                val check = token
                Log.d("neotica", "authCheck: $token")
            }
        }
    }

    private fun login(){
        binding.btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            lifecycleScope.launch {
                viewModel.login(
                    email = email,
                    password = password,
                    onLoginSuccess = {
                        val gotoMain = Intent(activity, MainActivity::class.java)
                        activity?.finish()
                        startActivity(gotoMain)
                    },
                    onLoginFailed = {
                        lifecycleScope.launch {
                            Toast.makeText(context, "Login failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

            }
        }
        binding.tvRegister.setOnClickListener {
            viewModel.navigate(this)
        }
        binding.ivLogin.setOnClickListener {
            lifecycleScope.launch {
                viewModel.setToken()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}