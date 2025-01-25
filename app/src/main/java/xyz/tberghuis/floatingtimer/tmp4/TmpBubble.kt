package xyz.tberghuis.floatingtimer.tmp4

import android.util.Log
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import xyz.tberghuis.floatingtimer.data.SavedCountdown
import xyz.tberghuis.floatingtimer.data.SavedStopwatch
import xyz.tberghuis.floatingtimer.data.SavedTimer
import xyz.tberghuis.floatingtimer.data.appDatabase
import xyz.tberghuis.floatingtimer.service.BubbleProperties
import xyz.tberghuis.floatingtimer.service.FloatingService
import xyz.tberghuis.floatingtimer.service.TimerViewHolder

// todo final override methods in constructor

abstract class TmpBubble(
  private val service: FloatingService,
  bubbleSizeScaleFactor: Float,
  override val haloColor: Color,
  override val timerShape: String,
  override val label: String? = null,
  override val isBackgroundTransparent: Boolean,
  private var savedTimer: SavedTimer? = null
) : BubbleProperties {
  override val arcWidth = TmpBubbleProperties.calcArcWidth(bubbleSizeScaleFactor)
  override val fontSize = TmpBubbleProperties.calcFontSize(bubbleSizeScaleFactor)
  val viewHolder: TimerViewHolder

  init {
    // todo TmpTimerViewHolder
    // remove widthPx heightPx params
    viewHolder =
      TimerViewHolder(service, 0, 0, savedTimer?.positionX, savedTimer?.positionY)
  }

  open fun exit() {
    try {
      service.ftWindowManager.removeView(viewHolder.view)
    } catch (e: IllegalArgumentException) {
      Log.e("Bubble", "IllegalArgumentException $e")
    }
  }

  abstract fun reset()
  abstract fun onTap()

  fun saveTimerPositionIfNull() {
    savedTimer?.let {
      if (it.positionX == null)
        saveTimerPosition()
    }
  }

  fun saveTimerPosition() {
    service.scope.launch(IO) {
      savedTimer = savedTimer?.let {
        when (it) {
          is SavedStopwatch -> {
            it.copy(
              positionX = viewHolder.params.x,
              positionY = viewHolder.params.y
            ).also { savedStopwatch ->
              service.application.appDatabase.savedStopwatchDao().update(savedStopwatch)
            }
          }

          is SavedCountdown -> {
            it.copy(
              positionX = viewHolder.params.x,
              positionY = viewHolder.params.y
            ).also { savedCountdown ->
              service.application.appDatabase.savedCountdownDao().update(savedCountdown)
            }
          }

          else -> {
            it
          }
        }
      }
    }
  }
}