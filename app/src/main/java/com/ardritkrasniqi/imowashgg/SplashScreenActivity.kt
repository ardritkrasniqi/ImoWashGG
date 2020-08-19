package com.ardritkrasniqi.imowashgg

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth



class SplashScreenActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()

        Handler().postDelayed({
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                goToAdminPanel()
            } else {
                goToMainActivity()
            }
        }, 1000)

    }

    fun goToAdminPanel() {

    }

    fun goToMainActivity() {

    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth?.currentUser
    }
}