package com.example.mytodolist.data

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

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