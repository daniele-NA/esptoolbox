package com.crescenzi.esptoolbox.di

import com.crescenzi.esptoolbox.data.core.BaseRepo
import com.crescenzi.esptoolbox.data.phone.data.DeviceRepo
import com.crescenzi.esptoolbox.data.usb.data.UsbRepo
import com.crescenzi.esptoolbox.data.usb.firmware.data.repository.EspRepo
import com.crescenzi.esptoolbox.presentation.navigation.home.HomeViewModel
import com.crescenzi.esptoolbox.presentation.navigation.physical.log.LogViewModel
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection.UsbConnectionViewModel
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.updater.UsbUpdaterViewModel
import com.crescenzi.esptoolbox.presentation.navigation.physical.wifi.WifiViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositories = module {
    single { BaseRepo() }
    single { DeviceRepo() }
    single { EspRepo(get()) }
    single { UsbRepo(get(), get()) }
}

val viewModels = module {
    viewModel { HomeViewModel(get()) }
    viewModel { LogViewModel(get()) }
    viewModel { UsbConnectionViewModel(get(), get(), get(), get()) }
    viewModel { UsbUpdaterViewModel(get(), get(), get()) }
    viewModel { WifiViewModel(application = get(), deviceRepo = get(), get()) }
}
