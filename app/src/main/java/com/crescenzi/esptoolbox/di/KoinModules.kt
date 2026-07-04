package com.crescenzi.esptoolbox.di

import com.crescenzi.esp32.LogRepo
import com.crescenzi.esptoolbox.data.phone.data.DeviceRepo
import com.crescenzi.esp32.usb.UsbRepo
import com.crescenzi.esp32.firmware.EspRepo
import com.crescenzi.esp32.wifi.EspTouchRepo
import com.crescenzi.esptoolbox.presentation.navigation.home.HomeViewModel
import com.crescenzi.esptoolbox.presentation.navigation.physical.log.LogViewModel
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection.UsbConnectionViewModel
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.updater.UsbUpdaterViewModel
import com.crescenzi.esptoolbox.presentation.navigation.physical.wifi.WifiViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositories = module {
    single { LogRepo() }
    single { DeviceRepo() }
    single { EspRepo(get()) }
    single { UsbRepo(get(), get()) }
    single { EspTouchRepo() }
}

val viewModels = module {
    viewModel { HomeViewModel(get()) }
    viewModel { LogViewModel(get()) }
    viewModel { UsbConnectionViewModel(get(), get(), get(), get()) }
    viewModel { UsbUpdaterViewModel(get(), get(), get()) }
    viewModel { WifiViewModel(application = get(), deviceRepo = get(), get(), get()) }
}
