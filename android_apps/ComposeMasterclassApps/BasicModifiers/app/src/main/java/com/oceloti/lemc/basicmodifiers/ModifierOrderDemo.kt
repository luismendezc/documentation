package com.oceloti.lemc.basicmodifiers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oceloti.lemc.basicmodifiers.ui.theme.BasicModifiersTheme

@Composable
fun BasicModifierDemo2(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(16.dp)
            .background(Color.Red)
            .clip(CircleShape)
            .background(Color.Gray)
            .padding(16.dp)
            .background(Color.Green),
        contentAlignment = Alignment.Center
    ) {
        Text("T")
    }
}

@Composable
fun BasicModifierDemo1(modifier: Modifier = Modifier) {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Red)) {
                append("Bold Text")
            }
            append("\n\n")
            append("Regular Text")
        },
        modifier = modifier
    )

}

@Preview
@Composable
private fun ModifierOrderDemo() {
    BasicModifiersTheme() {
        BasicModifierDemo2(modifier = Modifier.background(Color.Cyan))
    }
}

@Preview
@Composable
private fun ModifierOrderDemo2() {
    BasicModifiersTheme() {
        BasicModifierDemo1(modifier = Modifier.background(Color.Cyan))
    }
}