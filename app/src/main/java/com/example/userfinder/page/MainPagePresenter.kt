package com.example.userfinder.page

import com.example.userfinder.network.Service
import com.example.userfinder.network.response.ResponseUser
import rx.subscriptions.CompositeSubscription

class MainPagePresenter(val service: Service, var view: MainPageContract.View) : MainPageContract.Presenter {
    private val subscriptions = CompositeSubscription()

    init {
        this.view.setPresenter(this)
    }

    override fun getUser() {
        view.showLoading()
        val subscription = service.getUser(object : Service.callbackGetUser{
            override fun onSuccess(response: List<ResponseUser>) {
                view.onSuccessGetUser(response)
                view.hideLoading()
            }

            override fun onError(message: String) {
                view.hideLoading()
            }

        })

        subscriptions.add(subscription)
    }

    override fun start() {

    }


}