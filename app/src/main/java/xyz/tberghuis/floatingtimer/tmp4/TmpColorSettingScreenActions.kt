package xyz.tberghuis.floatingtimer.tmp4

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.tberghuis.floatingtimer.LocalNavController
import xyz.tberghuis.floatingtimer.R
import xyz.tberghuis.floatingtimer.composables.PremiumDialog
import xyz.tberghuis.floatingtimer.viewmodels.ColorSettingViewModel

@Composable
fun TmpColorSettingScreenActions(
  vm: ColorSettingViewModel = viewModel()
) {
  val nav = LocalNavController.current
  val ifPremiumCallback = when (vm.timerType) {
    is String -> {
      {
        nav.previousBackStackEntry
          ?.savedStateHandle
          ?.set("color_result", vm.settingsTimerPreviewVmc.haloColor.toArgb())
        nav.popBackStack()
      }
    }

    else -> {
      {
        vm.saveDefaultHaloColor()
        // todo, just collect default in each screen VM
//        homeVm.countdownHaloColor = vm.settingsTimerPreviewVmc.haloColor
//        homeVm.stopwatchHaloColor = vm.settingsTimerPreviewVmc.haloColor
        nav.popBackStack()
      }
    }
  }

  Row(
    horizontalArrangement = Arrangement.spacedBy(10.dp),
  ) {
    Button(onClick = {
      nav.popBackStack()
    }) {
      Text(stringResource(R.string.cancel).uppercase())
    }
    Button(onClick = {
      vm.okButtonClick {
        ifPremiumCallback()
      }
    }) {
      Text(stringResource(R.string.save).uppercase())
    }
  }

  PremiumDialog(vm.premiumVmc, stringResource(R.string.premium_reason_halo_colour)) {
    ifPremiumCallback()
  }
}