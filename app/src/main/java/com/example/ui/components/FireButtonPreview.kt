package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.FireOrangePrimary
import com.example.ui.theme.FireRedAccent
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun FireButtonPreview(
    fireButtonSize: Int,
    dpi: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF161826))
            .border(1.dp, Color(0xFF2B2D45), RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Fire Button Info & Visual Circle
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF222538))
                    .border(2.dp, FireOrangePrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Inner button representation scaled to size
                val innerSize = (24 + (fireButtonSize - 35) * 0.8f).coerceIn(20f, 44f).dp
                Box(
                    modifier = Modifier
                        .size(innerSize)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(FireRedAccent, FireOrangePrimary)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AdsClick,
                        contentDescription = "Fire Button Target",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "ফায়ার বাটন সাইজ",
                    fontSize = 12.sp,
                    color = TextMuted
                )
                Text(
                    text = "$fireButtonSize%",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite
                )
                Text(
                    text = "স্ক্রিনের নিচে-ডানে সেট করুন",
                    fontSize = 10.sp,
                    color = Color(0xFFA0A0C0)
                )
            }
        }

        // Right: DPI recommendation
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Speed,
                    contentDescription = "DPI",
                    tint = NeonCyan,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "DPI সেটিংস",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }
            Text(
                text = if (dpi <= 360) "ডিফল্ট (৩৬০)" else "$dpi DPI",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = NeonCyan
            )
            Text(
                text = "বিকাশকারী বিকল্প (Dev Options)",
                fontSize = 9.sp,
                color = Color(0xFF7E80A0)
            )
        }
    }
}
