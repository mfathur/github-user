package com.mfathurz.githubuser.ui.detail.following

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import com.mfathurz.githubuser.BuildConfig
import com.mfathurz.githubuser.model.UserRecycler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowingViewModel : ViewModel() {

    val followings = MutableLiveData<ArrayList<UserRecycler>>()

    fun setUserFollowing(username : String?){
        val listFollowings = ArrayList<UserRecycler>()

        val url = "https://api.github.com/users/${username}/following"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token ${BuildConfig.API_KEY}")
        client.addHeader("User-Agent","request")
        client.get(url, object : TextHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: String?) {
                try {
                    val list = JSONArray(responseString)

                    for (i in 0 until list.length()){
                        val obj = list.getJSONObject(i)

                        val userName = obj.getString("login")
                        val avatarUrl = obj.getString("avatar_url")

                        val following = UserRecycler(userName,avatarUrl)
                        Log.d("following", following.toString())
                        listFollowings.add(following)
                    }

                    followings.postValue(listFollowings)
                }
                catch ( e : Exception)
                { Log.d("Exception", e.message as String) }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                Log.d("Failure", throwable?.message.toString())
            }
        })
    }

    fun getUserFollowing() : LiveData<ArrayList<UserRecycler>>{
        return followings
    }
}