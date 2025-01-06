package com.oceloti.lemc.designlemc.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oceloti.lemc.designlemc.presentation.models.FlowDemo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class FlowViewModel : ViewModel() {
  val demos = listOf(
    FlowDemo.ColdFlowSimple,
    FlowDemo.HotFlowStateFlow,
    FlowDemo.OperatorMap,
    FlowDemo.OperatorFilter,
    FlowDemo.OperatorFlatMapConcat
    // Add more as needed
  )

  // A hot flow to hold the result message that we'll show in the UI
  private val _flowResult = MutableStateFlow("No demo has been run yet.")
  val flowResult: StateFlow<String> = _flowResult.asStateFlow()

  // Called from the Compose UI when user clicks "Run"
  fun runDemo(demo: FlowDemo) {
    when (demo) {
      FlowDemo.ColdFlowSimple -> runColdFlowSimple()
      FlowDemo.HotFlowStateFlow -> runHotFlowStateFlow()
      FlowDemo.OperatorMap -> runOperatorMap()
      FlowDemo.OperatorFilter -> runOperatorFilter()
      FlowDemo.OperatorFlatMapConcat -> runOperatorFlatMapConcat()
    }
  }

  // ---------------------------------------------------------------------------
  // 1) Cold Flow Demo
  // ---------------------------------------------------------------------------
  private fun runColdFlowSimple() {
    viewModelScope.launch {
      // A cold flow: only emits when (and each time) we collect it
      val coldFlow = flow {
        emit("Emission #1 (Cold)")
        delay(500)
        emit("Emission #2 (Cold)")
        delay(500)
        emit("Emission #3 (Cold)")
      }

      // Collect the flow
      val results = mutableListOf<String>()
      coldFlow.collect { value ->
        results += value
      }

      // Let’s show the results in the UI
      _flowResult.value = """
                Cold Flow Demo Results:
                ${results.joinToString(separator = "\n")}
            """.trimIndent()
    }
  }

  // ---------------------------------------------------------------------------
  // 2) Hot Flow (StateFlow) Demo
  // ---------------------------------------------------------------------------
  private fun runHotFlowStateFlow() {
    // A hot flow: StateFlow always has the latest value (starts at 0)
    val stateFlow = MutableStateFlow(0)

    // Let's increment it in one coroutine...
    viewModelScope.launch {
      for (i in 1..5) {
        delay(500)
        stateFlow.value = i
      }
    }

    // ... and collect it in another coroutine
    viewModelScope.launch {
      val results = mutableListOf<Int>()
      // We'll collect 6 values (initial + 5 updates)
      stateFlow.take(6).collect { value ->
        results += value
      }

      _flowResult.value = """
                Hot StateFlow Demo Results:
                ${results.joinToString()} 
                (Note how it always holds the latest value)
            """.trimIndent()
    }
  }

  // ---------------------------------------------------------------------------
  // 3) Operator: map()
  // ---------------------------------------------------------------------------
  private fun runOperatorMap() {
    viewModelScope.launch {
      // We'll transform integers by multiplying them by 10
      val mappedResults = flowOf(1, 2, 3, 4, 5)
        .map { it * 10 }
        .toList() // toList() collects into a list

      _flowResult.value = """
                map() Operator Demo:
                Original: 1,2,3,4,5
                Mapped: $mappedResults
            """.trimIndent()
    }
  }

  // ---------------------------------------------------------------------------
  // 4) Operator: filter()
  // ---------------------------------------------------------------------------
  private fun runOperatorFilter() {
    viewModelScope.launch {
      // Filter out odd numbers
      val filteredResults = flowOf(1, 2, 3, 4, 5)
        .filter { it % 2 == 0 }
        .toList()

      _flowResult.value = """
                filter() Operator Demo:
                Original: 1,2,3,4,5
                Filtered (evens only): $filteredResults
            """.trimIndent()
    }
  }

  // ---------------------------------------------------------------------------
  // 5) Operator: flatMapConcat()
  // ---------------------------------------------------------------------------
  @OptIn(ExperimentalCoroutinesApi::class)
  private fun runOperatorFlatMapConcat() {
    viewModelScope.launch {
      // We'll create a simple flow of strings, then flatMap them to a flow of characters
      val results = flowOf("Hello", "Flow")
        .flatMapConcat { word ->
          flow {
            // Emit each character
            for (char in word) {
              emit(char.toString())
              delay(100)
            }
          }
        }
        .toList()

      _flowResult.value = """
                flatMapConcat() Operator Demo:
                Original words: ["Hello", "Flow"]
                Emitted chars (concatenated): $results
            """.trimIndent()
    }
  }


}