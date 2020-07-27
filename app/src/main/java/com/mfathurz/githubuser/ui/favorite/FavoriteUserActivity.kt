package com.mfathurz.githubuser.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfathurz.githubuser.R
import com.mfathurz.githubuser.Repository
import com.mfathurz.githubuser.db.Contract.CONTENT_URI
import com.mfathurz.githubuser.db.FavUserDatabase
import com.mfathurz.githubuser.model.User
import com.mfathurz.githubuser.ui.detail.main.DetailActivity
import com.mfathurz.githubuser.ui.main.MainActivity
import com.mfathurz.githubuser.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_favorite_user.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var adapter: FavoriteRecyclerAdapter
    private lateinit var repository: Repository
    private var favoriteUserSelected: User? = null

    companion object {
        private const val ALERT_DIALOG_DELETE_ALL = 1
        private const val ALERT_DIALOG_DELETE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_user)

        repository = Repository(FavUserDatabase.invoke(this).getFavUserDao())
        val viewModelFactory = FavoriteUserViewModelFactory(repository)

        favoriteViewModel =
            ViewModelProvider(this, viewModelFactory).get(FavoriteViewModel::class.java)

        adapter = FavoriteRecyclerAdapter()

        supportActionBar?.apply {
            title = getString(R.string.fav_user)
            setDisplayHomeAsUpEnabled(true)
        }

        favUserRecyclerView.setHasFixedSize(true)
        favUserRecyclerView.layoutManager = LinearLayoutManager(this)
        favUserRecyclerView.adapter = adapter

        favoriteViewModel.getFavoriteData().observe(this, Observer {
            adapter.submitList(it)
            showLoading(false)
        })

        adapter.setOnItemClickCallback(object : FavoriteRecyclerAdapter.OnItemClickCallback {
            override fun onItemClicked(favoriteUser: User) {
                val mIntent = Intent(this@FavoriteUserActivity,DetailActivity::class.java)
                mIntent.putExtra(DetailActivity.EXTRA_USERNAME,favoriteUser.username)
                startActivity(mIntent)
            }
        })

        adapter.setOnItemLongClickCallback(object :
            FavoriteRecyclerAdapter.OnItemLongClickCallback {
            override fun onItemLongClicked(favoriteUser: User) {
                favoriteUserSelected = favoriteUser
                showAlertDialog(ALERT_DIALOG_DELETE)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_action_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_delete_all -> showAlertDialog(ALERT_DIALOG_DELETE_ALL)
            android.R.id.home -> {
                val mIntent = Intent(this, MainActivity::class.java)
                startActivity(mIntent)
            }
        }
        return true
    }

    private fun loadFavoriteAsync() {
        CoroutineScope(Main).launch {
            showLoading(true)

            val deferredFavorite = async(IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToList(cursor)
            }

            val notes = deferredFavorite.await()
            adapter.submitList(notes)
            showLoading(false)
        }
    }

    private fun showAlertDialog(type: Int) {
        val isDeleteAll = type == ALERT_DIALOG_DELETE_ALL

        val message = if (isDeleteAll) {
            getString(R.string.alert_delete_all)
        } else {
            getString(R.string.alert_delete_one)
        }

        val dialog = AlertDialog.Builder(this@FavoriteUserActivity)
        dialog.setTitle(getString(R.string.alert_title))
        dialog.setMessage(message)
        dialog.setPositiveButton(getString(R.string.yes)) { _, _ ->
            if (isDeleteAll) {
                favoriteViewModel.deleteAllFavUser(repository)
            } else {
                favoriteViewModel.deleteFavUser(repository, favoriteUserSelected!!)
                favoriteViewModel.refreshData(repository)
            }

        }
        dialog.setNegativeButton(getString(R.string.no)) { _, _ -> }
        dialog.setCancelable(true)
        dialog.show()
    }

    private fun showLoading(state: Boolean) {
        if (state)
            favoriteProgressBar.visibility = View.VISIBLE
        else
            favoriteProgressBar.visibility = View.GONE
    }
}