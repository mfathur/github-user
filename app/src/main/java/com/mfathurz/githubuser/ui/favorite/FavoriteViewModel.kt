package com.mfathurz.githubuser.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfathurz.githubuser.Repository
import com.mfathurz.githubuser.model.User
import com.mfathurz.githubuser.util.MappingHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class FavoriteViewModel(repo: Repository) : ViewModel() {

    private val favoriteUsers = MutableLiveData<List<User>>()

    init {
        favoriteUsers.value = emptyList()
        getAllFavUsers(repo)
    }

    private fun getAllFavUsers(repo: Repository) = CoroutineScope(IO).launch {
        val cursor = repo.getAllFavoriteUser()
        val list = MappingHelper.mapCursorToList(cursor)
        favoriteUsers.postValue(list)
    }

    fun getFavoriteData():LiveData<List<User>> = favoriteUsers

    fun refreshData(repo: Repository){
        getAllFavUsers(repo)
    }

    fun deleteFavUser(repo: Repository, favoriteUser: User) = CoroutineScope(IO).launch {
        repo.deleteFavUser(favoriteUser)
    }

    fun deleteAllFavUser(repo: Repository) {
        CoroutineScope(IO).launch {
            repo.deleteAllFavUser()
        }
        favoriteUsers.value = emptyList()
    }

}