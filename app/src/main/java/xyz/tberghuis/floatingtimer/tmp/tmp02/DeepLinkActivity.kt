package xyz.tberghuis.floatingtimer.tmp.tmp02

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.tberghuis.floatingtimer.INTENT_COMMAND
import xyz.tberghuis.floatingtimer.INTENT_COMMAND_EXIT
import xyz.tberghuis.floatingtimer.screens.DeepLinkScreen
import xyz.tberghuis.floatingtimer.viewmodels.DeepLinkScreenVm
import xyz.tberghuis.floatingtimer.ui.theme.FloatingTimerTheme

class DeepLinkActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    run {
      startFloatingServiceForDeeplink(this, intent?.data ?: return@run)
    }

    finishAndRemoveTask()


//    enableEdgeToEdge()
//    setContent {
//      val vm: DeepLinkScreenVm = viewModel()
//      val activity = LocalActivity.current
//      // hack to only run once even if configuration change
//      rememberSaveable(vm) {
//        activity?.intent?.data?.let {
//          vm.processDataUri(it)
//        }
//        ""
//      }
//      FloatingTimerTheme {
//        DeepLinkScreen()
//      }
//    }
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

