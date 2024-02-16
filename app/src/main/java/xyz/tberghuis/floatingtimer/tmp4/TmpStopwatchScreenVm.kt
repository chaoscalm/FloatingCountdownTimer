package xyz.tberghuis.floatingtimer.tmp4

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.tberghuis.floatingtimer.DEFAULT_HALO_COLOR
import xyz.tberghuis.floatingtimer.MainApplication
import xyz.tberghuis.floatingtimer.providePreferencesRepository
import xyz.tberghuis.floatingtimer.viewmodels.PremiumVmc

class TmpStopwatchScreenVm(
  private val application: Application,
) : AndroidViewModel(application) {


//  var showDeleteDialog by mutableStateOf<TmpSavedStopwatch?>(null)


  private val preferencesRepository = application.providePreferencesRepository()
  val premiumVmc = PremiumVmc(application, viewModelScope)
  private val boundFloatingService = (application as MainApplication).boundFloatingService

  var haloColor by mutableStateOf(DEFAULT_HALO_COLOR)

  init {
    viewModelScope.launch {
      preferencesRepository.haloColourFlow.collect {
        haloColor = it
      }
    }
  }

  fun stopwatchButtonClick() {
    viewModelScope.launch {
      if (shouldShowPremiumDialogMultipleTimers(application)) {
        premiumVmc.showPurchaseDialog = true
        return@launch
      }
      boundFloatingService.provideFloatingService().overlayController.addStopwatch(
        haloColor
      )
    }
  }

}