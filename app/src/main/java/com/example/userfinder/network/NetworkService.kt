package com.example.userfinder.network

import com.example.userfinder.network.response.ResponseUser
import retrofit2.http.*
import rx.Observable


interface NetworkService {

    @GET("users")
    fun getUser(): Observable<List<ResponseUser>>

}
