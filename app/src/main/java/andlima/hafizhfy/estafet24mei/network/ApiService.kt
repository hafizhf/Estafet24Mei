package andlima.hafizhfy.estafet24mei.network

import andlima.hafizhfy.estafet24mei.model.GetUserResponseItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("user")
    fun getLoginData(@Query("username") username : String) : Call<List<GetUserResponseItem>>
}