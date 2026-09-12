package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MakimaCrimson
import com.example.ui.theme.MakimaStatusGreen
import com.example.ui.theme.MakimaStatusOrange
import com.example.ui.theme.MakimaTextPrimary
import com.example.ui.theme.MakimaTextSecondary

@Composable
fun LogItemView(
    logMessage: String,
    modifier: Modifier = Modifier
) {
    val indicatorColor = when {
        logMessage.startsWith("✅") -> MakimaStatusGreen
        logMessage.startsWith("🚀") || logMessage.startsWith("⚡") -> MakimaCrimson
        logMessage.startsWith("📍") || logMessage.startsWith("🧠") -> MakimaStatusOrange
        else -> MakimaTextSecondary
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(indicatorColor)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = logMessage,
            fontSize = 12.sp,
            color = MakimaTextPrimary,
            fontFamily = FontFamily.Monospace,
            lineHeight = 16.sp
        )
    }
}
