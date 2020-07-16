package com.mfathurz.githubuser.db

import android.content.ContentValues
import android.database.Cursor
import androidx.room.*
import com.mfathurz.githubuser.Constant.TABLE_NAME

@Dao
interface FavUserDao {
    @Query("SELECT * FROM $TABLE_NAME ORDER BY username ASC")
    fun getAllFavUser(): Cursor?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavUser(values : ContentValues): Int

    @Delete
    fun deleteFavUserById(id: Int): Int
}