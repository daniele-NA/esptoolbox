package com.crescenzi.esptoolbox.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.data.core.BaseRepo
import com.crescenzi.esptoolbox.presentation.navigation.home.HomeScreen
import com.crescenzi.esptoolbox.presentation.navigation.physical.log.LogScreen
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.navigation.UsbNavigation
import com.crescenzi.esptoolbox.presentation.navigation.physical.wifi.WifiScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject


@Serializable
object HomePage

@Serializable
object UsbPage

@Serializable
object LogPage

@Serializable
object WifiPage


val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("LocalNavController is not provided.")
}


private enum class Workspace {
    USB, LOG, WIFI;

    fun destination(): Any = when (this) {
        USB -> UsbPage
        LOG -> LogPage
        WIFI -> WifiPage
    }

    @Composable
    fun label(): String = when (this) {
        USB -> stringResource(R.string.usb_label)
        LOG -> stringResource(R.string.log_label)
        WIFI -> stringResource(R.string.wifi_label)
    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainNavigation(
    onReqUsbPermission: () -> Unit
) {

    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        val current = navController.currentDestination
        if (current == null || current.route == null) {
            navController.navigate(HomePage) {
                popUpTo(0)
            }
        }
    }

    val baseRepo = koinInject<BaseRepo>()

    val showBadge = rememberSaveable { mutableStateOf(false) }
    val logs by baseRepo.logs.collectAsStateWithLifecycle()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val destination = backStackEntry?.destination
    val currentWorkspace = when {
        destination?.hasRoute(UsbPage::class) == true -> Workspace.USB
        destination?.hasRoute(LogPage::class) == true -> Workspace.LOG
        destination?.hasRoute(WifiPage::class) == true -> Workspace.WIFI
        else -> null
    }

    LaunchedEffect(logs.size) {
        if (currentWorkspace != Workspace.LOG) {
            showBadge.value = true
        }
    }

    CompositionLocalProvider(LocalNavController provides navController) {
        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                NavHost(
                    navController = navController,
                    startDestination = HomePage,
                    modifier = Modifier.weight(1f)
                ) {
                    composable<HomePage> {
                        HomeScreen(koinViewModel(viewModelStoreOwner = it))
                    }
                    composable<UsbPage> {
                        UsbNavigation(
                            usbConnectionViewModel = koinViewModel(viewModelStoreOwner = it),
                            usbUpdaterViewModel = koinViewModel(viewModelStoreOwner = it),
                            onReqUsbPermission = onReqUsbPermission
                        )
                    }
                    composable<LogPage> {
                        LogScreen(koinViewModel(viewModelStoreOwner = it))
                    }
                    composable<WifiPage> {
                        WifiScreen(koinViewModel(viewModelStoreOwner = it))
                    }
                }
            }

            if (currentWorkspace != null) {
                HorizontalFloatingToolbar(
                    expanded = true,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp)
                ) {
                    Workspace.entries.forEach { workspace ->
                        ToggleButton(
                            checked = currentWorkspace == workspace,
                            onCheckedChange = {
                                if (currentWorkspace != workspace) {
                                    navController.navigate(workspace.destination()) {
                                        popUpTo(HomePage) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                                if (workspace == Workspace.LOG) {
                                    showBadge.value = false
                                }
                            }
                        ) {
                            Text(
                                text = workspace.label(),
                                maxLines = 1,
                                style = MaterialTheme.typography.labelMedium
                            )

                            if (workspace == Workspace.LOG && showBadge.value) {
                                Spacer(Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.error,
                                            shape = CircleShape
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
