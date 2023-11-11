package xyz.tberghuis.floatingtimer.tmp2

import android.graphics.PixelFormat
import android.util.Log
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// todo move windowManager to service
class TrashController(
  val windowManager: WindowManager,
  private val service: FloatingService,
) {
  val isBubbleDragging = MutableStateFlow(false)
  val bubbleDraggingPosition = MutableStateFlow(IntOffset.Zero)

  private var overlay: ComposeView? = null
  private val params: WindowManager.LayoutParams = WindowManager.LayoutParams(
    WindowManager.LayoutParams.MATCH_PARENT,
    WindowManager.LayoutParams.MATCH_PARENT,
    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
    PixelFormat.TRANSLUCENT
  )

  init {
    service.scope.launch(Dispatchers.Main) {
      addRemoveTrashOverlay()
    }
  }

  private suspend fun addRemoveTrashOverlay() {
    isBubbleDragging.collect { isVisible ->
      when (isVisible) {
        true -> {
          val newOverlay = createTrashView()
          overlay = newOverlay
          windowManager.addView(newOverlay, params)
        }

        false -> {
          overlay?.let {
            try {
              windowManager.removeView(it)
              // do i need disposecomposition???
            } catch (e: IllegalArgumentException) {
              Log.e("OverlayViewController", "IllegalArgumentException $e")
            } finally {
              overlay = null
            }
          }
        }
      }
    }
  }


  private fun createTrashView(): ComposeView {
    val view = createComposeView(service)
    view.setContent {

      // compositionlocal trashcontroller or service

      TrashOverlay()
    }
    return view
  }

}