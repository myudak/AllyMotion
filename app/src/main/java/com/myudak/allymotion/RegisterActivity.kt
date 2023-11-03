package com.myudak.allymotion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.myudak.allymotion.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide();

        auth = FirebaseAuth.getInstance()
        binding.tvToLogin.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnRegister.setOnClickListener{
            val email = binding.edtEmailRegister.text.toString()
            val password = binding.edtPasswordRegister.text.toString()
            val passwordConfirm = binding.edtConfirmPassword.text.toString()

            if(email.isEmpty()){

                binding.edtEmailRegister.error = "Email Ndak Boleh Kosong"
                binding.edtEmailRegister.requestFocus()
                return@setOnClickListener
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.edtEmailRegister.error = "Email Tidak Valid"
                binding.edtEmailRegister.requestFocus()
                return@setOnClickListener
            }
            if(password.isEmpty()){
                binding.edtPasswordRegister.error = "Password Tidak Boleh Kosong"
                binding.edtPasswordRegister.requestFocus()
                return@setOnClickListener
            }
            if(password.length < 6){
                binding.edtPasswordRegister.error = "Password Kurang dari 6 char"
                binding.edtPasswordRegister.requestFocus()
                return@setOnClickListener
            }

            if(passwordConfirm != password) {
                binding.edtConfirmPassword.error = "Password tidak sama!"
                binding.edtConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            registerFirebase(email,password)
        }
    }

    private fun registerFirebase(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){
            if(it.isSuccessful){
                Toast.makeText(this,"Berhasil Register",Toast.LENGTH_SHORT).show()
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this,"${it.exception?.message}",Toast.LENGTH_SHORT).show()
            }
        }

    }
}