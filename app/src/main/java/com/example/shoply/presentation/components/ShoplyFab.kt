package com.example.shoply.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ShoplyFab(
    modifier: Modifier,
    onFabClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onFabClick,
        modifier = modifier
            .size(50.dp)
            .clip(CircleShape),
        containerColor = Color(0xffFF0080),
        contentColor = Color.White
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add"
        )
    }
}

@Preview
@Composable
fun FloatingActionButtonPreview() {
    ShoplyFab(modifier = Modifier, onFabClick = {})
}


