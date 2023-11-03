package com.myudak.allymotion

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myudak.allymotion.databinding.ActivityMainBinding
import com.myudak.allymotion.fragment.FirstFragment
import com.myudak.allymotion.fragment.SecondFragment
import com.myudak.allymotion.util.ViewPagerAdapter
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity() {



    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var auth: FirebaseAuth
    lateinit var binding:ActivityMainBinding

    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()

        checkUser()
        val user = auth.currentUser


        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navView =  findViewById<NavigationView>(R.id.nav_view)



//        update nav header
        val headerView = navView.inflateHeaderView(R.layout.nav_header)
        val headerText = headerView.findViewById<TextView>(R.id.user_name)
        val headerEmail = headerView.findViewById<TextView>(R.id.user_email)
        val profilePicture : CircleImageView = headerView.findViewById(R.id.ProfilePictureNavHeader)


        val parentView = profilePicture.parent as ViewGroup?




        if (user != null) {
            headerEmail.text = user.email
            if (user.displayName.isNullOrEmpty()) {
                parentView!!.removeView(profilePicture)
            }
            else {
                Log.w("TAG", user.displayName!!)
                headerText.text = user.displayName
                val profileImageUrl = user.photoUrl.toString()

                // Load the profile image into the ImageView using Glide library
                Glide.with(this)
                    .load(profileImageUrl)
                    .circleCrop() // This will make the image circular
                    .into(profilePicture)
            }
        }



        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)




        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_settings -> {
                    val intent= Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_home -> Toast.makeText(applicationContext,"HOME",Toast.LENGTH_SHORT).show()
//                R.id.nav_logout ->  mGoogleSignInClient.signOut().addOnCompleteListener {
//                    Firebase.auth.signOut()
//                    val intent= Intent(this, WelcomeActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
                R.id.nav_nofap -> {
                    val intent= Intent(this, NofapActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_logout -> showDialog()
            }
            true
        }

        ///// bottom nav view

        var viewPager : ViewPager2 = findViewById(R.id.viewPager)
        var bottomNavView : BottomNavigationView = findViewById(R.id.bottomNavView)

        val pagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        pagerAdapter.addFragment(FirstFragment())
        pagerAdapter.addFragment(SecondFragment())
        viewPager.adapter = pagerAdapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNavView.menu.getItem(position).isChecked = true
            }
        })

        bottomNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.firstFragment -> viewPager.currentItem = 0
                R.id.secondFragment -> viewPager.currentItem = 1
            }
            true
        }

//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
//
////        val navController = findNavController(R.id.fragmentContainerView)
//
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
//        val navController = navHostFragment.navController
//
//        bottomNavigationView.setupWithNavController(navController)





    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_logout)

        val logOut = dialog.findViewById<AppCompatButton>(R.id.btnLogOut)

        logOut.setOnClickListener{
            //        logout
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            mGoogleSignInClient= GoogleSignIn.getClient(this,gso)

            Toast.makeText(it.context,"Log Out",Toast.LENGTH_SHORT).show()

            mGoogleSignInClient.signOut().addOnCompleteListener {
                    Firebase.auth.signOut()
                    val intent= Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        }

        dialog.show()
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.Transparent.toArgb()))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun checkUser() {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null){

            // logged in
        } else {
            val intent = Intent(this,WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}