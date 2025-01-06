package com.oceloti.lemc.designlemc.presentation.viewmodels

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.oceloti.lemc.designlemc.presentation.actions.DrawingAction
import com.oceloti.lemc.designlemc.presentation.states.DrawingState
import com.oceloti.lemc.designlemc.presentation.states.PathData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DrawingViewModel : ViewModel() {
  private val _state = MutableStateFlow(DrawingState())
  val state = _state.asStateFlow()

  fun onAction(action: DrawingAction) {
    when (action) {
      DrawingAction.OnClearCanvasClick -> clearCanvas()
      is DrawingAction.OnDraw -> onDraw(action.offset)
      DrawingAction.OnNewPathStart -> onNewPathStart()
      DrawingAction.OnPathEnd -> onPathEnd()
      is DrawingAction.OnSelectColor -> onSelectColor(action.color)
    }

  }

  private fun onSelectColor(color: Color) {
    _state.update {
      it.copy(
        selectedColor = color
      )
    }
  }

  private fun onPathEnd() {
    val currentPathData = state.value.currentPath ?: return
    _state.update {
      it.copy(
        currentPath = null,
        paths = it.paths + currentPathData
      )
    }
  }

  private fun onNewPathStart() {
    _state.update {
      it.copy(
        currentPath = PathData(
          id = System.currentTimeMillis().toString(),
          color = it.selectedColor,
          path = emptyList()
        )
      )
    }
  }

  private fun onDraw(offset: Offset) {
    val currentPathData = state.value.currentPath ?: return
    _state.update {
      it.copy(
        currentPath = currentPathData.copy(
          path = currentPathData.path + offset
        )
      )
    }
  }

  private fun clearCanvas() {
    _state.update {
      it.copy(
        currentPath = null,
        paths = emptyList()
      )
    }
  }

}