package xyz.tberghuis.floatingtimer.composables

import androidx.compose.runtime.compositionLocalOf
import xyz.tberghuis.floatingtimer.service.FloatingService
import xyz.tberghuis.floatingtimer.service.TimerViewHolder
import xyz.tberghuis.floatingtimer.tmp4.TmpTimerViewHolder

val LocalFloatingService = compositionLocalOf<FloatingService> {
  error("CompositionLocal LocalFloatingService not present")
}

val LocalTimerViewHolder = compositionLocalOf<TimerViewHolder?> {
  null
}

//val LocalTimerViewHolder = compositionLocalOf<TmpTimerViewHolder?> {
//  null
//}