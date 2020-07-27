package com.mfathurz.consumerapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.CircleCropTransformation
import com.mfathurz.consumerapp.R
import com.mfathurz.consumerapp.model.User
import kotlinx.android.synthetic.main.item_rv_github_user.view.*

class FavoriteRecyclerAdapter : RecyclerView.Adapter<FavoriteRecyclerAdapter.UserViewHolder>(){
     var users = ArrayList<User>()

    fun setData(items :ArrayList<User>){
        users.clear()
        users.addAll(items)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_github_user, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        with(holder.view) {
            item_txt_name.text = users[position].username

            item_img_user.load(users[position].avatar){
                crossfade(true)
                placeholder(R.drawable.ic_image_placeholder)
                transformations(CircleCropTransformation())
            }

        }
    }
}
