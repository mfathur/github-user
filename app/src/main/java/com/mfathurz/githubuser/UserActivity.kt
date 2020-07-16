package com.mfathurz.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfathurz.githubuser.ui.main.UserRecyclerAdapter
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        supportActionBar?.let {
            it.title = getString(R.string.fav_user)
            it.setDisplayHomeAsUpEnabled(true)
        }

        favUserRecyclerView.setHasFixedSize(true)
        favUserRecyclerView.layoutManager = LinearLayoutManager(this)
        favUserRecyclerView.adapter = UserRecyclerAdapter()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}