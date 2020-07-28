package com.mfathurz.githubuser.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.net.toUri
import com.mfathurz.githubuser.R

class FavoriteUserGithubWidget : AppWidgetProvider() {

    companion object {
        private const val TOAST_ACTION = "com.mfathurz.githubuser.TOAST_ACTION"
        const val EXTRA_ITEM = "com.mfathurz.githubuser.EXTRA_ITEM"
        const val UPDATE_WIDGET = "com.mfathurz.githubuser.UPDATE_WIDGET"

        private fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {

            val intent = Intent(
                context,
                StackWidgetService::class.java
            )
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val views = RemoteViews(
                context.packageName,
                R.layout.favorite_user_github_widget
            )
            views.setRemoteAdapter(R.id.stack_view, intent)

            views.setEmptyView(
                R.id.stack_view,
                R.id.empty_view
            )

            val toastIntent = Intent(
                context,
                FavoriteUserGithubWidget::class.java
            )
            toastIntent.action =
                TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val toastPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                toastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, views)

        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action != null) {
            Log.d("updateWidgetonReceive", "hello")
            when (intent.action) {
                TOAST_ACTION -> {
                    val username = intent.getStringExtra(EXTRA_ITEM)

                    Toast.makeText(context, username, Toast.LENGTH_SHORT).show()
                }

                UPDATE_WIDGET -> {

                    Log.d("updateWidget", "hello")

                    val componentName = ComponentName(
                        context.applicationContext,
                        FavoriteUserGithubWidget::class.java
                    )

                    val ids = AppWidgetManager.getInstance(context.applicationContext)
                        .getAppWidgetIds(componentName)

                    ids.forEach { id ->
                        AppWidgetManager.getInstance(context.applicationContext)
                            .notifyAppWidgetViewDataChanged(
                                id,
                                R.id.stack_view
                            )
                    }
                }
            }
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

