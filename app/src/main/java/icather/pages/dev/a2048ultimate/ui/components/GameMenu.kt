package icather.pages.dev.a2048ultimate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import icather.pages.dev.a2048ultimate.model.GameMode
import icather.pages.dev.a2048ultimate.ui.theme.*

@Composable
fun GameMenu(
    currentMode: GameMode,
    onModeSelected: (GameMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(GameBackground)
            .padding(20.dp)
    ) {
        Text(
            text = "Menu",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = TextColor,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        HorizontalDivider(color = TextColor.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(20.dp))

        MenuItem(
            title = "Standard",
            description = "2048 with powerups",
            isSelected = currentMode == GameMode.STANDARD,
            onClick = { onModeSelected(GameMode.STANDARD) }
        )

        MenuItem(
            title = "Classic",
            description = "The original 2048, no undo",
            isSelected = currentMode == GameMode.CLASSIC,
            onClick = { onModeSelected(GameMode.CLASSIC) }
        )

        MenuItem(
            title = "Plus",
            description = "Bonus powerups and dark board!",
            isSelected = currentMode == GameMode.PLUS,
            onClick = { onModeSelected(GameMode.PLUS) },
            isPlus = true
        )
    }
}

@Composable
fun MenuItem(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    isPlus: Boolean = false
) {
    val backgroundColor = if (isSelected) GridBackground else Color.Transparent
    val titleColor = if (isPlus) Color(0xFFEBC562) else TextColor
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Icon placeholder could go here
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )
        }
        Text(
            text = description,
            fontSize = 14.sp,
            color = TextColor.copy(alpha = 0.7f)
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
}
