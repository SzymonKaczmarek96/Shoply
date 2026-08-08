import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoply.presentation.utils.UiDim
import com.example.shoply.presentation.utils.UiDim.AccentPink
import com.example.shoply.presentation.utils.UiDim.CardBackground
import com.example.shoply.presentation.utils.UiDim.DividerColor
import com.example.shoply.presentation.utils.UiDim.TextDark
import com.example.shoply.presentation.utils.UiDim.TextMuted


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToPassword: () -> Unit = {},
) {
    var myTasksOnly by remember { mutableStateOf(false) }
    var dailyDigest by remember { mutableStateOf(false) }
    var darkMode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiDim.BackgroundColor)
            .padding(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Settings",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "WORKSPACE",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                SettingsClickableItem(title = "Default Board", value = "Shoply Project") {}
                HorizontalDivider(color = DividerColor, thickness = 1.dp)
                SettingsClickableItem(title = "Change Password", onClick = onNavigateToPassword)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "NOTIFICATIONS & AUTOMATION",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                SettingsSwitchItem(
                    title = "Synchronize list",
                    description = "Notify only when assigned or mentioned",
                    checked = myTasksOnly,
                    onCheckedChange = { myTasksOnly = it },
                    switchColor = AccentPink
                )
                HorizontalDivider(color = DividerColor, thickness = 1.dp)
                SettingsSwitchItem(
                    title = "Offline Mode",
                    description = "Morning summary of team tasks",
                    checked = dailyDigest,
                    onCheckedChange = { dailyDigest = it },
                    switchColor = AccentPink
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "PREFERENCES",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                SettingsSwitchItem(
                    title = "Dark Mode",
                    checked = darkMode,
                    onCheckedChange = { darkMode = it },
                    switchColor = Color(0xFFCBD5E1)
                )
                HorizontalDivider(color = DividerColor, thickness = 1.dp)
                SettingsClickableItem(title = "Clear Local Cache", value = "12.4 MB") {}
            }
        }

        Spacer(modifier = Modifier.weight(1f))

    }
}

@Composable
private fun SettingsClickableItem(
    title: String,
    value: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, fontSize = 16.sp, color = TextDark, fontWeight = FontWeight.Medium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (value != null) {
                Text(
                    text = value,
                    fontSize = 14.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Go",
                tint = TextMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun SettingsSwitchItem(
    title: String,
    description: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    switchColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 16.sp, color = TextDark, fontWeight = FontWeight.Medium)
            if (description != null) {
                Text(text = description, fontSize = 12.sp, color = TextMuted)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = switchColor,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFE2E8F0)
            )
        )
    }
}


@Preview()
@Composable
fun SettingsPreview() {
    SettingsScreen()
}