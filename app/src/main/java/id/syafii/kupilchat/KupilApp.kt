package id.syafii.kupilchat

import android.app.Application
import com.sendbird.android.exception.SendbirdException
import com.sendbird.android.handler.InitResultHandler
import com.sendbird.uikit.SendbirdUIKit
import com.sendbird.uikit.adapter.SendbirdUIKitAdapter
import com.sendbird.uikit.interfaces.UserInfo
import id.syafii.kupilchat.utils.SendBirdUtils

class KupilApp : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        sendbirdChatInit()
    }

    private fun sendbirdChatInit() {
        SendBirdUtils.initializeSendBird(this@KupilApp, BuildConfig.APP_ID_SENDBIRD)
    }

    private fun initializeSendBird() {
        SendbirdUIKit.init(object :SendbirdUIKitAdapter {
            override fun getAppId(): String {
                return BuildConfig.APP_ID_SENDBIRD
            }

            override fun getAccessToken(): String? {
                return ""
            }

            override fun getUserInfo(): UserInfo {
                return object : UserInfo {
                    override fun getUserId(): String {
                        return ""
                    }

                    override fun getNickname(): String? {
                        return ""
                    }

                    override fun getProfileUrl(): String? {
                        return ""
                    }

                }
            }

            override fun getInitResultHandler(): InitResultHandler {
                return object : InitResultHandler {
                    override fun onInitFailed(e: SendbirdException) {
                    }

                    override fun onInitSucceed() {
                    }

                    override fun onMigrationStarted() {
                    }

                }
            }

        }, this@KupilApp)
    }

    companion object {
        lateinit var context: KupilApp
            private set
    }
}