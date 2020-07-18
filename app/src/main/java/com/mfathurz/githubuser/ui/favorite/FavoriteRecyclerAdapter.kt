package com.mfathurz.githubuser.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.CircleCropTransformation
import com.mfathurz.githubuser.R
import com.mfathurz.githubuser.model.User
import kotlinx.android.synthetic.main.item_rv_github_user.view.*

class FavoriteRecyclerAdapter :
    ListAdapter<User, FavoriteRecyclerAdapter.FavoriteViewHolder>(FavoriteDiffCallback()) {

    inner class FavoriteViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(favUser: User) {
            with(view) {
                item_txt_name.text = favUser.username

                item_img_user.load(favUser.avatar) {
                    crossfade(true)
                    placeholder(R.drawable.ic_image_placeholder)
                    transformations(CircleCropTransformation())
                }

                setOnClickListener {
                    onItemClickCallback.onItemClicked(favUser)
                }

                setOnLongClickListener {
                    onItemLongClickCallback.onItemLongClicked(favUser)
                    return@setOnLongClickListener true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_github_user, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) =
        holder.bind(getItem(position))

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(favoriteUser: User)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    private lateinit var onItemLongClickCallback: OnItemLongClickCallback

    interface OnItemLongClickCallback {
        fun onItemLongClicked(favoriteUser: User)
    }

    fun setOnItemLongClickCallback(onItemLongClickCallback: OnItemLongClickCallback) {
        this.onItemLongClickCallback = onItemLongClickCallback
    }
}

private class FavoriteDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.username == newItem.username
    }

}