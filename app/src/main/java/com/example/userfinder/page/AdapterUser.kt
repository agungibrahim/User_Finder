package com.example.userfinder.page

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.userfinder.R
import com.example.userfinder.network.response.ResponseUser
import kotlinx.android.synthetic.main.item_user_recycleview_layout.view.*

class AdapterUser(private val context: Context) : RecyclerView.Adapter<AdapterUser.ItemHolder>() {

    val listDataUser : ArrayList<ResponseUser> = ArrayList()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user_recycleview_layout, viewGroup, false))
    }

    override fun getItemCount(): Int {
        return listDataUser.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = listDataUser[position]

        if (!item.avatarUrl.isNullOrEmpty()) {
            Glide.with(context)
                .load(item.avatarUrl)
                .into(holder.imgProfileUser)
        } else {
            holder.imgProfileUser.setImageResource(R.drawable.img_user)
        }

        holder.textNameUser.text = item.login.toString()
    }

    inner class ItemHolder(v: View) : RecyclerView.ViewHolder(v){
        var imgProfileUser = v.img_profile
        var textNameUser = v.txt_name_user
    }

    fun updateList(listItem: List<ResponseUser>) {
        listDataUser.clear()
        listDataUser.addAll(listItem)
        notifyDataSetChanged()
    }
}