package icather.pages.dev.a2048ultimate.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import icather.pages.dev.a2048ultimate.model.Direction
import icather.pages.dev.a2048ultimate.model.GameManager
import icather.pages.dev.a2048ultimate.model.GameRepository
import icather.pages.dev.a2048ultimate.model.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = GameRepository(application)
    private val gameManager = GameManager()

    private val _gameState = MutableStateFlow(gameManager.getGameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _bestScore = MutableStateFlow(repository.getBestScore())
    val bestScore: StateFlow<Long> = _bestScore.asStateFlow()

    fun move(direction: Direction) {
        if (gameManager.move(direction)) {
            updateState()
        }
    }

    fun restart() {
        gameManager.restart()
        updateState()
    }

    fun keepPlaying() {
        gameManager.keepPlaying()
        updateState()
    }
    
    fun undo() {
        gameManager.undo()
        updateState()
    }
    
    fun changeMode(mode: icather.pages.dev.a2048ultimate.model.GameMode) {
        gameManager.setup(mode)
        updateState()
    }

    private fun updateState() {
        val newState = gameManager.getGameState()
        _gameState.value = newState
        
        if (newState.score > _bestScore.value) {
            repository.saveBestScore(newState.score)
            _bestScore.value = newState.score
        }
    }
}
