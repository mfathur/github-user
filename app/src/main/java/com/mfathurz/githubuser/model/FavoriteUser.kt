package com.mfathurz.githubuser.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mfathurz.githubuser.Constant.AVATAR
import com.mfathurz.githubuser.Constant.COMPANY
import com.mfathurz.githubuser.Constant.FOLLOWERS
import com.mfathurz.githubuser.Constant.FOLLOWINGS
import com.mfathurz.githubuser.Constant.ID
import com.mfathurz.githubuser.Constant.LOCATION
import com.mfathurz.githubuser.Constant.NAME
import com.mfathurz.githubuser.Constant.REPOSITORY
import com.mfathurz.githubuser.Constant.TABLE_NAME
import com.mfathurz.githubuser.Constant.USERNAME
import java.io.Serializable

@Entity(tableName = TABLE_NAME)
data class FavoriteUser(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    var id: Int? = null,

    @ColumnInfo(name = AVATAR)
    val avatar: String,

    @ColumnInfo(name = COMPANY)
    val company: String,

    @ColumnInfo(name = LOCATION)
    val location: String,

    @ColumnInfo(name = NAME)
    val name: String,

    @ColumnInfo(name = REPOSITORY)
    val repository: Int,

    @ColumnInfo(name = USERNAME)
    val username: String,

    @ColumnInfo(name = FOLLOWERS)
    val followers: List<String>,

    @ColumnInfo(name = FOLLOWINGS)
    val followings: List<String>
) : Serializable