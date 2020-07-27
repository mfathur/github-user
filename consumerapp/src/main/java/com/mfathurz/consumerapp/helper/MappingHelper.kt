package com.mfathurz.consumerapp.helper

import android.database.Cursor
import com.mfathurz.consumerapp.Contract.AVATAR
import com.mfathurz.consumerapp.Contract.COMPANY
import com.mfathurz.consumerapp.Contract.LOCATION
import com.mfathurz.consumerapp.Contract.NAME
import com.mfathurz.consumerapp.Contract.REPOSITORY
import com.mfathurz.consumerapp.Contract.USERNAME
import com.mfathurz.consumerapp.model.User


object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<User> {
        val favUserList = arrayListOf<User>()
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