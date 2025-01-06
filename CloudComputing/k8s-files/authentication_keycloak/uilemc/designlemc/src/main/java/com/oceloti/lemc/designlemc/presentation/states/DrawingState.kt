package com.oceloti.lemc.designlemc.presentation.states

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class DrawingState(
  val selectedColor: Color = Color.Black,
  val currentPath: PathData? = null,
  val paths: List<PathData> = emptyList()
)

val allColors = listOf(
  Color.Black,
  Color.Red,
  Color.Blue,
  Color.Green,
  Color.Yellow,
  Color.Magenta,
  Color.Cyan
)

data class PathData(
  val id: String,
  val color: Color,
  val path: List<Offset>
)