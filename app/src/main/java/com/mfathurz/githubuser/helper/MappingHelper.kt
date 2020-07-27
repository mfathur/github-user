package com.mfathurz.githubuser.helper

import android.database.Cursor
import com.mfathurz.githubuser.db.Contract.AVATAR
import com.mfathurz.githubuser.db.Contract.COMPANY
import com.mfathurz.githubuser.db.Contract.LOCATION
import com.mfathurz.githubuser.db.Contract.NAME
import com.mfathurz.githubuser.db.Contract.REPOSITORY
import com.mfathurz.githubuser.db.Contract.USERNAME
import com.mfathurz.githubuser.model.User

object MappingHelper {
    fun mapCursorToList(cursor: Cursor?): List<User> {
        val favUserList = mutableListOf<User>()
        cursor?.apply {
            while (moveToNext()) {
                val avatar = getString(getColumnIndexOrThrow(AVATAR))
                val company: String = getString(getColumnIndexOrThrow(COMPANY))
                val location: String = getString(getColumnIndexOrThrow(LOCATION))
                val name: String = getString(getColumnIndexOrThrow(NAME))
                val repository: Int = getInt(getColumnIndexOrThrow(REPOSITORY))
                val username: String = getString(getColumnIndexOrThrow(USERNAME))

                favUserList.add(User(avatar, company, location, name, repository, username))
            }
        }
        return favUserList
    }

}