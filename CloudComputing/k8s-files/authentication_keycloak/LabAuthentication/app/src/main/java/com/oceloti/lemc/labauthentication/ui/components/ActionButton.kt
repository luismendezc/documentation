package com.oceloti.lemc.labauthentication.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oceloti.lemc.labauthentication.ui.theme.LabAuthenticationBlack
import com.oceloti.lemc.labauthentication.ui.theme.LabAuthenticationGray
import com.oceloti.lemc.labauthentication.ui.theme.LabAuthenticationTheme

@Composable
fun ActionButton(
  text: String,
  isLoading: Boolean,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  onClick: () -> Unit
) {
  Button(
    onClick = onClick,
    enabled = enabled,
    colors = ButtonDefaults.buttonColors(
      containerColor = MaterialTheme.colorScheme.primary,
      contentColor = MaterialTheme.colorScheme.onPrimary,
      disabledContainerColor = LabAuthenticationGray,
      disabledContentColor = LabAuthenticationBlack,
    ),
    shape = RoundedCornerShape(50f),
    modifier = modifier
      .height(IntrinsicSize.Min)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
      contentAlignment = Alignment.Center
    ) {
      CircularProgressIndicator(
        modifier = Modifier
          .size(15.dp)
          .alpha(if(isLoading) 1f else 0f),
        strokeWidth = 1.5.dp,
        strokeCap = StrokeCap.Round,
        color = MaterialTheme.colorScheme.onPrimary
      )
      Text(
        text = text,
        modifier = Modifier
          .alpha(if(isLoading) 0f else 1f),
        fontWeight = FontWeight.Medium
      )
    }
  }
}

@Composable
fun OutlinedActionButton(
  text: String,
  isLoading: Boolean,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  onClick: () -> Unit
) {
  Button(
    onClick = onClick,
    enabled = enabled,
    colors = ButtonDefaults.outlinedButtonColors(
      contentColor = MaterialTheme.colorScheme.onBackground
    ),
    border = BorderStroke(
      width = 0.5.dp,
      color = MaterialTheme.colorScheme.onBackground
    ),
    shape = RoundedCornerShape(50f),
    modifier = modifier
      .height(IntrinsicSize.Min)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
      contentAlignment = Alignment.Center
    ) {
      CircularProgressIndicator(
        modifier = Modifier
          .size(15.dp)
          .alpha(if(isLoading) 1f else 0f),
        strokeWidth = 1.5.dp,
        strokeCap = StrokeCap.Round,
        color = MaterialTheme.colorScheme.onPrimary
      )
      Text(
        text = text,
        modifier = Modifier
          .alpha(if(isLoading) 0f else 1f),
        fontWeight = FontWeight.Medium
      )
    }
  }
}

@Preview
@Composable
private fun PreviewActionButton(){
  LabAuthenticationTheme{
    Column {
      ActionButton(
        text = "Testing",
        isLoading = false,
        enabled = true,
        onClick = {}
      )
      Spacer(modifier = Modifier.height(16.dp))
      ActionButton(
        text = "Testing",
        isLoading = true,
        enabled = true,
        onClick = {}
      )
      Spacer(modifier = Modifier.height(16.dp))
      ActionButton(
        text = "Testing",
        isLoading = false,
        enabled = false,
        onClick = {}
      )
    }
  }
}


@Preview
@Composable
private fun PreviewOutlinedActionButton(){
  LabAuthenticationTheme{
    Column {
      OutlinedActionButton(
        text = "Testing",
        isLoading = false,
        enabled = true,
        onClick = {}
      )
      Spacer(modifier = Modifier.height(16.dp))
      OutlinedActionButton(
        text = "Testing",
        isLoading = true,
        enabled = true,
        onClick = {}
      )
      Spacer(modifier = Modifier.height(16.dp))
      OutlinedActionButton(
        text = "Testing",
        isLoading = false,
        enabled = false,
        onClick = {}
      )
    }
  }
}