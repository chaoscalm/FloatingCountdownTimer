package xyz.tberghuis.floatingtimer.tmp.tmp02

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import xyz.tberghuis.floatingtimer.logd
import android.provider.Settings
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch
import xyz.tberghuis.floatingtimer.R
import xyz.tberghuis.floatingtimer.data.SavedStopwatchDao
import xyz.tberghuis.floatingtimer.data.appDatabase
import xyz.tberghuis.floatingtimer.viewmodels.shouldShowPremiumDialogMultipleTimers

fun tmp_process_uri(data: Uri, fs: FloatingService) {
  logd("tmp_process_uri data $data")

  val timerType = data.getQueryParameter("type")
  val id = data.getQueryParameter("id")
  val start = data.getBooleanQueryParameter("start", false)

  if (!Settings.canDrawOverlays(fs.application)) {
// todo error Toast 
    return
  }
  if (timerType == null || id == null) {
    // todo error Toast
    return
  }

  fs.scope.launch {
//    if (shouldShowPremiumDialogMultipleTimers(fs.application)) {
//      // todo
////      uiResult = application.getString(R.string.need_premium_to_run_more_than_2_timers)
//      return@launch
//    }


    if (should_Show_Premium_Dialog_Multiple_Timers(fs)) {
      display_toast_message(fs, fs.getString(R.string.premium_reason_multiple_timers))
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
          // todo
//          uiResult = application.getString(R.string.invalid_timer_type)
        }
      }
    } catch (e: RuntimeException) {
      // todo
//      uiResult = "error $e"
    }

//    delay(1000)
//    val numTimers =
//      application.boundFloatingServiceProvider.provideService().overlayController.getNumberOfBubbles()
//    logd("numTimers $numTimers")
//    if (numTimers == 0) {
//      application.boundFloatingServiceProvider.provideService().stopSelf()
//    }
  }


}


private suspend fun addStopwatch(id: Int, start: Boolean, fs: FloatingService) {
  val stopwatch =
    fs.application.appDatabase.savedStopwatchDao().getById(id)

  if (stopwatch == null) {
    // todo
//    uiResult = "stopwatch timer id=$id not found"
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

//  uiResult = application.getString(R.string.stopwatch_timer_launched)
}


private suspend fun addCountdown(id: Int, start: Boolean, fs: FloatingService) {
  logd("addCountdown")
  val countdown = fs.application.appDatabase.savedCountdownDao().getById(id)

  if (countdown == null) {
//      uiResult = "countdown timer id=$id not found"
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

fun display_toast_message(context: Context, message: String) {
  Handler(Looper.getMainLooper()).post {
    Toast.makeText(
      context,
      message,
      Toast.LENGTH_SHORT
    ).show()
  }
}
