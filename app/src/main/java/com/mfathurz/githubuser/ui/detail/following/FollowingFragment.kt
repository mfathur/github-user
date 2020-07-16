package com.mfathurz.githubuser.ui.detail.following

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfathurz.githubuser.R
import com.mfathurz.githubuser.ui.detail.FollowRecyclerAdapter
import com.mfathurz.githubuser.ui.detail.main.DetailActivity
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment() {

    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        followingViewModel = ViewModelProvider(this).get(FollowingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val username = it.getString(DetailActivity.EXTRA_USERNAME)
            followingViewModel.setUserFollowing(username)
        }

        followingViewModel.getUserFollowing().observe(viewLifecycleOwner, Observer {
            followingRecyclerView.adapter = FollowRecyclerAdapter(it)
            followingRecyclerView.layoutManager = LinearLayoutManager(activity)
        })
    }
}