package xyz.tberghuis.floatingtimer.tmp3

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun TmpSettingsNavHost() {
  val settingsNavController = rememberNavController()
  NavHost(
    navController = settingsNavController,
    startDestination = "settings",
  ) {
    composable("settings") {
      TmpSettingsScreen(settingsNavController)
    }
    composable("change_color/default") {
      // todo pass in settingsNavController as argument
      // or provide via compositionlocal
      Text("change default color")
    }
    composable("change_size/default") {
      // todo pass in settingsNavController as argument
      Text("change default size")
    }
  }
}