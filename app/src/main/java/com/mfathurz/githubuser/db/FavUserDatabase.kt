package com.mfathurz.githubuser.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mfathurz.githubuser.model.FavoriteUser

@Database(
    entities = [FavoriteUser::class],
    version = 1
)
abstract class FavUserDatabase : RoomDatabase() {

    abstract fun getFavUserDao(): FavUserDao

    companion object {
        @Volatile
        private var instance: FavUserDatabase? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            FavUserDatabase::class.java,
            "fav_user_database"
        ).build()
    }
}