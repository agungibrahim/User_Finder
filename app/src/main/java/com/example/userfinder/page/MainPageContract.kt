package com.example.userfinder.page

import com.example.userfinder.network.response.ResponseUser
import com.example.userfinder.utils.BasePresenter
import com.example.userfinder.utils.BaseView

class MainPageContract {
    interface View : BaseView<Presenter> {
        fun showLoading()
        fun hideLoading()
        fun onError(response: String)
        fun onSuccessGetUser(response: List<ResponseUser>)
    }

    interface Presenter : BasePresenter {
        fun getUser()
    }
}