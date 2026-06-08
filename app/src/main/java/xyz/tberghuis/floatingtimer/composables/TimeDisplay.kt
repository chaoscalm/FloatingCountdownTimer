package xyz.tberghuis.floatingtimer.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.TextUnit

// converts seconds to X:XX
fun Int.toTimeDisplay(): String {
  fun Int.padZero(): String {
    return toString().padStart(2, '0')
  }

  val totalSeconds = this
  val hours = totalSeconds / (3600)
  val minutes = (totalSeconds % (3600)) / 60
  val seconds = totalSeconds % 60
  var text = "${minutes.padZero()}:${seconds.padZero()}"
  if (hours > 0) {
    text = "${hours.padZero()}:$text"
  }
  return text
}

@Composable
fun TimeDisplay(
  totalSeconds: Int,
  fontSize: TextUnit,
  isBackgroundTransparent: Boolean
) {
//  val hours = totalSeconds / (3600)
//  val minutes = (totalSeconds % (3600)) / 60
//  val seconds = totalSeconds % 60
//  var text = "${formatIntTimerDisplay(minutes)}:${formatIntTimerDisplay(seconds)}"
//  if (hours > 0) {
//    text = "${formatIntTimerDisplay(hours)}:$text"
//  }
  val text = totalSeconds.toTimeDisplay()
  TimerText(
    text,
    fontSize,
    isBackgroundTransparent
  )
}