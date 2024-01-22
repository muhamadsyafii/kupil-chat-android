package id.syafii.kupilchat.utils

import android.content.Context
import android.widget.Toast
import com.sendbird.android.LogLevel
import com.sendbird.android.SendbirdChat
import com.sendbird.android.exception.SendbirdError
import com.sendbird.android.exception.SendbirdException
import com.sendbird.android.handler.InitResultHandler
import com.sendbird.android.params.InitParams
import com.sendbird.android.params.UserUpdateParams
import id.syafii.kupilchat.BuildConfig

object SendBirdUtils {

    fun initializeSendBird(context: Context, appId: String) {
        val initParams = InitParams(BuildConfig.APP_ID_SENDBIRD, context, true)
        initParams.logLevel = LogLevel.ERROR
        SendbirdChat.init(initParams, object : InitResultHandler {
            override fun onInitFailed(e: SendbirdException) {
            }

            override fun onInitSucceed() {
            }

            override fun onMigrationStarted() {
            }

        })
    }

    fun loginUser(userId: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        SendbirdChat.connect(userId) { user, e ->
            if (e != null) {
                val errorMessage = "SendBird login failed: ${e.message}"

                // Check if the error message indicates user not found
                if (e.message?.contains("User not found") == true) {
                    // Handle user not registered error
                    onError.invoke("User not registered with SendBird")
                } else {
                    // Handle other errors
                    onError.invoke(errorMessage)
                }
            } else {
                onSuccess.invoke(user?.nickname ?: "")
            }
        }
    }

    fun logoutUser(onSuccess: () -> Unit, onError: (String) -> Unit) {
        SendbirdChat.disconnect {
            onSuccess.invoke()
        }
    }

    fun updateUserNickname(nickname: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val params = UserUpdateParams().apply {
            this.nickname = nickname
        }
        SendbirdChat.updateCurrentUserInfo(params) {
            if (it != null) {
                onError.invoke(it.message.toString())
            }else {
                onSuccess.invoke()
            }

        }
    }

   /* fun refreshCurrentUser(onSuccess: () -> Unit, onError: (String) -> Unit) {
        SendbirdChat.getCurrentUserInfo { userInfo ->
            if (userInfo == null) {
                onError.invoke("Failed to refresh user information")
            } else {
                onSuccess.invoke()
            }
        }
    }*/

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}