package com.mfathurz.githubuser.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfathurz.githubuser.R
import com.mfathurz.githubuser.ui.setting.PreferenceFragment
import com.mfathurz.githubuser.ui.favorite.FavoriteUserActivity
import com.mfathurz.githubuser.ui.detail.main.DetailActivity
import com.mfathurz.githubuser.ui.setting.SettingActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel : MainViewModel
    private lateinit var recyclerAdapter: UserRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = getString(R.string.main_action_bar_tile)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        recyclerAdapter = UserRecyclerAdapter()
        recyclerAdapter.notifyDataSetChanged()

        rv_github_user.layoutManager = LinearLayoutManager(this@MainActivity)
        rv_github_user.adapter = recyclerAdapter

        btn_search.setOnClickListener {
            val username = edt_username.text.toString()
            if (username.isEmpty()){
                edt_username.error = getString(R.string.emptyField)
                return@setOnClickListener
            }
            showLoading(true)
            mainViewModel.searchUser(username)
        }

        mainViewModel.getUsers().observe(this, Observer {query->
            if (query != null){
                recyclerAdapter.setData(query)
                showLoading(false)
            }
        })

        recyclerAdapter.setOnItemClickedCallback(object : UserRecyclerAdapter.OnItemClickCallback {
            override fun onItemClicked(username: String) {
                val mIntent = Intent(this@MainActivity, DetailActivity::class.java)
                mIntent.putExtra(DetailActivity.EXTRA_USERNAME, username)
                startActivity(mIntent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_setting -> {
                val mIntent = Intent(this,
                    SettingActivity::class.java)
                startActivity(mIntent)
            }

            R.id.nav_favorite -> {
                val mIntent = Intent(this,
                    FavoriteUserActivity::class.java)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(state : Boolean){
        if (state){
            mainProgressBar.visibility = View.VISIBLE
        } else {
            mainProgressBar.visibility = View.GONE
        }
    }

}