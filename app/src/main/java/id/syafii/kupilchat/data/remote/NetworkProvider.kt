package id.syafii.kupilchat.data.remote

import android.content.Intent
import android.widget.Toast
import com.chuckerteam.chucker.api.ChuckerInterceptor
import id.syafii.kupilchat.BuildConfig
import id.syafii.kupilchat.KupilApp
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkProvider {

    fun getOkhttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val builder = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(30, TimeUnit.SECONDS) // Increase the timeout duration
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)

        /** HTTP Logging and Chucker*/
        builder.apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(getHttpLoggingInterceptor())
                addInterceptor(getChuckerInterceptor())
            }
        }

        builder.addInterceptor { chain ->
            val mOriRequest: Request = chain.request()
            val request: Request =
                mOriRequest.newBuilder()
//                    .addHeader("Authorization", "Bearer ${KasirPreferences().accessToken}")
                    .addHeader("Accept", "application/json")
                    .method(mOriRequest.method, mOriRequest.body)
                    .build()

            val response = chain.proceed(request)

            if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // Handle unauthorized access
                Toast.makeText(
                    KupilApp.context,
                    "Your session has expired, please log in again",
                    Toast.LENGTH_SHORT
                ).show()
                goToLogin()
            }
            response
        }
        return builder.build()
    }

    fun getRetrofit(client: OkHttpClient): Retrofit {
        val isDebug = BuildConfig.DEBUG
        val baseUrl = "https://api-${BuildConfig.APP_ID_SENDBIRD}.sendbird.com/"
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    private fun getChuckerInterceptor(): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(KupilApp.context).build()
    }

    private fun goToLogin() {

    }
}