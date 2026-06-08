package xyz.tberghuis.floatingtimer.tmp.tmp02

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import androidx.core.net.toUri
import android.graphics.drawable.Icon
import androidx.core.content.getSystemService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import xyz.tberghuis.floatingtimer.R
import xyz.tberghuis.floatingtimer.data.SavedCountdown
import xyz.tberghuis.floatingtimer.data.SavedStopwatch
import xyz.tberghuis.floatingtimer.data.SavedTimer
import xyz.tberghuis.floatingtimer.data.appDatabase
import xyz.tberghuis.floatingtimer.logd
import xyz.tberghuis.floatingtimer.viewmodels.toDeepLink


fun add_to_homescreen(context: Context, savedTimer: SavedTimer, autoStart: Boolean) {

  val shortcut = savedTimer.toShortcutInfo(context, autoStart)
  val shortcutManager = context.getSystemService<ShortcutManager>()

  if (shortcutManager?.isRequestPinShortcutSupported == true) {
    shortcutManager.requestPinShortcut(
      shortcut,
      null
    )
  }
}

suspend fun delete_saved_timer(savedTimer: SavedTimer, application: Application) {
  logd("delete_saved_timer")

  val shortcutManager = application.getSystemService<ShortcutManager>()

  val shortcutId = savedTimer.toShortcutId()
//  shortcutManager?.removeDynamicShortcuts(listOf(shortcutId))
  shortcutManager?.disableShortcuts(listOf(shortcutId))

  when (savedTimer) {
    is SavedStopwatch -> {
      application.appDatabase.savedStopwatchDao().delete(savedTimer)
    }

    is SavedCountdown -> {
      application.appDatabase.savedCountdownDao().delete(savedTimer)
    }
  }

}