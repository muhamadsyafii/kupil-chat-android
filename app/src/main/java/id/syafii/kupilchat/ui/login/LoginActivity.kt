package id.syafii.kupilchat.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.sendbird.android.exception.SendbirdException
import com.sendbird.android.handler.InitResultHandler
import com.sendbird.uikit.SendbirdUIKit
import com.sendbird.uikit.adapter.SendbirdUIKitAdapter
import com.sendbird.uikit.interfaces.UserInfo
import id.syafii.kupilchat.BuildConfig
import id.syafii.kupilchat.data.remote.NetworkProvider
import id.syafii.kupilchat.data.remote.api.AccountApi
import id.syafii.kupilchat.databinding.ActivityLoginBinding
import id.syafii.kupilchat.domain.UserModel
import id.syafii.kupilchat.ui.home.MainActivity
import id.syafii.kupilchat.utils.SendBirdUtils
import id.syafii.kupilchat.utils.SharedPreferenceUtils
import id.syafii.kupilchat.utils.hideKeyboard
import id.syafii.kupilchat.utils.hideProgress
import id.syafii.kupilchat.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.btnRegister.setOnClickListener {
            hideKeyboard()
            val userId = binding.etUserId.text.toString()
            SendBirdUtils.loginUser(userId = userId,
                onSuccess = {
                    showToast("Login Berhasil $it")
                    fetchAccessToken(userId = userId)
//                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                },
                onError = {
                    hideProgress(0)
                    showToast("$it")
                })
        }
    }

    private fun fetchAccessToken(userId: String) {
        val requestBody =
            RequestBody.create("application/json".toMediaTypeOrNull(), """{"userId": "$userId"}""")

        val accountApi = NetworkProvider.getRetrofit(NetworkProvider.getOkhttpClient())
            .create(AccountApi::class.java)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = accountApi.getAccessToken(userId = userId)
                Log.d("CheckValue", "fetchAccessToken: $response")
                if (response.isSuccessful) {
                    val accessToken = response.body()?.accessToken
                    val userId = response.body()?.userId
                    val user = response.body()
                    showToast("Broo dapet :$accessToken")
//                    SharedPreferenceUtils.accessToken = accessToken
//                    SharedPreferenceUtils.userId = userId
//                    SharedPreferenceUtils.userId = accessToken
                    if (user != null) {
                        initializeSendBird(user)
                    }
//                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                } else {
                    // Tangani kesalahan
                    showToast("GAGAL BROO : ${response.body()?.message}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("GAGAL BROO: ${e.message}")
            }
        }
    }

    private fun initializeSendBird(user: UserModel) {
        Log.d("CheckValue", "initializeSendBird: ${Gson().toJson(user)}")
        SendbirdUIKit.init(object : SendbirdUIKitAdapter {
            override fun getAppId(): String {
                return BuildConfig.APP_ID_SENDBIRD
            }

            override fun getAccessToken(): String {
                return BuildConfig.API_TOKEN
            }

            override fun getUserInfo(): UserInfo {
                return object : UserInfo {
                    override fun getUserId(): String {
                        return user.userId ?: ""
                    }

                    override fun getNickname(): String? {
                        return user.userId
                    }

                    override fun getProfileUrl(): String? {
                        return user.profileUrl
                    }

                }
            }

            override fun getInitResultHandler(): InitResultHandler {
                return object : InitResultHandler {
                    override fun onInitFailed(e: SendbirdException) {
                        showToast("onInitFailed")
                    }

                    override fun onInitSucceed() {
                        showToast("onInitSucceed")
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    }

                    override fun onMigrationStarted() {
                        showToast("onMigrationStarted")
                    }

                }
            }

        }, this)
    }
}