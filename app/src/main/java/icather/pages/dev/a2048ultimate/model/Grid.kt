package icather.pages.dev.a2048ultimate.model

import kotlin.random.Random

class Grid(val size: Int, previousState: Array<Array<Tile?>>? = null) {
    val cells: Array<Array<Tile?>> = previousState ?: empty()

    private fun empty(): Array<Array<Tile?>> {
        return Array(size) { arrayOfNulls<Tile>(size) }
    }

    fun randomAvailableCell(): Cell? {
        val cells = availableCells()
        if (cells.isNotEmpty()) {
            return cells[Random.nextInt(cells.size)]
        }
        return null
    }

    fun availableCells(): List<Cell> {
        val cells = mutableListOf<Cell>()
        for (x in 0 until size) {
            for (y in 0 until size) {
                if (this.cells[x][y] == null) {
                    cells.add(Cell(x, y))
                }
            }
        }
        return cells
    }

    fun eachCell(callback: (Int, Int, Tile?) -> Unit) {
        for (x in 0 until size) {
            for (y in 0 until size) {
                callback(x, y, cells[x][y])
            }
        }
    }

    fun cellsAvailable(): Boolean {
        return availableCells().isNotEmpty()
    }

    fun cellAvailable(cell: Cell): Boolean {
        return !cellOccupied(cell)
    }

    fun cellOccupied(cell: Cell): Boolean {
        return cellContent(cell) != null
    }

    fun cellContent(cell: Cell): Tile? {
        return if (withinBounds(cell)) {
            cells[cell.x][cell.y]
        } else {
            null
        }
    }

    fun insertTile(tile: Tile) {
        cells[tile.x][tile.y] = tile
    }

    fun removeTile(tile: Tile) {
        cells[tile.x][tile.y] = null
    }

    fun withinBounds(cell: Cell): Boolean {
        return cell.x in 0 until size && cell.y in 0 until size
    }
    
    // Helper for cloning the grid state
    fun clone(): Grid {
        val newCells = Array(size) { x ->
            Array(size) { y ->
                cells[x][y]?.copyTile()
            }
        }
        return Grid(size, newCells)
    }
}
