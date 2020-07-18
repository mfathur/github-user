package com.mfathurz.githubuser.db

import android.database.Cursor
import androidx.room.*
import com.mfathurz.githubuser.db.Contract.TABLE_NAME
import com.mfathurz.githubuser.model.User

@Dao
interface FavUserDao {
    @Query("SELECT * FROM $TABLE_NAME ORDER BY username ASC")
    fun getAllFavUser(): Cursor?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavUser(user: User)

    @Delete
    suspend fun deleteFavUser(user: User)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAllFavUser()

}