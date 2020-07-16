package com.mfathurz.githubuser.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import com.mfathurz.githubuser.model.UserRecycler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainViewModel : ViewModel() {

    private val listUser = MutableLiveData<ArrayList<UserRecycler>>()

    fun searchUser(username: String) {
        val users = ArrayList<UserRecycler>()

        val url =SEARCH_USER_URL + username
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token bf6977231c3014d7d6b3dacbe474feda71b24618")
        client.addHeader("User-Agent", "request")
        client.get(url, object : TextHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?
            ) {
                try {
                    val items = JSONObject(responseString as String).getJSONArray("items")
                    for (i in 0 until items.length()) {
                        val userLogin = items.getJSONObject(i).getString("login")
                        val avatarUrl = items.getJSONObject(i).getString("avatar_url")
                        val user = UserRecycler(userLogin, avatarUrl)

                        users.add(user)
                    }
                    listUser.postValue(users)
                } catch (e: Exception) {
                    Log.d("Exception", e.message as String)
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?,
                throwable: Throwable?
            ) {
                Log.d("Fail", throwable?.message.toString())
            }
        })

    }

    fun getUsers(): LiveData<ArrayList<UserRecycler>> {
        return listUser
    }

    companion object{
        const val SEARCH_USER_URL = "https://api.github.com/search/users?q="
    }
}