package com.mfathurz.githubuser.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Binder
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import coil.Coil
import coil.request.GetRequest
import com.mfathurz.githubuser.R
import com.mfathurz.githubuser.db.Contract.CONTENT_URI
import com.mfathurz.githubuser.helper.Helpers
import com.mfathurz.githubuser.model.User
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class StackRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val favoriteUserImage = ArrayList<Bitmap?>()
    private var listFavUser = listOf<User>()

    override fun onCreate() {
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(p0: Int): Long = 0

    override fun onDataSetChanged() {

        val identityToken = Binder.clearCallingIdentity()
        val cursor = context.contentResolver.query(CONTENT_URI, null, null, null, null)
        Binder.restoreCallingIdentity(identityToken)
        listFavUser = Helpers.mapCursorToList(cursor)

        listFavUser.forEach {
            val imageLoader = Coil.imageLoader(context)
            val request = GetRequest.Builder(context)
                .data(it.avatar)
                .build()

            runBlocking {
                val deferredDrawable = async(IO) { imageLoader.execute(request).drawable as Drawable }
                val drawable = deferredDrawable.await()
                val bitmap = drawable.toBitmap(300,300)
                favoriteUserImage.add(bitmap)
                Log.d("userBitmap", favoriteUserImage.toString())
            }
        }

    }

    override fun hasStableIds(): Boolean = false

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)

        rv.setImageViewBitmap(R.id.favorite_user_img_view, favoriteUserImage[position])

        val extras = bundleOf(
            FavoriteUserGithubWidget.EXTRA_ITEM to listFavUser[position].name
        )

        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.favorite_user_img_view, fillInIntent)
        return rv
    }

    override fun getCount(): Int = favoriteUserImage.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {
    }

}