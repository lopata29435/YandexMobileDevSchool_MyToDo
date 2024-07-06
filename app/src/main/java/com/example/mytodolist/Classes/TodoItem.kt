package com.example.mytodolist.Classes

import android.util.Log
import com.google.firebase.dataconnect.LogLevel
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

enum class Importance {
    low,
    basic,
    important
}

data class TodoItem(
    val id: String,
    val text: String,
    val importance: String,
    val deadline: Long?,
    val done: Boolean,
    val color: String?,
    val created_at: Long,
    val changed_at: Long,
    val last_updated_by: String
)

data class TodoResponse(
    val status: String,
    val list: List<TodoItem>,
    val revision: Int
)
interface TodoApi {
    @GET("list/")
    fun getList(): Call<TodoResponse>

    @GET("list/{id}")
    fun getItem(@Path("id") id: String): Call<TodoItem>

    data class AddItemRequest(
        @SerializedName("element")
        val list: TodoItem
    )
    @POST("list/")
    fun addItem(@Body request: AddItemRequest, @Header("X-Last-Known-Revision") revision: Int): Call<TodoItem>

    data class UpdateListRequest(
        @SerializedName("list")
        val list: List<TodoItem>
    )

    @PATCH("list/")
    fun updateList(@Body request: UpdateListRequest, @Header("X-Last-Known-Revision") revision: Int): Call<TodoResponse>

    @DELETE("list/{id}")
    fun deleteItem(@Path("id") id: String, @Header("X-Last-Known-Revision") revision: Int): Call<TodoItem>
}

val api = RetrofitClient.instance