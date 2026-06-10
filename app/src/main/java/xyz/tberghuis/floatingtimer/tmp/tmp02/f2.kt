package xyz.tberghuis.floatingtimer.tmp.tmp02

import android.app.Application
import android.content.Context
import android.content.pm.ShortcutManager
import androidx.core.content.getSystemService
import kotlinx.coroutines.flow.first
import xyz.tberghuis.floatingtimer.R
import xyz.tberghuis.floatingtimer.data.SavedCountdown
import xyz.tberghuis.floatingtimer.data.SavedStopwatch
import xyz.tberghuis.floatingtimer.data.SavedTimer
import xyz.tberghuis.floatingtimer.data.appDatabase
import xyz.tberghuis.floatingtimer.data.preferencesRepository
import xyz.tberghuis.floatingtimer.logd
import xyz.tberghuis.floatingtimer.service.FloatingService


suspend fun delete_saved_timer(savedTimer: SavedTimer, application: Application) {
  logd("delete_saved_timer")
  val shortcutManager = application.getSystemService<ShortcutManager>()
  val shortcutId = savedTimer.toShortcutId()
  shortcutManager?.disableShortcuts(
    listOf(shortcutId),
    application.getString(R.string.this_timer_has_been_deleted)
  )
  when (savedTimer) {
    is SavedStopwatch -> {
      application.appDatabase.savedStopwatchDao().delete(savedTimer)
    }

    is SavedCountdown -> {
      application.appDatabase.savedCountdownDao().delete(savedTimer)
    }
  }
}

