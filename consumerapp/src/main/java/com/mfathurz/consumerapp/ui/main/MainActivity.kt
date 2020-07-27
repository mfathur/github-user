package com.mfathurz.consumerapp.ui.main

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfathurz.consumerapp.Contract
import com.mfathurz.consumerapp.R
import com.mfathurz.consumerapp.helper.MappingHelper
import com.mfathurz.consumerapp.model.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteRecyclerAdapter
    private var favoriteUsers = arrayListOf<User>()
    private lateinit var myObserver: ContentObserver

    companion object {
        private const val FAVORITE_USERS_LIST = "favorite_users_list"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = FavoriteRecyclerAdapter()

        favUserRecyclerView.adapter = adapter
        favUserRecyclerView.setHasFixedSize(true)
        favUserRecyclerView.layoutManager = LinearLayoutManager(this)

        showLoading(true)
        if (savedInstanceState != null) {
            val list = savedInstanceState.getParcelableArrayList<User>(FAVORITE_USERS_LIST)
            if (list != null)
                adapter.users = list
        } else {
            loadFavoriteAsync()
        }
        showLoading(false)

        val handlerThread = HandlerThread("FavoriteUsersObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadFavoriteAsync()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        contentResolver.unregisterContentObserver(myObserver)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(FAVORITE_USERS_LIST, adapter.users)
    }

    private fun loadFavoriteAsync() {
        CoroutineScope(Dispatchers.Main).launch {

            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(Contract.CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val notes = deferredFavorite.await()
            favoriteUsers.addAll(notes)
            adapter.setData(notes)

        }

    }

    private fun showLoading(state: Boolean) {
        if (state)
            mainProgressBar.visibility = View.VISIBLE
        else
            mainProgressBar.visibility = View.GONE
    }
}
