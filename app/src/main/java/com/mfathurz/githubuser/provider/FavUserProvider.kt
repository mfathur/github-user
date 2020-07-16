package com.mfathurz.githubuser.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.core.net.toUri
import com.mfathurz.githubuser.Constant.AUTHORITY
import com.mfathurz.githubuser.Constant.CONTENT_URI
import com.mfathurz.githubuser.Constant.TABLE_NAME
import com.mfathurz.githubuser.db.FavUserDao
import com.mfathurz.githubuser.db.FavUserDatabase

class FavUserProvider : ContentProvider() {

    companion object {
        private const val FAV_USER = 1
        private const val FAV_USER_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, TABLE_NAME, FAV_USER)
            addURI(AUTHORITY, "$TABLE_NAME/#", FAV_USER_ID)
        }
    }

    private val getFavUserDao: FavUserDao by lazy {
        FavUserDatabase.invoke(requireNotNull(context)).getFavUserDao()
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleteFavUser = when (FAV_USER_ID) {
            sUriMatcher.match(uri) -> getFavUserDao.deleteFavUserById(uri.lastPathSegment!!.toInt())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleteFavUser
    }

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val insertFavUser = when(FAV_USER){
            sUriMatcher.match(uri) -> getFavUserDao.insertFavUser(values as ContentValues)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI,null)

        return "$CONTENT_URI/$insertFavUser".toUri()
    }

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
            val cursor = when(sUriMatcher.match(uri)){
                FAV_USER -> getFavUserDao.getAllFavUser()
                else -> null
            }
        cursor?.setNotificationUri(context?.contentResolver,uri)
        return cursor
    }


    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        throw UnsupportedOperationException()
    }
}
