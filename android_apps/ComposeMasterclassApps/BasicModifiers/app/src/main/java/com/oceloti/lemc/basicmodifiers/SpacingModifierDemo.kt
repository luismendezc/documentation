package com.oceloti.lemc.basicmodifiers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oceloti.lemc.basicmodifiers.ui.theme.BasicModifiersTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SpacingModifierDemo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .imeNestedScroll()
        ) {
            items(100) {
                Text(
                    text = "Item $it",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
        TextField(
            value = "",
            onValueChange = {}
        )
        /*Text("Hello world")
        //Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Hello world",
            modifier = Modifier.padding(top = 16.dp)
        )*/
    }
}

@Preview
@Composable
private fun SpacingModifierDemoPreview() {
    BasicModifiersTheme {
        SpacingModifierDemo()
    }
}