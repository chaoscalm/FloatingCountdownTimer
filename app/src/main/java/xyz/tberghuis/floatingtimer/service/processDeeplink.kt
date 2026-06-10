package xyz.tberghuis.floatingtimer.service

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import xyz.tberghuis.floatingtimer.logd
import android.provider.Settings
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
import xyz.tberghuis.floatingtimer.R
import xyz.tberghuis.floatingtimer.data.appDatabase
import xyz.tberghuis.floatingtimer.tmp.tmp02.should_Show_Premium_Dialog_Multiple_Timers

fun processDeeplink(data: Uri, fs: FloatingService) {
  logd("processDeeplink data $data")

  val timerType = data.getQueryParameter("type")
  val id = data.getQueryParameter("id")
  val start = data.getBooleanQueryParameter("start", false)

  if (!Settings.canDrawOverlays(fs.application)) {
    displayToastMessage(fs, fs.getString(R.string.dialog_enable_overlay_permission))
    fs.stopSelf()
    return
  }
  if (timerType == null || id == null) {
    displayToastMessage(fs, "invalid link $data")
    stopServiceIfNoTimers(fs)
    return
  }

  fs.scope.launch {
    if (should_Show_Premium_Dialog_Multiple_Timers(fs)) {
      displayToastMessage(fs, fs.getString(R.string.premium_reason_multiple_timers))
      return@launch
    }

    try {
      when (timerType) {
        "stopwatch" -> {
          addStopwatch(id.toInt(), start, fs)
        }

        "countdown" -> {
          addCountdown(id.toInt(), start, fs)
        }

        else -> {
          displayToastMessage(fs, "invalid link $data")
          stopServiceIfNoTimers(fs)
        }
      }
    } catch (e: RuntimeException) {
      displayToastMessage(fs, "error $e")
      stopServiceIfNoTimers(fs)
    }
  }
}

private suspend fun addStopwatch(id: Int, start: Boolean, fs: FloatingService) {
  val stopwatch =
    fs.application.appDatabase.savedStopwatchDao().getById(id)

  if (stopwatch == null) {
    displayToastMessage(fs, "saved stopwatch $id not found")
    stopServiceIfNoTimers(fs)
    return
  }

  fs.overlayController.addStopwatch(
    haloColor = Color(stopwatch.timerColor),
    timerShape = stopwatch.timerShape,
    label = stopwatch.label,
    isBackgroundTransparent = stopwatch.isBackgroundTransparent,
    savedTimer = stopwatch,
    start = start
  )
}

private suspend fun addCountdown(id: Int, start: Boolean, fs: FloatingService) {
  logd("addCountdown")
  val countdown = fs.application.appDatabase.savedCountdownDao().getById(id)

  if (countdown == null) {
    displayToastMessage(fs, "saved countdown $id not found")
    stopServiceIfNoTimers(fs)
    return
  }

  fs.overlayController.addCountdown(
    durationSeconds = countdown.durationSeconds,
    haloColor = Color(countdown.timerColor),
    timerShape = countdown.timerShape,
    label = countdown.label,
    isBackgroundTransparent = countdown.isBackgroundTransparent,
    savedTimer = countdown,
    start = start
  )
}

private fun displayToastMessage(context: Context, message: String) {
  Handler(Looper.getMainLooper()).post {
    Toast.makeText(
      context,
      message,
      Toast.LENGTH_LONG
    ).show()
  }
}

private fun stopServiceIfNoTimers(fs: FloatingService) {
  fs.scope.launch {
    if (fs.overlayController.getNumberOfBubbles() == 0) {
      fs.stopSelf()
    }
  }
}