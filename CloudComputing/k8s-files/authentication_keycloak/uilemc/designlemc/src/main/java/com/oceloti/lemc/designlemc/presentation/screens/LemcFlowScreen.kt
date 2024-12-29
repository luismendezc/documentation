package com.oceloti.lemc.designlemc.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oceloti.lemc.designlemc.presentation.models.FlowDemo
import com.oceloti.lemc.designlemc.presentation.viewmodels.FlowViewModel

@Composable
internal fun LemcFlowScreen(
  viewModel: FlowViewModel = viewModel()
){
  // We'll collect the list of demos and the current result text
  val demos = viewModel.demos
  val currentResult by viewModel.flowResult.collectAsState()

  Column(modifier = Modifier.padding(16.dp)) {
    Text(
      text = "Flow Playground",
      style = MaterialTheme.typography.titleSmall
    )

    Spacer(modifier = Modifier.height(8.dp))

    // List each demo
    demos.forEach { demo ->
      FlowDemoItem(
        demo = demo,
        onRunDemo = { viewModel.runDemo(demo) }
      )
      Spacer(modifier = Modifier.height(8.dp))
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Result:",
      style = MaterialTheme.typography.titleSmall
    )
    Text(
      text = currentResult,
      style = MaterialTheme.typography.bodyMedium
    )
  }
}

@Composable
private fun FlowDemoItem(
  demo: FlowDemo,
  onRunDemo: () -> Unit
) {
  Card(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.padding(8.dp)) {
      Text(text = demo.title, style = MaterialTheme.typography.titleSmall)
      Spacer(modifier = Modifier.height(4.dp))
      Text(text = demo.description, style = MaterialTheme.typography.bodyMedium)
      Spacer(modifier = Modifier.height(4.dp))
      Button(onClick = onRunDemo) {
        Text("Run")
      }
    }
  }
}