package icather.pages.dev.a2048ultimate.model

import kotlin.math.floor
import kotlin.random.Random

enum class Direction(val x: Int, val y: Int) {
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);

    companion object {
        fun fromInt(value: Int): Direction = entries.first { it.ordinal == value }
    }
}

enum class GameMode {
    STANDARD,
    CLASSIC,
    PLUS
}

data class GameState(
    val grid: Grid,
    val score: Long,
    val over: Boolean,
    val won: Boolean,
    val keepPlaying: Boolean,
    val mode: GameMode,
    val canUndo: Boolean
)

class GameManager(val size: Int = 4) {
    var grid: Grid = Grid(size)
    var score: Long = 0
    var over: Boolean = false
    var won: Boolean = false
    var keepPlaying: Boolean = false
    var mode: GameMode = GameMode.STANDARD
    
    private val history = java.util.Stack<GameState>()
    
    private val startTiles = 2

    init {
        setup()
    }

    fun setup(newMode: GameMode? = null) {
        if (newMode != null) {
            mode = newMode
        }
        grid = Grid(size)
        score = 0
        over = false
        won = false
        keepPlaying = false
        history.clear()
        addStartTiles()
    }
    
    fun restart() {
        setup()
    }

    fun keepPlaying() {
        keepPlaying = true
    }

    fun isGameTerminated(): Boolean {
        return over || (won && !keepPlaying)
    }

    fun undo() {
        if (history.isNotEmpty()) {
            val previousState = history.pop()
            grid = previousState.grid
            score = previousState.score
            over = previousState.over
            won = previousState.won
            keepPlaying = previousState.keepPlaying
            // Mode should not change on undo
        }
    }
    
    private fun saveState() {
        // Limit history size if needed, e.g. 10 steps
        // For now, unlimited or reasonable limit
        if (history.size > 20) {
            history.removeAt(0)
        }
        history.push(getGameState())
    }

    private fun addStartTiles() {
        repeat(startTiles) {
            addRandomTile()
        }
    }

    private fun addRandomTile() {
        if (grid.cellsAvailable()) {
            val value = if (Random.nextDouble() < 0.9) 2 else 4
            val cell = grid.randomAvailableCell()
            cell?.let {
                val tile = Tile(it.x, it.y, value)
                grid.insertTile(tile)
            }
        }
    }

    private fun prepareTiles() {
        grid.eachCell { x, y, tile ->
            tile?.let {
                it.mergedFrom = null
                it.savePosition()
            }
        }
    }

    private fun moveTile(tile: Tile, cell: Cell) {
        grid.cells[tile.x][tile.y] = null
        grid.cells[cell.x][cell.y] = tile
        tile.updatePosition(cell)
    }

    fun move(direction: Direction): Boolean {
        if (isGameTerminated()) return false

        val vector = direction
        val traversals = buildTraversals(vector)
        var moved = false

        // Save state before moving
        // We need to save a COPY of the current state
        val currentState = getGameState()
        
        prepareTiles()

        traversals.x.forEach { x ->
            traversals.y.forEach { y ->
                val cell = Cell(x, y)
                val tile = grid.cellContent(cell)

                if (tile != null) {
                    val positions = findFarthestPosition(cell, vector)
                    val next = grid.cellContent(positions.next)

                    if (next != null && next.value == tile.value && next.mergedFrom == null) {
                        val merged = Tile(positions.next.x, positions.next.y, tile.value * 2)
                        merged.mergedFrom = listOf(tile, next)

                        grid.insertTile(merged)
                        grid.removeTile(tile)

                        // Converge the two tiles' positions
                        tile.updatePosition(positions.next)

                        // Update the score
                        score += merged.value

                        // The mighty 2048 tile
                        if (merged.value == 2048) won = true
                    } else {
                        moveTile(tile, positions.farthest)
                    }

                    if (!positionsEqual(cell, tile)) {
                        moved = true
                    }
                }
            }
        }

        if (moved) {
            // If moved, push the PREVIOUS state to history
            // But wait, we modified the grid in place.
            // So we should have pushed `currentState` to history BEFORE modifying.
            // Let's do that.
            if (mode != GameMode.CLASSIC) { // Classic mode has no undo
                history.push(currentState)
            }
            
            addRandomTile()

            if (!movesAvailable()) {
                over = true
            }
        }
        
        return moved
    }

    private fun buildTraversals(vector: Direction): Traversals {
        val x = MutableList(size) { it }
        val y = MutableList(size) { it }

        if (vector.x == 1) x.reverse()
        if (vector.y == 1) y.reverse()

        return Traversals(x, y)
    }

    private fun findFarthestPosition(cell: Cell, vector: Direction): FarthestPosition {
        var previous: Cell
        var current = cell

        do {
            previous = current
            current = Cell(previous.x + vector.x, previous.y + vector.y)
        } while (grid.withinBounds(current) && grid.cellAvailable(current))

        return FarthestPosition(previous, current)
    }

    private fun movesAvailable(): Boolean {
        return grid.cellsAvailable() || tileMatchesAvailable()
    }

    private fun tileMatchesAvailable(): Boolean {
        for (x in 0 until size) {
            for (y in 0 until size) {
                val tile = grid.cellContent(Cell(x, y))
                if (tile != null) {
                    for (direction in Direction.entries) {
                        val vector = direction
                        val cell = Cell(x + vector.x, y + vector.y)
                        val other = grid.cellContent(cell)

                        if (other != null && other.value == tile.value) {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    private fun positionsEqual(first: Cell, second: Tile): Boolean {
        return first.x == second.x && first.y == second.y
    }
    
    // Helper to get current state
    fun getGameState(): GameState {
        return GameState(grid.clone(), score, over, won, keepPlaying, mode, history.isNotEmpty())
    }
}

data class Traversals(val x: List<Int>, val y: List<Int>)
data class FarthestPosition(val farthest: Cell, val next: Cell)
