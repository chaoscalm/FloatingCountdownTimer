package xyz.tberghuis.floatingtimer.tmp3

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import xyz.tberghuis.floatingtimer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TmpSettingsScreen(nav: NavHostController) {
  Scaffold(
    modifier = Modifier,
    topBar = {
      TopAppBar(
        title = { Text("Settings") },
        navigationIcon = {
          IconButton(onClick = {
          }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back))
          }
        },
        modifier = Modifier,
      )
    },
  ) { padding ->
    Column(
      modifier = Modifier.padding(padding),
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.Start,
    ) {
      Text("notification settings")
      Text("draw overlay setting")
      BatterySettings()
      Text("dontkillmyapp.com")
      Text("app version")
    }
  }
}

@Composable
fun BatterySettings() {
//  val context = LocalContext.current
  Text(
    "battery settings",
    modifier = Modifier.clickable {
//                                  i heard stories of being banned for having this
      // ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS

//      val intent = Intent()
//      val pm = context.getSystemService<PowerManager>()
//      if (pm?.isIgnoringBatteryOptimizations(context.packageName) == true) {
//        intent.action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
//      } else {
//        intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
//        intent.data = Uri.parse("package:${context.packageName}")
//      }
//      context.startActivity(intent)
    },
  )
}
