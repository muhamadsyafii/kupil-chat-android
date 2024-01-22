package id.syafii.kupilchat.data.remote.api

import id.syafii.kupilchat.BuildConfig
import id.syafii.kupilchat.domain.UserModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface AccountApi {

    @GET("v3/users/{user_id}")
    suspend fun getAccessToken(
        @Header("Api-Token") apiToken: String = BuildConfig.API_TOKEN,
        @Path("user_id") userId: String,
    ): Response<UserModel>

}