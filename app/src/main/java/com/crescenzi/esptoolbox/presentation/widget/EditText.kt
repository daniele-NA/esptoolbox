package com.crescenzi.esptoolbox.presentation.widget

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// == comment == //
// == App Common TextField with animated dashed border and M3 Expressive premium visuals == //
@Composable
internal fun EditText(
    modifier: Modifier = Modifier,
    vt: VisualTransformation = VisualTransformation.None,
    opt: KeyboardOptions,
    actions: KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit,
    label: String,
    initialValue: String = "",
    singleLine: Boolean = true,
    enabled: Boolean = true,
    readOnly: Boolean = false
) {
    // == readOnly: non-editable but NOT dimmed (e.g. dropdown anchors). disabled: dimmed == //
    val fieldEnabled = enabled && !readOnly
    val dimmed = !enabled
    var text by rememberSaveable { mutableStateOf(initialValue) }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val colorScheme = MaterialTheme.colorScheme
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    val dashInterval = 10.dp.dpToPx()
    val borderWidth = 2.dp

    // == Scroll into view when focused == //
    LaunchedEffect(isFocused) {
        if (isFocused) {
            bringIntoViewRequester.bringIntoView()
        }
    }

    // == Animate dash phase when focused == //
    val infiniteTransition = rememberInfiniteTransition(label = "dash")
    val dashPhase = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = dashInterval * 2,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = LinearEasing)
        ),
        label = "dash_phase"
    )

    // == Emphasized Easing visual animations == //
    val animatedBorderColor by animateColorAsState(
        targetValue = when {
            dimmed -> colorScheme.outline.copy(alpha = 0.2f)
            isFocused -> colorScheme.primary
            else -> colorScheme.outline.copy(alpha = 0.4f)
        },
        animationSpec = tween(
            durationMillis = 400,
            easing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
        ),
        label = "border_color"
    )

    val containerColor by animateColorAsState(
        targetValue = when {
            dimmed -> colorScheme.surfaceContainerLow.copy(alpha = 0.5f)
            isFocused -> colorScheme.surfaceContainerHigh
            else -> colorScheme.surfaceContainerLow
        },
        animationSpec = tween(
            durationMillis = 400,
            easing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
        ),
        label = "container_color"
    )

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.015f else 1.0f,
        animationSpec = tween(
            durationMillis = 400,
            easing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
        ),
        label = "scale"
    )

    val textColor = if (dimmed) colorScheme.onSurface.copy(alpha = 0.4f) else colorScheme.onSurface
    val cornerRadiusPx = 24.dp.dpToPx()
    val borderWidthPx = borderWidth.dpToPx()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .bringIntoViewRequester(bringIntoViewRequester)
            .scale(scale)
            .drawBehind {
                val phase = if (isFocused) dashPhase.value else 0f
                val stroke = if (isFocused) {
                    Stroke(
                        width = borderWidthPx,
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(dashInterval, dashInterval),
                            phase
                        ),
                    )
                } else {
                    Stroke(
                        width = 1.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
                // == Draw filled container background == //
                drawRoundRect(
                    color = containerColor,
                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                )
                // == Draw animated border == //
                drawRoundRect(
                    color = animatedBorderColor,
                    style = stroke,
                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                )
            }
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onValueChange(it)
            },
            enabled = fieldEnabled,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = textColor
            ),
            cursorBrush = SolidColor(colorScheme.primary),
            keyboardOptions = opt,
            keyboardActions = actions,
            singleLine = singleLine,
            visualTransformation = vt,
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                if (text.isEmpty()) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
private fun Dp.dpToPx(): Float = with(LocalDensity.current) { this@dpToPx.toPx() }