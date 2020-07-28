package com.mfathurz.githubuser.ui.detail.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import com.mfathurz.githubuser.BuildConfig
import com.mfathurz.githubuser.Repository
import com.mfathurz.githubuser.helper.Helpers
import com.mfathurz.githubuser.model.User
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject

class DetailViewModel(private val repo: Repository) : ViewModel() {

    val user = MutableLiveData<User>()
    val isFavoriteUser = MutableLiveData<Boolean>()

    private fun checkIfFavoriteUser(repo: Repository, username: String) : Boolean {
        var isFavorite = false
        viewModelScope.launch {
            val deferredFavorite = CoroutineScope(IO).async {
                val cursor = repo.getAllFavoriteUser()
                Helpers.mapCursorToList(cursor)
            }
            val favoriteUsers = deferredFavorite.await()

            for (i in favoriteUsers.indices){
                if (username == favoriteUsers[i].username){
                    isFavorite = true
                    user.postValue(favoriteUsers[i])
                    isFavoriteUser.postValue(true)
                    break
                }
            }
        }
        return isFavorite
    }

    fun detailUser(username: String) {
        if (!checkIfFavoriteUser(repo,username)){
            val url = DETAIL_USER_URL + username
            val client = AsyncHttpClient()
            client.addHeader("Authorization", "token ${BuildConfig.API_KEY}")
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
    }

    fun getUser(): LiveData<User> {
        return user
    }

    fun insertFavUser( favoriteUser: User) = CoroutineScope(IO).launch{
        repo.insertFavUser(favoriteUser)
    }

    fun deleteFavUser( favoriteUser: User) = CoroutineScope(IO).launch {
        repo.deleteFavUser(favoriteUser)
        isFavoriteUser.postValue(false)
    }

    companion object {
        const val DETAIL_USER_URL = "https://api.github.com/users/"
    }
}