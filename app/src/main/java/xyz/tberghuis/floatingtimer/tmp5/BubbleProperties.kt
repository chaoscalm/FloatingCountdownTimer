package xyz.tberghuis.floatingtimer.tmp5

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import xyz.tberghuis.floatingtimer.ARC_WIDTH_NO_SCALE
import xyz.tberghuis.floatingtimer.TIMER_FONT_SIZE_NO_SCALE

// put in constants
val TIMER_WIDTH_NO_SCALE = 60.dp

val RECT_TIMER_HEIGHT_NO_SCALE = 50.dp
val RECT_TIMER_HEIGHT_MAX_SCALE = 72.dp

// future data class implements this, use composition over inheritance
// make update method a single method interface, does calcs and returns copy instance

// state that is static for lifetime of bubble goes here
interface BubbleProperties {
  val widthDp: Dp
  val heightDp: Dp

  val arcWidth: Dp
  val fontSize: TextUnit
  val haloColor: Color

  val timerShape: String

  companion object {
    fun calcWidthDp(scaleFactor: Float) = TIMER_WIDTH_NO_SCALE * (scaleFactor + 1)
    fun calcRectHeightDp(scaleFactor: Float): Dp {
      return (RECT_TIMER_HEIGHT_MAX_SCALE - RECT_TIMER_HEIGHT_NO_SCALE) * scaleFactor + RECT_TIMER_HEIGHT_NO_SCALE
    }
    fun calcArcWidth(scaleFactor: Float) = ARC_WIDTH_NO_SCALE * (0.9f * scaleFactor + 1)
    fun calcFontSize(scaleFactor: Float) = TIMER_FONT_SIZE_NO_SCALE * (1.2 * scaleFactor + 1)
  }
}