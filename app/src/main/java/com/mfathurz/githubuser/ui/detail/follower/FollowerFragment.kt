package com.mfathurz.githubuser.ui.detail.follower

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
import kotlinx.android.synthetic.main.fragment_follower.*

class FollowerFragment : Fragment() {

    private lateinit var followerViewModel: FollowerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        followerViewModel = ViewModelProvider(this).get(FollowerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val username = it.getString(DetailActivity.EXTRA_USERNAME)
            followerViewModel.setUserFollowers(username)
        }

        followerViewModel.getUserFollowers().observe(viewLifecycleOwner, Observer {
            followerRecyclerView.adapter = FollowRecyclerAdapter(it)
            followerRecyclerView.layoutManager = LinearLayoutManager(activity)
        })

    }
}