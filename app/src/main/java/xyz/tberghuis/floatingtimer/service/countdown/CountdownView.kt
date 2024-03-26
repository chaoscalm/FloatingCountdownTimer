package xyz.tberghuis.floatingtimer.service.countdown

import android.content.Context
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntSize
import xyz.tberghuis.floatingtimer.composables.TimerLabelView
import xyz.tberghuis.floatingtimer.composables.TimerRectView
import xyz.tberghuis.floatingtimer.logd
import xyz.tberghuis.floatingtimer.service.BubbleProperties
import xyz.tberghuis.floatingtimer.tmp4.LocalTimerViewHolder

@Composable
fun CountdownView(countdown: Countdown) {
  val timeLeftFraction = countdown.countdownSeconds / countdown.durationSeconds.toFloat()
  val timerState = countdown.timerState.collectAsState()
  val isPaused by remember {
    derivedStateOf {
      timerState.value == TimerStatePaused
    }
  }
  CountdownView(countdown, timeLeftFraction, countdown.countdownSeconds, isPaused)
}

@Composable
fun CountdownView(
  bubbleProperties: BubbleProperties,
  timeLeftFraction: Float,
  countdownSeconds: Int,
  isPaused: Boolean
) {
  when (bubbleProperties.timerShape) {
    "circle" -> {
      CountdownCircleView(
        bubbleProperties = bubbleProperties,
        timeLeftFraction = timeLeftFraction,
        countdownSeconds = countdownSeconds,
        isPaused = isPaused
      )
    }

    "rectangle" -> {
      TimerRectView(
        isPaused = isPaused,
        widthDp = bubbleProperties.widthDp,
        heightDp = bubbleProperties.heightDp,
        arcWidth = bubbleProperties.arcWidth,
        haloColor = bubbleProperties.haloColor,
        timeElapsed = countdownSeconds,
        timeLeftFraction = timeLeftFraction,
        fontSize = bubbleProperties.fontSize,
      )
    }

    "label" -> {
      val tvh = LocalTimerViewHolder.current
      val updateViewLayout = tvh?.let {
        { size: IntSize ->
          val windowManager = tvh.service.getSystemService(Context.WINDOW_SERVICE) as WindowManager
          logd("runOnceOnGloballyPositioned $size")
          tvh.params.width = size.width
          tvh.params.height = size.height
          windowManager.updateViewLayout(tvh.view, tvh.params)
        }
      }
      TimerLabelView(
        isPaused,
        bubbleProperties.arcWidth,
        bubbleProperties.haloColor,
        countdownSeconds,
        timeLeftFraction,
        bubbleProperties.fontSize,
        bubbleProperties.label,
        updateViewLayout
      )
    }

    else -> {
      throw RuntimeException("invalid timer shape")
    }
  }
}