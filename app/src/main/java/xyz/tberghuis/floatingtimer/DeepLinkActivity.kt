package xyz.tberghuis.floatingtimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.tberghuis.floatingtimer.screens.DeepLinkScreen
import xyz.tberghuis.floatingtimer.viewmodels.DeepLinkScreenVm
import xyz.tberghuis.floatingtimer.ui.theme.FloatingTimerTheme

class DeepLinkActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val vm: DeepLinkScreenVm = viewModel()
      val activity = LocalActivity.current
      // hack to only run once even if configuration change
      rememberSaveable(vm) {
        activity?.intent?.data?.let {
          vm.processDataUri(it)
        }
        ""
      }
      FloatingTimerTheme {
        DeepLinkScreen()
      }
    }
  }
}