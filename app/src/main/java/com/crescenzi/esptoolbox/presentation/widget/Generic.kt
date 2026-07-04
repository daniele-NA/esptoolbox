package com.crescenzi.esptoolbox.presentation.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

/***
 * Large centered title
 */
@Composable
fun CenterHeroTitle(modifier: Modifier= Modifier,txt: String,textStyle: TextStyle){
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            letterSpacing = 1.sp,
            text = txt,
            textAlign = TextAlign.Center,
            style =textStyle,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}