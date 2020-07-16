package com.mfathurz.githubuser.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.CircleCropTransformation
import com.mfathurz.githubuser.R
import com.mfathurz.githubuser.model.UserRecycler
import kotlinx.android.synthetic.main.item_rv_github_user.view.*

class UserRecyclerAdapter : RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder>() {

    private val users = ArrayList<UserRecycler>()

    fun setData(items :ArrayList<UserRecycler>){
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
            item_txt_name.text = users[position].user

            item_img_user.load(users[position].avatarUrl){
                crossfade(true)
                placeholder(R.drawable.ic_image_placeholder)
                transformations(CircleCropTransformation())
            }

            setOnClickListener {
                onItemClickCallback.onItemClicked(users[position].user)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(username: String)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickedCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}