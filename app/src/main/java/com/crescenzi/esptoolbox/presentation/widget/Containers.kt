package com.crescenzi.esptoolbox.presentation.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.crescenzi.esptoolbox.core.values.Constants


/**
 * Wrapper for cards:
 * shape
 * color
 * outer padding
 * inner padding between elements and border
 */
@Composable
fun CardContainer(
    modifier: Modifier = Modifier,
    cardColors: CardColors = CardDefaults.cardColors(),
    applyOuterPadding: Boolean=true,
    applyInnerPadding: Boolean=true,
    content: @Composable () -> Unit
) {

    val innerPadding= if(applyInnerPadding) Constants.CARD_PADDING else 0.dp
    val outerPadding= if(applyOuterPadding) Constants.CARD_PADDING else 0.dp



    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 18.dp),
        modifier = modifier.padding(
            top = outerPadding,
            bottom = outerPadding
        ),
        shape = RoundedCornerShape(Constants.CARD_CORNER),
        colors = cardColors
    ) {

        Box(modifier = Modifier.padding(innerPadding))
        { content() }
    }
}


@Composable
fun OutlinedCardContainer(
    modifier: Modifier = Modifier,
    applyOuterPadding: Boolean=true,
    applyInnerPadding: Boolean=true,
    content: @Composable () -> Unit,
    ) {
    val innerPadding= if(applyInnerPadding) Constants.CARD_PADDING else 0.dp
    val outerPadding= if(applyOuterPadding) Constants.CARD_PADDING else 0.dp


    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 18.dp),
        modifier = modifier.padding(
            top = outerPadding,
            bottom = outerPadding
        ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        shape = RoundedCornerShape(Constants.CARD_CORNER),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
    ) {
        Box(modifier = Modifier.padding(innerPadding))
        { content() }
    }
}

