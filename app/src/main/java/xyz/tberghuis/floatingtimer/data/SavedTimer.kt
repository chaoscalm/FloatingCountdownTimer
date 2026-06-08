package xyz.tberghuis.floatingtimer.data

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.graphics.drawable.Icon
import androidx.core.net.toUri
import xyz.tberghuis.floatingtimer.R
import xyz.tberghuis.floatingtimer.composables.toTimeDisplay
import xyz.tberghuis.floatingtimer.viewmodels.toDeepLink

interface SavedTimer {
  val id: Int
  val timerShape: String
  val timerColor: Int
  val label: String?
  val isBackgroundTransparent: Boolean
  val positionX: Int?
  val positionY: Int?

  fun toShortcutId() = buildString {
    when (this@SavedTimer) {
      is SavedCountdown -> {
        append("countdown")
      }

      is SavedStopwatch -> {
        append("stopwatch")
      }
    }
    append("-${id}")
  }

  fun toShortcutInfo(context: Context, autoStart: Boolean): ShortcutInfo {
    val savedTimer = this
    val _label = buildString {
      when (savedTimer) {
        is SavedCountdown -> {
          if (label != null) {
            append("$label - ${savedTimer.durationSeconds.toTimeDisplay()}")
          } else {
            append(savedTimer.durationSeconds.toTimeDisplay())
          }
        }

        is SavedStopwatch -> {
          if (label != null) {
            append("$label - Stopwatch")
          } else {
            append("Stopwatch")
          }
        }
      }
    }

    val shortcut = ShortcutInfo.Builder(context, toShortcutId())
      .setShortLabel(_label)
      .setLongLabel(_label)
      .setIcon(Icon.createWithResource(context, R.drawable.ic_launcher_foreground))
      .setIntent(
//      intent
        Intent(
          Intent.ACTION_VIEW,
          savedTimer.toDeepLink(autoStart)
//          "mydeeplink://mydeeplink/mypath?id=1".toUri()
        )
      )
      .build()
    return shortcut
  }
}