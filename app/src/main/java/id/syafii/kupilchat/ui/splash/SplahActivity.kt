package id.syafii.kupilchat.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import id.syafii.kupilchat.R
import id.syafii.kupilchat.ui.login.LoginActivity

class SplahActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splah)
        initView()
    }

    private fun initView() {
        Handler(Looper.getMainLooper()).postDelayed( {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 1500L)
    }
}