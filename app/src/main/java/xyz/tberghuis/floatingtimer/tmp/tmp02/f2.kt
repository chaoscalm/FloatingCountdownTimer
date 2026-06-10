package xyz.tberghuis.floatingtimer.tmp.tmp02

import android.app.Application
import android.content.Context
import android.content.pm.ShortcutManager
import androidx.core.content.getSystemService
import kotlinx.coroutines.flow.first
import xyz.tberghuis.floatingtimer.R
import xyz.tberghuis.floatingtimer.data.SavedCountdown
import xyz.tberghuis.floatingtimer.data.SavedStopwatch
import xyz.tberghuis.floatingtimer.data.SavedTimer
import xyz.tberghuis.floatingtimer.data.appDatabase
import xyz.tberghuis.floatingtimer.data.preferencesRepository
import xyz.tberghuis.floatingtimer.logd
import xyz.tberghuis.floatingtimer.service.FloatingService



