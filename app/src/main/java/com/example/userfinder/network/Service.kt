package com.example.userfinder.network

import com.example.userfinder.network.response.ResponseUser
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class Service(private val networkService: NetworkService) {

    fun getUser(callback: callbackGetUser): Subscription {
        return networkService.getUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable -> Observable.error(throwable) }
            .subscribe(object : Subscriber<List<ResponseUser>>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable?) {
                    try {
                        callback.onError(NetworkError(e).toString())
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onNext(t: List<ResponseUser>) {
                    if (t != null) {
                        callback.onSuccess(t)
                    }
                }

            })
    }

    interface BaseCallback {
        fun onError(message: String)
    }

    interface callbackGetUser : BaseCallback {
        fun onSuccess(response: List<ResponseUser>)
    }


}
