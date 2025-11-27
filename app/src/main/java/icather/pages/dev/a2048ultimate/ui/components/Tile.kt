package icather.pages.dev.a2048ultimate.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import icather.pages.dev.a2048ultimate.model.Tile
import icather.pages.dev.a2048ultimate.ui.theme.*

@Composable
fun TileView(
    tile: Tile,
    cellSize: Dp,
    cellSpacing: Dp,
    modifier: Modifier = Modifier
) {
    // Calculate position
    // x is column (left), y is row (top)
    val targetLeft = (cellSize + cellSpacing) * tile.x + cellSpacing
    val targetTop = (cellSize + cellSpacing) * tile.y + cellSpacing

    val left by animateDpAsState(targetValue = targetLeft, label = "tile_left")
    val top by animateDpAsState(targetValue = targetTop, label = "tile_top")
    
    val scale = remember { Animatable(0f) }
    
    LaunchedEffect(Unit) {
        if (tile.mergedFrom != null) {
            // Pop animation
            scale.snapTo(1f)
            scale.animateTo(1.2f, animationSpec = tween(100))
            scale.animateTo(1f, animationSpec = tween(100))
        } else {
            // Appear animation
            scale.animateTo(1f, animationSpec = tween(200))
        }
    }

    Box(
        modifier = modifier
            .offset(x = left, y = top)
            .size(cellSize)
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value
            )
            .background(
                color = getTileColor(tile.value),
                shape = RoundedCornerShape(4.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tile.value.toString(),
            color = getTileTextColor(tile.value),
            fontSize = if (tile.value > 512) 24.sp else 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

fun getTileColor(value: Int): Color {
    return when (value) {
        2 -> Tile2
        4 -> Tile4
        8 -> Tile8
        16 -> Tile16
        32 -> Tile32
        64 -> Tile64
        128 -> Tile128
        256 -> Tile256
        512 -> Tile512
        1024 -> Tile1024
        2048 -> Tile2048
        else -> TileSuper
    }
}

fun getTileTextColor(value: Int): Color {
    return if (value <= 4) TextDark else TextWhite
}
