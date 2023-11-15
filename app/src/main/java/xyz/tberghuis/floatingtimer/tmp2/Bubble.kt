package xyz.tberghuis.floatingtimer.tmp2

import android.util.Log

abstract class Bubble(private val service: FloatingService) {
  // todo move ViewHolder logic to here
  val viewHolder = TimerViewHolder(service)

  open fun exit() {

//    service.overlayController.bubbleSet.iterator(). remove(this)


    // todo move logic to overlaycontroller
    //  bubbleSet empty stop service
    try {
      service.overlayController.windowManager.removeView(viewHolder.view)
    } catch (e: IllegalArgumentException) {
      Log.e("Stopwatch", "IllegalArgumentException $e")
    }
  }

  abstract fun reset()

  abstract fun onTap()

// todo or MVI
//  fun exitBubble() {
//    try {
//      service.overlayController.windowManager.removeView(viewHolder.view)
//    } catch (e: IllegalArgumentException) {
//      Log.e("Stopwatch", "IllegalArgumentException $e")
//    }
//  }

}