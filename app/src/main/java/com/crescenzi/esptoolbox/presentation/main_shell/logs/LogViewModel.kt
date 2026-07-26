package com.crescenzi.esptoolbox.presentation.main_shell.logs

import androidx.lifecycle.ViewModel
import com.crescenzi.esp32.LogRepo

/**
 * Manages board logs, STARTED FROM USB_VIEW_MODEL
 * Handles reset and Flash
 */
class LogViewModel(
    val logRepo: LogRepo
) : ViewModel()
