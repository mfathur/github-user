package com.mfathurz.githubuser.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mfathurz.githubuser.Repository
import com.mfathurz.githubuser.helper.Helpers
import com.mfathurz.githubuser.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repo: Repository) : ViewModel() {

    private val favoriteUsers = MutableLiveData<List<User>>()

    init {
        favoriteUsers.value = emptyList()
        getAllFavUsers()
    }

    private fun getAllFavUsers() = CoroutineScope(IO).launch {
        val cursor = repo.getAllFavoriteUser()
        val list = Helpers.mapCursorToList(cursor)
        favoriteUsers.postValue(list)
    }

    fun getFavoriteData():LiveData<List<User>> = favoriteUsers

    fun refreshData(){
        getAllFavUsers()
    }

    fun deleteFavUser(favoriteUser: User) = CoroutineScope(IO).launch {
        repo.deleteFavUser(favoriteUser)
    }

    fun deleteAllFavUser() {
        CoroutineScope(IO).launch {
            repo.deleteAllFavUser()
        }
        favoriteUsers.value = emptyList()
    }

}