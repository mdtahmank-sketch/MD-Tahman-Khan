package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.FireOrangePrimary
import com.example.ui.theme.FireRedAccent
import com.example.ui.theme.GameDarkSurface
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.GoldCoinGlow
import com.example.ui.theme.NeonCyan

@Composable
fun CoinHeader(
    coins: Int,
    isRewardAvailable: Boolean,
    onDailyRewardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "coin_pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isRewardAvailable) 1.08f else 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "coin_scale"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("coin_header"),
        color = GameDarkSurface,
        tonalElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Title with Gaming Flame
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(FireOrangePrimary, FireRedAccent)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "FF Logo",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "FF SENSI",
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(FireOrangePrimary)
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "PRO",
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = Color.White
                            )
                        }
                    }
                    Text(
                        text = "সেরা হেডশট সেন্সিটিভিটি",
                        fontSize = 11.sp,
                        color = NeonCyan,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Coin Balance & Daily Reward Badge
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Coins Counter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF221E14))
                        .border(1.dp, GoldCoin.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = "কয়েন",
                            tint = GoldCoin,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$coins",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            color = GoldCoinGlow
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Daily Reward Quick Button
                Box(
                    modifier = Modifier
                        .scale(if (isRewardAvailable) scale else 1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (isRewardAvailable) {
                                Brush.horizontalGradient(listOf(FireOrangePrimary, FireRedAccent))
                            } else {
                                Brush.horizontalGradient(listOf(Color(0xFF2A2B3D), Color(0xFF1E1F2E)))
                            }
                        )
                        .clickable { onDailyRewardClick() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("daily_reward_header_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CardGiftcard,
                            contentDescription = "Daily Reward",
                            tint = if (isRewardAvailable) Color.White else Color(0xFFA0A0B8),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isRewardAvailable) "রিওয়ার্ড!" else "ডেইলি",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isRewardAvailable) Color.White else Color(0xFFA0A0B8)
                        )
                    }
                }
            }
        }
    }
}
