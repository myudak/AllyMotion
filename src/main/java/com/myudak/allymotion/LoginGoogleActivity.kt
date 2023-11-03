package com.myudak.allymotion

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.myudak.allymotion.databinding.ActivityLoginGoogleBinding
import com.myudak.allymotion.util.mlkit.activity.LivePreviewActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext



class LoginGoogleActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    private lateinit var binding:ActivityLoginGoogleBinding

    @OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginGoogleBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide();

        binding.apply {
            btnGoogleLogin.setOnClickListener{
                val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.webclient_id))
                    .requestEmail()
                    .requestProfile()
                    .build()

                val signInClient = GoogleSignIn.getClient(it.context,options)

                val intent = signInClient.signInIntent

                startActivityForResult(intent,10001)

            }

//            tvLoginText.setOnClickListener{
//                val intent = Intent(it.context,LivePreviewActivity::class.java)
//                startActivity(intent)
//                finish()
//            }

            tvtext2.setOnClickListener{
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/myudak/AllyMotion/blob/main/AllyMotion-tos.md"))
                startActivity(intent)
            }

            tvLogin.setOnClickListener{
                val intent = Intent(it.context,LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }


    @OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 10001){
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)

                val credential = GoogleAuthProvider.getCredential(account.idToken,null)

                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener{task ->
                        if(task.isSuccessful){
                            val i= Intent(this,OnBoardingActivity::class.java)
                            startActivity(i)
                            finish()
                        }else {
                            Toast.makeText(this, task.exception?.message ?: "",Toast.LENGTH_LONG).show()
                        }


                    }
            } catch (e : ApiException){
                Log.w("TAG", "signInResult:failed code=" + e.statusCode)
            }

        }
    }
}

