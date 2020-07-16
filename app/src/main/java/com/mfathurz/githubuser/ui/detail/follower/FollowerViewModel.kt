package com.mfathurz.githubuser.ui.detail.follower

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import com.mfathurz.githubuser.model.UserRecycler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowerViewModel : ViewModel() {
    val followers = MutableLiveData<ArrayList<UserRecycler>>()

    fun setUserFollowers(username : String?){
        val listFollowers = ArrayList<UserRecycler>()

        val url = "https://api.github.com/users/${username}/followers"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token bf6977231c3014d7d6b3dacbe474feda71b24618")
        client.addHeader("User-Agent","request")
        client.get(url, object : TextHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: String?) {
                try {
                    val list = JSONArray(responseString)

                    for (i in 0 until list.length()){
                        val obj = list.getJSONObject(i)

                        val userName = obj.getString("login")
                        val avatarUrl = obj.getString("avatar_url")

                        val followers = UserRecycler(userName,avatarUrl)
                        Log.d("FollowerViewModel", followers.toString())
                        listFollowers.add(followers)
                    }

                    followers.postValue(listFollowers)
                }
                catch ( e : Exception)
                { Log.d("Exception", e.message as String) }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                Log.d("Failure", throwable?.message.toString())
            }
        })
    }

    fun getUserFollowers() : LiveData<ArrayList<UserRecycler>> {
        return followers
    }
}