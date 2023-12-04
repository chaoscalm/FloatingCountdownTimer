package xyz.tberghuis.floatingtimer.tmp3

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import xyz.tberghuis.floatingtimer.LocalNavController
import xyz.tberghuis.floatingtimer.R
import xyz.tberghuis.floatingtimer.REQUEST_CODE_ACTION_MANAGE_OVERLAY_PERMISSION
import xyz.tberghuis.floatingtimer.composables.CreateStopwatchCard
import xyz.tberghuis.floatingtimer.logd
import xyz.tberghuis.floatingtimer.screens.LaunchPostNotificationsPermissionRequest

@Composable
fun TmpNavHost() {
  val navController = rememberNavController()
  CompositionLocalProvider(LocalNavController provides navController) {
    NavHost(
      navController = navController, startDestination = "home"
    ) {
      composable("home") {
        HomeScreen()
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
  LaunchPostNotificationsPermissionRequest()

  val vm: HomeViewModel = viewModel()
  val context = LocalContext.current
  val navController = LocalNavController.current

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        modifier = Modifier,
        actions = {
          IconButton(onClick = {
            navController.navigate("settings")
          }) {
            Icon(Icons.Filled.Settings, stringResource(R.string.settings))
          }
        },
      )
    },
    snackbarHost = { SnackbarHost(vm.snackbarHostState) },
  ) {
    HomeScreenContent(it)
  }

  if (vm.showGrantOverlayDialog) {
    AlertDialog(onDismissRequest = {
      vm.showGrantOverlayDialog = false
    }, confirmButton = {
      Button(onClick = {
        logd("go to settings")
        val intent = Intent(
          Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.packageName)
        )

        startActivityForResult(
          context as Activity, intent, REQUEST_CODE_ACTION_MANAGE_OVERLAY_PERMISSION, null
        )
        vm.showGrantOverlayDialog = false

      }) {
        Text(stringResource(R.string.go_to_settings))
      }
    }, title = {
      Text(stringResource(R.string.enable_overlay_permission))
    }, text = {
      Text(buildAnnotatedString {
        append(stringResource(R.string.dialog_enable_overlay_permission))
        append(" ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
          append(stringResource(R.string.app_name))
        }
      })
    })
  }
}

@Composable
fun HomeScreenContent(paddingValues: PaddingValues) {
  val focusManager = LocalFocusManager.current

  Column(
    modifier = Modifier
      .padding(paddingValues)
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .pointerInput(Unit) {
        detectTapGestures(onTap = {
          focusManager.clearFocus()
          logd("on tap")
        })
      },
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    CreateCountdownCard()
    CreateStopwatchCard()
  }
}

