package com.mfathurz.githubuser

import android.database.Cursor
import com.mfathurz.githubuser.db.FavUserDao
import com.mfathurz.githubuser.model.User

class Repository (private val favUserDao: FavUserDao){

    fun getAllFavoriteUser(): Cursor? = favUserDao.getAllFavUser()

    suspend fun deleteFavUser(favoriteUser: User) = favUserDao.deleteFavUser(favoriteUser)

    suspend fun insertFavUser(favoriteUser: User) = favUserDao.insertFavUser(favoriteUser)

    suspend fun deleteAllFavUser() = favUserDao.deleteAllFavUser()

}