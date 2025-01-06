package com.oceloti.lemc.designlemc.presentation.models

sealed class FlowDemo(
  val title: String,
  val description: String
) {
  object ColdFlowSimple : FlowDemo(
    title = "Cold Flow: Simple",
    description = "Only emits when collected; new collector restarts emission."
  )

  object HotFlowStateFlow : FlowDemo(
    title = "Hot Flow: StateFlow",
    description = "Always has the latest value; continues emitting whether or not there are collectors."
  )

  object OperatorMap : FlowDemo(
    title = "Operator: map()",
    description = "Transforms each emission."
  )

  object OperatorFilter : FlowDemo(
    title = "Operator: filter()",
    description = "Filters emissions based on a predicate."
  )

  object OperatorFlatMapConcat : FlowDemo(
    title = "Operator: flatMapConcat()",
    description = "Transforms an emission into another flow, concatenating the results."
  )

  // ... Add more demos if you like
}