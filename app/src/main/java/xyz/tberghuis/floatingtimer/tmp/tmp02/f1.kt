package xyz.tberghuis.floatingtimer.tmp.tmp02

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
import xyz.tberghuis.floatingtimer.service.FloatingService






fun display_toast_message(context: Context, message: String) {
  Handler(Looper.getMainLooper()).post {
    Toast.makeText(
      context,
      message,
      Toast.LENGTH_LONG
    ).show()
  }
}

fun stop_service_no_timers(fs: FloatingService) {
  fs.scope.launch {
    if (fs.overlayController.getNumberOfBubbles() == 0) {
      fs.stopSelf()
    }
  }
}