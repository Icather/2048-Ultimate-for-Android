package icather.pages.dev.a2048ultimate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import icather.pages.dev.a2048ultimate.ui.theme.*

@Composable
fun Header(
    score: Long,
    bestScore: Long,
    onNewGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "2048",
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = TextColor
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ScoreBox(label = "SCORE", score = score)
                ScoreBox(label = "BEST", score = bestScore)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Join the numbers and get to the ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("2048 tile!")
                    }
                },
                color = TextColor,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )

            Box(
                modifier = Modifier
                    .background(ButtonBackground, RoundedCornerShape(3.dp))
                    .clickable { onNewGame() }
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "New Game",
                    color = ButtonText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun ScoreBox(label: String, score: Long) {
    Box(
        modifier = Modifier
            .background(GridBackground, RoundedCornerShape(3.dp))
            .padding(horizontal = 15.dp, vertical = 8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                color = Color(0xFFEEE4DA),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = score.toString(),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
