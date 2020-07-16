package com.mfathurz.githubuser.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.CircleCropTransformation
import com.mfathurz.githubuser.R
import com.mfathurz.githubuser.model.UserRecycler
import kotlinx.android.synthetic.main.item_rv_github_user.view.*


class FollowRecyclerAdapter(private val follows : ArrayList<UserRecycler>) :RecyclerView.Adapter<FollowRecyclerAdapter.FollowViewHolder>(){

    inner class FollowViewHolder(val view : View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_github_user,parent,false)
        return FollowViewHolder(view)
    }

    override fun getItemCount(): Int = follows.size

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        with(holder.view){
            item_img_user.load(follows[position].avatarUrl){
                crossfade(true)
                error(R.drawable.ic_image_placeholder)
                transformations(CircleCropTransformation())
            }

            item_txt_name.text = follows[position].user
        }
    }
}