package com.mfathurz.consumerapp

import android.net.Uri

object Contract {
    private const val TABLE_NAME = "table_favorite_user"
    private const val SCHEME = "content"
    private const val AUTHORITY = "com.mfathurz.githubuser"

    val CONTENT_URI: Uri= Uri.Builder().scheme(SCHEME)
        .authority(AUTHORITY)
        .appendPath(TABLE_NAME)
        .build()

    const val AVATAR = "avatar"
    const val COMPANY = "company"
    const val LOCATION= "location"
    const val NAME ="name"
    const val REPOSITORY= "repository"
    const val USERNAME ="username"
}