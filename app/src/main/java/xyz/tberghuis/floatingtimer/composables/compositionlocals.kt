package xyz.tberghuis.floatingtimer.composables

import androidx.compose.runtime.compositionLocalOf
import xyz.tberghuis.floatingtimer.service.TimerViewHolder

val LocalFloatingService = compositionLocalOf<xyz.tberghuis.floatingtimer.tmp.tmp02.FloatingService> {
  error("CompositionLocal LocalFloatingService not present")
}

val LocalTimerViewHolder = compositionLocalOf<TimerViewHolder?> {
  null
}