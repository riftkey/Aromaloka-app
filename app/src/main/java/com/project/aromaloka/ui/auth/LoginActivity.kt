package com.project.aromaloka.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import com.project.aromaloka.databinding.ActivityLoginBinding
import com.project.aromaloka.models.ResponseSession
import com.project.aromaloka.ui.main.BottomMainActivity
import com.project.aromaloka.utils.Factory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var factory: Factory
    private val viewModel by viewModels<LoginViewModel>{factory}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = Factory.getInstance(this)
        setListeners()
    }

    private fun setListeners() {

        binding.apply {
            btnLogin.isEnabled = false

            btnLogin.setOnClickListener {
                if (edEmail.length() !=0 && edPassword.length() >= 8){
                    postText()
                    viewModel.login()
                    viewModel.responseLogin.observe(this@LoginActivity){response ->
                        if (response != null) {
                            if (!response.error &&  response.idToken != null){
                                val session = ResponseSession(
                                    response.name.toString(),
                                    KEY_AUTH + (response.idToken.toString()), true)

                                viewModel.saveSession(session)
                                val intent = Intent(this@LoginActivity, BottomMainActivity::class.java)
                                intent.putExtra("session", session)
                                startActivity(intent)
                                finish()
                            } else {
                                if(response.error){
                                    Toast.makeText(
                                        this@LoginActivity, "Login gagal. Mohon check email dan password", Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(this@LoginActivity, "Silakan daftar terlebih dahulu", Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }

            }

            edPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    val passwordLength = s?.length ?: 0
                    btnLogin.isEnabled = passwordLength >= 8
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            btnRegister.setOnClickListener {
                val iRegister = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(iRegister)
            }
        }

    }

    private fun postText() {
        binding.apply {
            viewModel.postLogin(edEmail.text.toString(), edPassword.text.toString())
        }
    }

    companion object {
        private const val KEY_AUTH = " "
    }

}