package com.mfathurz.githubuser.ui.detail.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import coil.transform.CircleCropTransformation
import com.mfathurz.githubuser.R
import com.mfathurz.githubuser.model.User
import com.mfathurz.githubuser.ui.detail.FollowPagerAdapter
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel
    private var statusFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        supportActionBar?.let {
            it.title = getString(R.string.detail_action_bar_title)
            it.setDisplayHomeAsUpEnabled(true)
        }

        val user = intent.getStringExtra(EXTRA_USERNAME) as String
        showLoading(true)
        viewPagerInit(user)

        detailViewModel.detailUser(user)
        detailViewModel.getUser().observe(this, Observer {
            viewInit(it)
            showLoading(false)
        })

        fab_add_favorite.setOnClickListener {
            statusFavorite = !statusFavorite
            setStatusFavorite(statusFavorite)
        }
    }

    private fun viewPagerInit(user: String) {
        val mBundle = Bundle()
        mBundle.putString(EXTRA_USERNAME, user)


        val followPagerAdapter = FollowPagerAdapter(this, supportFragmentManager)
        followPagerAdapter.setData(mBundle)
        view_pager.adapter = followPagerAdapter
        tab_layout.setupWithViewPager(view_pager)
    }

    private fun viewInit(user: User) {
        img_user.load(user.avatar) {
            crossfade(true)
            placeholder(R.drawable.ic_image_placeholder)
            transformations(CircleCropTransformation())
        }

        txt_name.text = user.name
        txt_username.text = user.username
        txt_location.text = user.location
        txt_company.text = user.company
        txt_repository.text = user.repository.toString()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            detailProgressBar.visibility = View.VISIBLE
        } else {
            detailProgressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite)
            fab_add_favorite.setImageResource(R.drawable.ic_favorite)
        else
            fab_add_favorite.setImageResource(R.drawable.ic_favorite_border)
    }
}