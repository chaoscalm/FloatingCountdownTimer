package xyz.tberghuis.floatingtimer.viewmodels

import android.app.Application
import android.content.pm.ShortcutManager
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
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

class SavedTimerDialogVmc(
  private val application: Application,
  private val scope: CoroutineScope
) {
  // stores SavedCountdown or SavedStopwatch of saved timer long press
  // close dialog = null
  var showOptionsDialog by mutableStateOf<SavedTimer?>(null)

  var start by mutableStateOf(true)

  fun deepLinkToClipboard(
    clipboardManager: ClipboardManager
  ) {
//    assert(showLinkDialog != null)
    val deepLink = showOptionsDialog?.toDeepLink(start).toString()
    clipboardManager.setText(AnnotatedString(deepLink))
    showOptionsDialog = null
  }

  fun deleteSavedTimer() {
    logd("deleteSavedTimer")
    scope.launch(IO) {
      showOptionsDialog?.let { timer ->
        deleteSavedTimer(timer)
        showOptionsDialog = null
      }
    }
  }

  fun addToHomescreen(savedTimer: SavedTimer, autoStart: Boolean) {
    val shortcut = savedTimer.toShortcutInfo(application, autoStart)
    val shortcutManager = application.getSystemService<ShortcutManager>()
    if (shortcutManager?.isRequestPinShortcutSupported == true) {
      shortcutManager.requestPinShortcut(
        shortcut,
        null
      )
    }
    showOptionsDialog = null
  }


  private suspend fun deleteSavedTimer(savedTimer: SavedTimer) {
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
}

fun SavedTimer.toDeepLink(start: Boolean): Uri {
  val type = when (this) {
    is SavedStopwatch -> {
      "stopwatch"
    }

    is SavedCountdown -> {
      "countdown"
    }

    else -> {
      throw RuntimeException("invalid saved timer type")
    }
  }
  val id = id.toString()
  return Uri.Builder().apply {
    scheme("floatingtimer")
    authority("floatingtimer")
    appendQueryParameter("type", type)
    appendQueryParameter("id", id)
    if (start) {
      appendQueryParameter("start", "1")
    }
  }.build()
}