package com.crescenzi.esptoolbox.presentation.navigation.physical.log

import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.core.values.Constants.CARD_CORNER
import com.crescenzi.esp32.usb.model.LogLevel
import com.crescenzi.esptoolbox.presentation.navigation.physical.log.util.LogScreenTopBarWidget

/**
 * Log page,
 * Designed to start immediately after permissions are granted,
 * standalone since it allows external operations like flash/reset
 */


@Composable
fun LogScreen(
    logViewModel: LogViewModel

) {

    val logs by logViewModel.logRepo.logs.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()


    /**
     * Auto-scroll boilerplate
     */
    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty()) {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val shouldScroll = lastVisibleItem >= logs.lastIndex - 3 // scroll only if already near the bottom
            if (shouldScroll) {
                listState.animateScrollToItem(logs.lastIndex)
            }
        }
    }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(16.dp)
            .padding(bottom = 100.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(CARD_CORNER))
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                itemsIndexed(logs) { _, log ->
                    SelectionContainer {
                        Text(
                            text = log.line,
                            style = MaterialTheme.typography.bodySmall,
                            color = getLogColor(log.logLevel),
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
            LogScreenTopBarWidget(logViewModel)

        }

    }


}

@Composable
fun getLogColor(logLevel: LogLevel): Color =
    when (logLevel) {
        LogLevel.INFO -> if (isSystemInDarkTheme()) Color(0xFF00FF00) else Color(
            0xFF006400
        )

        LogLevel.ERROR -> colorResource(R.color.md_theme_error)
        LogLevel.WARNING -> if (isSystemInDarkTheme()) Color(0xFFFFD54F) else Color(0xFFFFA000)
    }
