package com.mfathurz.githubuser.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mfathurz.githubuser.db.Contract.AVATAR
import com.mfathurz.githubuser.db.Contract.COMPANY
import com.mfathurz.githubuser.db.Contract.LOCATION
import com.mfathurz.githubuser.db.Contract.NAME
import com.mfathurz.githubuser.db.Contract.REPOSITORY
import com.mfathurz.githubuser.db.Contract.TABLE_NAME
import com.mfathurz.githubuser.db.Contract.USERNAME
import java.io.Serializable

@Entity(tableName = TABLE_NAME)
data class User(
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
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = USERNAME)
    val username: String
):Serializable