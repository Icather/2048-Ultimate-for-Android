package icather.pages.dev.a2048ultimate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import icather.pages.dev.a2048ultimate.model.Grid
import icather.pages.dev.a2048ultimate.ui.theme.EmptyCellColor
import icather.pages.dev.a2048ultimate.ui.theme.GridBackground

@Composable
fun GameBoard(
    grid: Grid,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(GridBackground, RoundedCornerShape(6.dp))
    ) {
        val boardSize = maxWidth
        val cellSpacing = boardSize * 0.03f // 15px / 500px
        val cellSize = (boardSize - (cellSpacing * 5)) / 4

        // Render background grid cells
        for (x in 0 until 4) {
            for (y in 0 until 4) {
                val left = (cellSize + cellSpacing) * x + cellSpacing
                val top = (cellSize + cellSpacing) * y + cellSpacing
                
                Box(
                    modifier = Modifier
                        .offset(x = left, y = top)
                        .size(cellSize)
                        .background(EmptyCellColor, RoundedCornerShape(4.dp))
                )
            }
        }

        // Render active tiles
        // We need to collect all tiles from the grid
        val tiles = remember(grid) {
            val tileList = mutableListOf<icather.pages.dev.a2048ultimate.model.Tile>()
            grid.eachCell { _, _, tile ->
                if (tile != null) {
                    tileList.add(tile)
                }
            }
            tileList
        }

        tiles.forEach { tile ->
            androidx.compose.runtime.key(tile.id) {
                TileView(
                    tile = tile,
                    cellSize = cellSize,
                    cellSpacing = cellSpacing
                )
            }
        }
    }
}
