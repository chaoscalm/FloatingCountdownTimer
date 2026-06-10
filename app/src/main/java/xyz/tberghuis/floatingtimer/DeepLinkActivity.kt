package xyz.tberghuis.floatingtimer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import xyz.tberghuis.floatingtimer.service.FloatingService

class DeepLinkActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    intent?.data?.let { data ->
      startFloatingServiceForDeeplink(this, data)
    }
    finishAndRemoveTask()
  }
}

const val INTENT_COMMAND_DEEPLINK = "command_deeplink"

fun startFloatingServiceForDeeplink(context: Context, data: Uri) {
  val intent = Intent(context, FloatingService::class.java).apply {
    putExtra(INTENT_COMMAND, INTENT_COMMAND_DEEPLINK)
    this.data = data
  }
  context.startForegroundService(intent)
}