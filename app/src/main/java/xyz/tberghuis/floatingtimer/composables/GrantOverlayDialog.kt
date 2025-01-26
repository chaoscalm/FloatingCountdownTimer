package xyz.tberghuis.floatingtimer.composables

import androidx.compose.runtime.Composable
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.tberghuis.floatingtimer.R
import xyz.tberghuis.floatingtimer.REQUEST_CODE_ACTION_MANAGE_OVERLAY_PERMISSION
import xyz.tberghuis.floatingtimer.logd
import xyz.tberghuis.floatingtimer.viewmodels.SharedVm

@Composable
fun GrantOverlayDialog(
  vm: SharedVm = viewModel(LocalActivity.current as ComponentActivity)
) {
  val context = LocalContext.current
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
