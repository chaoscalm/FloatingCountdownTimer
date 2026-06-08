package xyz.tberghuis.floatingtimer.tmp.tmp02

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import androidx.core.net.toUri
import android.graphics.drawable.Icon
import androidx.core.content.getSystemService
import xyz.tberghuis.floatingtimer.R
import xyz.tberghuis.floatingtimer.data.SavedCountdown
import xyz.tberghuis.floatingtimer.data.SavedStopwatch
import xyz.tberghuis.floatingtimer.data.SavedTimer
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
