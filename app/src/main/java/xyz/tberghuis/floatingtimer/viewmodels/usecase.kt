package xyz.tberghuis.floatingtimer.viewmodels

import android.app.Application
import kotlinx.coroutines.flow.first
import xyz.tberghuis.floatingtimer.data.preferencesRepository
import xyz.tberghuis.floatingtimer.service.FloatingService
import xyz.tberghuis.floatingtimer.service.boundFloatingServiceProvider

suspend fun shouldShowPremiumDialogMultipleTimers(application: Application): Boolean {
  val boundFloatingService = application.boundFloatingServiceProvider
  val floatingService = boundFloatingService.provideService()
  return shouldShowPremiumDialogMultipleTimers(floatingService)
}

// this is also called from processDeeplink()
suspend fun shouldShowPremiumDialogMultipleTimers(fs: FloatingService): Boolean {
  val premiumPurchased =
    fs.application.preferencesRepository.haloColourPurchasedFlow.first()
  val numBubbles = fs.overlayController.getNumberOfBubbles()
  return !premiumPurchased && numBubbles == 2
}