package xyz.tberghuis.floatingtimer.service.countdown

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.zIndex
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import xyz.tberghuis.floatingtimer.service.BubbleProperties
import xyz.tberghuis.floatingtimer.tmp4.TmpTimeDisplay

@Composable
fun CountdownCircleView(
  bubbleProperties: BubbleProperties,
  timeLeftFraction: Float,
  countdownSeconds: Int,
  isPaused: Boolean,
  isBackgroundTransparent: Boolean
) {
  Box(
    modifier = Modifier
      .size(bubbleProperties.widthDp)
      .padding(bubbleProperties.arcWidth / 2)
      .zIndex(1f),
    contentAlignment = Alignment.Center
  ) {
    CountdownProgressArc(
      timeLeftFraction,
      bubbleProperties.arcWidth,
      bubbleProperties.haloColor,
      isBackgroundTransparent
    )
    if (isPaused) {
      Icon(
        Icons.Filled.PlayArrow,
        contentDescription = "paused",
        modifier = Modifier.fillMaxSize(),
        tint = Color.LightGray
      )
    }
    TmpTimeDisplay(countdownSeconds, bubbleProperties.fontSize, isBackgroundTransparent)
  }
}

@Composable
fun CountdownProgressArc(
  timeLeftFraction: Float, arcWidth: Dp, haloColor: Color,
  isBackgroundTransparent: Boolean
) {
  val sweepAngle = 360 * timeLeftFraction
  Canvas(
    Modifier.fillMaxSize()
  ) {
    if (!isBackgroundTransparent) {
      drawCircle(
        color = Color.White,
      )
      drawArc(
        color = Color.White,
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = false,
        style = Stroke(arcWidth.toPx()),
        size = Size(size.width, size.height)
      )
    }
    drawArc(
      color = haloColor.copy(alpha = .1f),
      startAngle = 0f,
      sweepAngle = 360f,
      useCenter = false,
      style = Stroke(arcWidth.toPx()),
      size = Size(size.width, size.height)
    )
    drawArc(
      color = haloColor,
      -90f,
      sweepAngle,
      false,
      style = Stroke(arcWidth.toPx()),
      size = Size(size.width, size.height)
    )
  }
}