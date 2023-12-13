package xyz.tberghuis.floatingtimer.tmp2

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import xyz.tberghuis.floatingtimer.providePreferencesRepository
import xyz.tberghuis.floatingtimer.viewmodels.PremiumVmc

class ChangeSizeViewModel(private val application: Application) : AndroidViewModel(application) {
  private val preferences = application.providePreferencesRepository()

  // doitwrong
  var initialised by mutableStateOf(false)
  lateinit var settingsTimerPreviewVmc: SettingsTimerPreviewVmc

  val premiumVmc = PremiumVmc(application, viewModelScope)
  private val premiumFlow = application.providePreferencesRepository().haloColourPurchasedFlow

  init {
    viewModelScope.launch {
      settingsTimerPreviewVmc = SettingsTimerPreviewVmc(preferences.bubbleScaleFlow.first())
      initialised = true
    }
  }

  // todo datastore
  //  paywall dialog

  fun saveChangeSize() {
    viewModelScope.launch {
      preferences.updateBubbleScale(settingsTimerPreviewVmc.bubbleSizeScaleFactor)
    }
  }

  // doitwrong, lets just repeat myself
  fun saveChangeSizeClick() {
    viewModelScope.launch {
      if (premiumFlow.first()) {
        saveChangeSize()
      } else {
        premiumVmc.showPurchaseDialog = true
      }
    }
  }


}