package com.project.aromaloka.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.project.aromaloka.R
import com.project.aromaloka.databinding.ActivityRegisterBinding
import com.project.aromaloka.utils.Factory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>{factory}
    private lateinit var factory: Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

       factory = Factory.getInstance(this)


        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            btnRegister.setOnClickListener {
                if (edName.length() !=0 && edEmail.length() !=0 && edPassword.length() > 7){
                    binding.apply{
                        viewModel.postRegister(edName.text.toString(), edEmail.text.toString(), edPassword.text.toString())
                    }
                    viewModel.responseRegister.observe(this@RegisterActivity){
                        response -> if(response.message != null){
                        showRegisterSuccessDialog()
                        }
                    }
                }

            }

            btnLogin.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun showRegisterSuccessDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_register_success, null)

        val alertDialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        val btnOk = dialogView.findViewById<Button>(R.id.btn_ok)
        btnOk.setOnClickListener {
            alertDialog.dismiss()
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}