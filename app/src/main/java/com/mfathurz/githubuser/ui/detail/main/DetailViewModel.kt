package com.mfathurz.githubuser.ui.detail.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import com.mfathurz.githubuser.model.User
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel : ViewModel() {

    val user = MutableLiveData<User>()

    fun detailUser(username: String) {
        val url = DETAIL_USER_URL + username
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
                    val obj = JSONObject(responseString as String)
                    val avatar = obj.getString("avatar_url")
                    val company = obj.getString("company")
                    val name = obj.getString("name")
                    val location = obj.getString("location")
                    val repository = obj.getInt("public_repos")
                    val userLogin = obj.getString("login")

                    val userInfo = User(avatar, company, location, name, repository, userLogin)
                    user.postValue(userInfo)
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
                Log.d("onFailure", throwable?.message.toString())
            }
        })
    }

    fun getUser(): LiveData<User> {
        return user
    }

    companion object{
        const val DETAIL_USER_URL = "https://api.github.com/users/"
    }
}