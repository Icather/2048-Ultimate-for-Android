package icather.pages.dev.a2048ultimate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import icather.pages.dev.a2048ultimate.model.Direction
import icather.pages.dev.a2048ultimate.ui.components.GameBoard
import icather.pages.dev.a2048ultimate.ui.components.GameOverlay
import icather.pages.dev.a2048ultimate.ui.components.Header
import icather.pages.dev.a2048ultimate.ui.theme.GameBackground
import icather.pages.dev.a2048ultimate.ui.theme._2048UltimateTheme
import icather.pages.dev.a2048ultimate.viewmodel.GameViewModel
import kotlin.math.abs

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import icather.pages.dev.a2048ultimate.ui.components.GameMenu
import icather.pages.dev.a2048ultimate.ui.components.PowerupBar
import icather.pages.dev.a2048ultimate.ui.theme.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu

class MainActivity : ComponentActivity() {
    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val gameState by viewModel.gameState.collectAsState()
            val isPlusMode = gameState.mode == icather.pages.dev.a2048ultimate.model.GameMode.PLUS
            
            _2048UltimateTheme(darkTheme = isPlusMode) {
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            GameMenu(
                                currentMode = gameState.mode,
                                onModeSelected = { mode ->
                                    viewModel.changeMode(mode)
                                    scope.launch { drawerState.close() }
                                }
                            )
                        }
                    }
                ) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        GameScreen(
                            viewModel = viewModel,
                            onMenuClick = { scope.launch { drawerState.open() } },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gameState by viewModel.gameState.collectAsState()
    val bestScore by viewModel.bestScore.collectAsState()

    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    
    val backgroundColor = if (gameState.mode == icather.pages.dev.a2048ultimate.model.GameMode.PLUS) PlusBackground else GameBackground

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        val threshold = 50f
                        if (abs(offsetX) > abs(offsetY)) {
                            if (abs(offsetX) > threshold) {
                                if (offsetX > 0) {
                                    viewModel.move(Direction.RIGHT)
                                } else {
                                    viewModel.move(Direction.LEFT)
                                }
                            }
                        } else {
                            if (abs(offsetY) > threshold) {
                                if (offsetY > 0) {
                                    viewModel.move(Direction.DOWN)
                                } else {
                                    viewModel.move(Direction.UP)
                                }
                            }
                        }
                        offsetX = 0f
                        offsetY = 0f
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 500.dp)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Menu Button (Temporary placement, ideally in Header or top bar)
            // For now, let's assume the user can swipe from edge or we add a button.
            // Let's add a small menu button at top left if needed, or just rely on drawer gesture?
            // Drawer gesture might conflict with game swipes.
            // Better to add a menu button.
            
            Box(modifier = Modifier.fillMaxWidth()) {
                androidx.compose.material3.IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = TextColor
                    )
                }
            }

            Header(
                score = gameState.score,
                bestScore = bestScore,
                onNewGame = { viewModel.restart() }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box {
                GameBoard(grid = gameState.grid)
                
                GameOverlay(
                    isGameOver = gameState.over,
                    isGameWon = gameState.won && !gameState.keepPlaying,
                    onTryAgain = { viewModel.restart() },
                    onKeepPlaying = { viewModel.keepPlaying() }
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            if (gameState.mode != icather.pages.dev.a2048ultimate.model.GameMode.CLASSIC) {
                PowerupBar(
                    onUndo = { viewModel.undo() },
                    canUndo = gameState.canUndo
                )
            }
        }
    }
}