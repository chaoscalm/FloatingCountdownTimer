package xyz.tberghuis.floatingtimer.tmp.tmp02

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import androidx.core.net.toUri
import android.graphics.drawable.Icon
import androidx.core.content.getSystemService
import xyz.tberghuis.floatingtimer.R
import xyz.tberghuis.floatingtimer.data.SavedTimer
import xyz.tberghuis.floatingtimer.viewmodels.toDeepLink

fun add_to_homescreen(context: Context, savedTimer: SavedTimer) {
  
  
  
  val shortcut = ShortcutInfo.Builder(context, "id1")
    .setShortLabel("Website")
    .setLongLabel("Open the website")
    .setIcon(Icon.createWithResource(context, R.drawable.ic_launcher_foreground))
    .setIntent(
//      intent
      Intent(
        Intent.ACTION_VIEW,
//        savedTimer.toDeepLink()
        "mydeeplink://mydeeplink/mypath?id=1".toUri()
      )
    )
    .build()


  val shortcutManager = context.getSystemService<ShortcutManager>()

  if (shortcutManager!!.isRequestPinShortcutSupported) {
    shortcutManager.requestPinShortcut(
      shortcut,
      null
    )
  }


}
