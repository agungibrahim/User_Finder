package com.example.userfinder.page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userfinder.MainApp
import com.example.userfinder.R
import com.example.userfinder.network.Service
import com.example.userfinder.network.response.ResponseUser
import com.example.userfinder.utils.BaseActivity
import com.example.userfinder.utils.ConnectionDetector
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity(), MainPageContract.View {

    @Inject
    lateinit var service: Service
    private var presenter: MainPageContract.Presenter? = null
    lateinit var cd: ConnectionDetector
    private var mAdapterUser: AdapterUser? = null
    private var listData : ArrayList<ResponseUser> = ArrayList()
    var counter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    fun init() {
        MainApp.instance.appComponent?.inject(this)
        presenter = MainPagePresenter(service, this)

        cd = ConnectionDetector()
        cd.isConnectingToInternet(this)

        mAdapterUser = AdapterUser(this)
        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_contact.layoutManager = layoutManager
        rv_contact.adapter = mAdapterUser

        if (cd.isConnectingToInternet(this)) {
            presenter?.getUser()
        } else toast("Tidak ada koneksi internet")

        edt_search.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                onSearch(p0.toString())
            }

        })

        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                    //code to fetch more data for endless scrolling
                    loading_load_more.visibility = View.VISIBLE
                    loadMore()
                }
            }
        } as NestedScrollView.OnScrollChangeListener)
    }

    override fun showLoading() {
        showDialog(true)
    }

    override fun hideLoading() {
        showDialog(false)
    }

    override fun onError(response: String) {
        showMessage(response)
    }

    override fun onSuccessGetUser(response: List<ResponseUser>) {
        if (response.isNotEmpty()) {
            listData.clear()
            listData.addAll(response)
            mAdapterUser?.updateList(listData.take(10))
            counter = 10
        }
    }

    override fun setPresenter(presenter: MainPageContract.Presenter?) {
        this.presenter = presenter
    }

    fun onSearch(nama : String) {
        showDialog(true)
        if (nama.isNotEmpty() && listData.count() != 0) {
            Log.e("typing : ", nama)
            val result = listData.filter {
                it.login!!.contains(nama, ignoreCase = true)
            }

            val gson = Gson()
            val jsonString = gson.toJson(result)
            Log.e("listSearch", jsonString)
            mAdapterUser?.updateList(result)
        } else {
            mAdapterUser?.updateList(listData.take(10))
        }
        showDialog(false)
    }

    fun loadMore() {
        counter += 10
        mAdapterUser?.updateList(listData.take(counter))
        loading_load_more.visibility = View.GONE
    }

    fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}