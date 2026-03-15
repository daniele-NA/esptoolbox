package com.crescenzi.esptoolbox.presentation.navigation.physical.log

import androidx.lifecycle.ViewModel
import com.crescenzi.esptoolbox.data.core.BaseRepo

/**
 * Manages board logs, STARTED FROM USB_VIEW_MODEL
 * Handles reset and Flash
 */
class LogViewModel(
    val baseRepo: BaseRepo
) : ViewModel()
