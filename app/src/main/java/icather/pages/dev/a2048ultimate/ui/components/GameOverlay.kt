package icather.pages.dev.a2048ultimate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import icather.pages.dev.a2048ultimate.ui.theme.*

@Composable
fun GameOverlay(
    isGameOver: Boolean,
    isGameWon: Boolean,
    onTryAgain: () -> Unit,
    onKeepPlaying: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isGameOver && !isGameWon) return

    val backgroundColor = if (isGameWon) OverlayWonBackground else OverlayBackground
    val message = if (isGameWon) "You win!" else "Game over!"

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor, RoundedCornerShape(6.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = message,
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(30.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                if (isGameWon) {
                    OverlayButton(text = "Keep going", onClick = onKeepPlaying)
                }
                OverlayButton(text = "Try again", onClick = onTryAgain)
            }
        }
    }
}

@Composable
fun OverlayButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(ButtonBackground, RoundedCornerShape(3.dp))
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text = text,
            color = ButtonText,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}
