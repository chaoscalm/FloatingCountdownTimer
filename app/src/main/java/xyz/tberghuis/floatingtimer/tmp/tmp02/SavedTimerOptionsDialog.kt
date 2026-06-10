package xyz.tberghuis.floatingtimer.tmp.tmp02

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import xyz.tberghuis.floatingtimer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedTimerOptionsDialog(
  vmc: SavedTimerDialogVmc
) {
  if (vmc.showOptionsDialog == null) {
    return
  }

  val clipboardManager = LocalClipboardManager.current
  val context = LocalContext.current

  BasicAlertDialog(
    onDismissRequest = {
      vmc.showOptionsDialog = null
    },
    modifier = Modifier,
  ) {
    Surface(
      modifier = Modifier
        .wrapContentWidth()
        .wrapContentHeight(),
      shape = MaterialTheme.shapes.large,
      color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
      Column(
        modifier = Modifier
          .padding(16.dp)
          .width(IntrinsicSize.Max),
        verticalArrangement = Arrangement.spacedBy(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        // todo add to strings.xml
        Text(stringResource(R.string.saved_timer_options))


        Row(
//          modifier = Modifier.wid(),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Checkbox(
            checked = vmc.start,
            onCheckedChange = { vmc.start = it }
          )
          Text(stringResource(R.string.auto_start))
        }




        Row(
          modifier = Modifier,
          horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
          IconButton(
            onClick = {
              vmc.deepLinkToClipboard(clipboardManager)
            },
          ) {
            Icon(Icons.Default.Link, stringResource(R.string.link))
          }


          IconButton(
            onClick = {
              add_to_homescreen(context, vmc.showOptionsDialog!!, vmc.start)
              vmc.showOptionsDialog = null
            },
          ) {
            Icon(Icons.Default.PushPin, stringResource(R.string.add_to_home_screen))
          }



          IconButton(
            onClick = {
              vmc.deleteSavedTimer()
            },
          ) {
            Icon(Icons.Default.Delete, stringResource(R.string.delete))
          }
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End,
        ) {
//          Spacer(modifier = Modifier.weight(1f))
          TextButton(onClick = {
            vmc.showOptionsDialog = null
          }) {
            Text(stringResource(R.string.cancel))
          }

        }


      }
    }
  }
}