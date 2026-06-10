package xyz.tberghuis.floatingtimer.viewmodels

import android.app.Application
import kotlinx.coroutines.flow.first
import xyz.tberghuis.floatingtimer.data.preferencesRepository
import xyz.tberghuis.floatingtimer.service.FloatingService
import xyz.tberghuis.floatingtimer.service.boundFloatingServiceProvider

suspend fun shouldShowPremiumDialogMultipleTimers(application: Application): Boolean {
  val boundFloatingService = application.boundFloatingServiceProvider
  val floatingService = boundFloatingService.provideService()
  return should_Show_Premium_Dialog_Multiple_Timers(floatingService)
}

suspend fun should_Show_Premium_Dialog_Multiple_Timers(fs: FloatingService): Boolean {
  val premiumPurchased =
    fs.application.preferencesRepository.haloColourPurchasedFlow.first()
  val numBubbles = fs.overlayController.getNumberOfBubbles()
  return !premiumPurchased && numBubbles == 2
}