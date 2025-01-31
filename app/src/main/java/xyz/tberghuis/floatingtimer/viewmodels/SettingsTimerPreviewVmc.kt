package xyz.tberghuis.floatingtimer.viewmodels

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import xyz.tberghuis.floatingtimer.service.BubbleProperties

class SettingsTimerPreviewVmc(
  initialScale: Float,
  initialHaloColor: Color,
  override val timerShape: String,
  override val label: String? = null,
  override val isBackgroundTransparent: Boolean
) : BubbleProperties {
  var bubbleSizeScaleFactor by mutableFloatStateOf(initialScale) // 0<=x<=1
  override var haloColor by mutableStateOf(initialHaloColor)
  override val arcWidth by derivedStateOf {
    BubbleProperties.calcArcWidth(bubbleSizeScaleFactor)
  }
  override val paddingTimerDisplay by derivedStateOf {
    BubbleProperties.calcTimerDisplayPadding(bubbleSizeScaleFactor)
  }
  override val fontSize by derivedStateOf {
    BubbleProperties.calcFontSize(bubbleSizeScaleFactor)
  }
}