package com.crescenzi.esptoolbox.presentation.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import com.crescenzi.esptoolbox.theme.LATERAL_PADDING
import com.crescenzi.esptoolbox.theme.SPACE_L
import com.crescenzi.esptoolbox.theme.SPACE_M
import com.crescenzi.esptoolbox.theme.SPACE_S

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    title: String? = null,
    trailing: (@Composable () -> Unit)? = null,
    scrollable: Boolean = true,
    titleOnPage: Boolean = true,
    reserveTopBarSpace: Boolean = false,
    contentWindowInsets: WindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout),
    contentPadding: PaddingValues = PaddingValues(horizontal = LATERAL_PADDING, vertical = SPACE_L),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(SPACE_L),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    bottomBar: (@Composable ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val headingOnPage = titleOnPage && title != null
    val barShown = trailing != null || (title != null && !headingOnPage) || reserveTopBarSpace
    val collapsing = headingOnPage && barShown && scrollable
    val density = LocalDensity.current
    val scrollState = rememberScrollState()
    var titleHeightPx by remember { mutableStateOf(0) }
    val topPadPx = with(density) { contentPadding.calculateTopPadding().toPx() }
    val headingParallaxPx = with(density) { SPACE_S.toPx() }
    val handoffLeadPx = with(density) { SPACE_M.toPx() }
    val collapseFraction: () -> Float = {
        val travel = titleHeightPx.toFloat().coerceAtLeast(1f)
        ((scrollState.value.toFloat() - (topPadPx - handoffLeadPx)) / travel).coerceIn(0f, 1f)
    }
    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.Transparent,
            contentWindowInsets = contentWindowInsets,
            topBar = {
                if (barShown) {
                    AppTopBar(
                        title = title.orEmpty(),
                        titleAlpha =
                            when {
                                collapsing -> {
                                    { barTitleAlpha(collapseFraction()) }
                                }
                                headingOnPage -> {
                                    { 0f }
                                }
                                else -> {
                                    { 1f }
                                }
                            },
                        trailing = trailing,
                    )
                }
            },
            bottomBar = {
                if (bottomBar != null) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .navigationBarsPadding()
                                .padding(horizontal = LATERAL_PADDING, vertical = SPACE_L),
                        verticalArrangement = Arrangement.spacedBy(SPACE_L),
                        content = bottomBar,
                    )
                }
            },
        ) { insets ->
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(insets)
                        .consumeWindowInsets(insets)
                        .then(
                            if (scrollable) {
                                Modifier
                                    .verticalScroll(scrollState)
                                    .imePadding()
                                    .padding(contentPadding)
                            } else {
                                Modifier
                            },
                        ),
                verticalArrangement = verticalArrangement,
                horizontalAlignment = horizontalAlignment,
            ) {
                if (headingOnPage) {
                    Text(
                        title,
                        style = MaterialTheme.typography.displaySmall,
                        modifier =
                            Modifier
                                .then(
                                    if (!scrollable) {
                                        Modifier.padding(
                                            start = LATERAL_PADDING,
                                            end = LATERAL_PADDING,
                                            top = SPACE_L,
                                        )
                                    } else {
                                        Modifier
                                    },
                                )
                                .onSizeChanged { titleHeightPx = it.height }
                                .then(
                                    if (collapsing) {
                                        Modifier.graphicsLayer {
                                            val c = collapseFraction()
                                            alpha = largeTitleAlpha(c)
                                            translationY = -c * headingParallaxPx
                                        }
                                    } else {
                                        Modifier
                                    },
                                ),
                    )
                }
                content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    titleAlpha: () -> Float = { 1f },
    trailing: (@Composable () -> Unit)? = null,
) {
    val slidePx = with(LocalDensity.current) { SPACE_S.toPx() }
    TopAppBar(
        title = {
            Text(
                title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier =
                    Modifier.graphicsLayer {
                        val a = titleAlpha()
                        alpha = a
                        translationY = (1f - a) * slidePx
                    },
            )
        },
        actions = { trailing?.invoke() },
    )
}

private const val BAR_TITLE_FADE_START = 0.45f

private fun smootherstep(x: Float): Float {
    val t = x.coerceIn(0f, 1f)
    return t * t * t * (t * (t * 6f - 15f) + 10f)
}

private fun largeTitleAlpha(collapse: Float): Float = 1f - smootherstep(collapse)

private fun barTitleAlpha(collapse: Float): Float =
    smootherstep((collapse - BAR_TITLE_FADE_START) / (1f - BAR_TITLE_FADE_START))
