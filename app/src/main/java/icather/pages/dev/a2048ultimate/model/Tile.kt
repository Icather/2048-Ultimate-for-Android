package icather.pages.dev.a2048ultimate.model

data class Cell(val x: Int, val y: Int)

data class Tile(
    var x: Int,
    var y: Int,
    var value: Int = 2,
    var previousPosition: Cell? = null,
    var mergedFrom: List<Tile>? = null,
    val id: Int = nextId()
) {
    companion object {
        private var idCounter = 0
        fun nextId() = idCounter++
    }

    fun savePosition() {
        previousPosition = Cell(x, y)
    }

    fun updatePosition(cell: Cell) {
        this.x = cell.x
        this.y = cell.y
    }
    
    fun copyTile(): Tile {
        return Tile(x, y, value, previousPosition, mergedFrom, id)
    }
}
