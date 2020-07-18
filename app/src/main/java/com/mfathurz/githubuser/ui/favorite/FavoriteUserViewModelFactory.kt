package com.mfathurz.githubuser.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mfathurz.githubuser.Repository

class FavoriteUserViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavoriteViewModel(repository) as T
    }

}