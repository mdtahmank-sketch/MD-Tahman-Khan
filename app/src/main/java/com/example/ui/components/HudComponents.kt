package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.GameEntity
import com.example.data.model.SystemMetrics
import com.example.ui.theme.DarLinkAqua
import com.example.ui.theme.DarLinkCard
import com.example.ui.theme.DarLinkCyan
import com.example.ui.theme.DarLinkDarkBg
import com.example.ui.theme.DarLinkGlowCyan
import com.example.ui.theme.DarLinkGreen
import com.example.ui.theme.DarLinkOrange
import com.example.ui.theme.DarLinkPurple
import com.example.ui.theme.DarLinkRed
import com.example.ui.theme.DarLinkSurface
import com.example.ui.theme.DarLinkYellow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun SystemHudDashboard(
    metrics: SystemMetrics,
    modifier: Modifier = Modifier,
    onCleanMemoryClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("system_hud_dashboard"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarLinkSurface),
        border = BorderStroke(1.dp, Brush.horizontalGradient(listOf(DarLinkCyan.copy(alpha = 0.5f), DarLinkOrange.copy(alpha = 0.3f))))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with Engine status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(DarLinkCyan, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DAR-LINK 3.0 SYSTEM HUD",
                        color = DarLinkCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DarLinkCyan.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, DarLinkCyan.copy(alpha = 0.4f)),
                    modifier = Modifier.clickable { onCleanMemoryClick() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Quick Clean",
                            tint = DarLinkCyan,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "কুইক ক্লিন",
                            color = DarLinkCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4 Grid metrics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // RAM Gauge
                val ramUsedGb = String.format("%.1f", metrics.ramUsedBytes / (1024.0 * 1024.0 * 1024.0))
                val ramTotalGb = String.format("%.1f", metrics.ramTotalBytes / (1024.0 * 1024.0 * 1024.0))
                HudMetricTile(
                    title = "র‍্যাম মেমরি",
                    value = "${metrics.ramUsagePercent}%",
                    subValue = "$ramUsedGb / $ramTotalGb GB",
                    progress = metrics.ramUsagePercent / 100f,
                    icon = Icons.Default.Memory,
                    accentColor = if (metrics.ramUsagePercent > 80) DarLinkOrange else DarLinkCyan,
                    modifier = Modifier.weight(1f)
                )

                // CPU Load
                HudMetricTile(
                    title = "CPU লোড",
                    value = "${metrics.cpuLoadPercent}%",
                    subValue = "${metrics.cpuFrequencyGhz} GHz Peak",
                    progress = metrics.cpuLoadPercent / 100f,
                    icon = Icons.Default.Speed,
                    accentColor = DarLinkOrange,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Battery & Temp
                val tempColor = when {
                    metrics.batteryTempCelsius >= 40f -> DarLinkRed
                    metrics.batteryTempCelsius >= 37f -> DarLinkOrange
                    else -> DarLinkGreen
                }
                HudMetricTile(
                    title = "ডিভাইস হিট",
                    value = "${metrics.batteryTempCelsius}°C",
                    subValue = "${metrics.batteryPercent}% ব্যাটারি",
                    progress = (metrics.batteryTempCelsius / 50f).coerceIn(0f, 1f),
                    icon = Icons.Default.Thermostat,
                    accentColor = tempColor,
                    modifier = Modifier.weight(1f)
                )

                // Network Ping
                val pingColor = when {
                    metrics.networkPingMs <= 35 -> DarLinkGreen
                    metrics.networkPingMs <= 70 -> DarLinkYellow
                    else -> DarLinkRed
                }
                HudMetricTile(
                    title = "পিং ল্যাটেন্সি",
                    value = "${metrics.networkPingMs}ms",
                    subValue = "লো ল্যাগ মোড",
                    progress = (metrics.networkPingMs / 120f).coerceIn(0f, 1f),
                    icon = Icons.Default.Wifi,
                    accentColor = pingColor,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun HudMetricTile(
    title: String,
    value: String,
    subValue: String,
    progress: Float,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = DarLinkCard,
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                color = TextWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )

            Text(
                text = subValue,
                color = TextMuted,
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(CircleShape),
                color = accentColor,
                trackColor = accentColor.copy(alpha = 0.15f)
            )
        }
    }
}

@Composable
fun TurboBoostButton(
    isBoosting: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isBoosting) 1.06f else 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = modifier
            .testTag("turbo_boost_button")
            .scale(pulseScale)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        DarLinkOrange,
                        Color(0xFFFF3E00),
                        DarLinkCyan
                    )
                )
            )
            .clickable(enabled = !isBoosting) { onClick() }
            .padding(horizontal = 24.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isBoosting) {
                CircularProgressIndicator(
                    color = TextWhite,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.5.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "দার-লিংক বুস্টিং...",
                    color = TextWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            } else {
                Icon(
                    imageVector = Icons.Default.RocketLaunch,
                    contentDescription = "Turbo Boost",
                    tint = TextWhite,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "1-ট্যাপ টার্বো বুস্ট",
                        color = TextWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "র‍্যাম ক্লিন ও CPU টার্বো ওভারক্লক",
                        color = TextWhite.copy(alpha = 0.85f),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
fun GameCardItem(
    game: GameEntity,
    onLaunchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("game_card_${game.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarLinkCard),
        border = BorderStroke(1.dp, DarLinkCyan.copy(alpha = 0.25f))
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(DarLinkSurface, DarLinkCyan.copy(alpha = 0.25f))
                                )
                            )
                            .border(1.dp, DarLinkCyan.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = DarLinkCyan,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = game.title,
                            color = TextWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = game.genre,
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                            Text(
                                text = " • ",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "${game.targetFps} FPS",
                                color = DarLinkCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("game_settings_btn_${game.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Game Settings",
                        tint = TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Badges row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (game.performanceMode == "BEAST") DarLinkOrange.copy(alpha = 0.15f) else DarLinkCyan.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, if (game.performanceMode == "BEAST") DarLinkOrange.copy(alpha = 0.4f) else DarLinkCyan.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = if (game.performanceMode == "BEAST") "⚡ বিস্ট মোড" else "⚖️ ব্যালান্সড",
                        color = if (game.performanceMode == "BEAST") DarLinkOrange else DarLinkCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = DarLinkPurple.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, DarLinkPurple.copy(alpha = 0.35f))
                ) {
                    Text(
                        text = "৩৬০Hz টাচ",
                        color = DarLinkPurple,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                if (game.dndEnabled) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = DarLinkGreen.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, DarLinkGreen.copy(alpha = 0.35f))
                    ) {
                        Text(
                            text = "🛡️ DND অন",
                            color = DarLinkGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Launch Button
            Button(
                onClick = onLaunchClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("launch_game_${game.id}"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarLinkCyan
                )
            ) {
                Icon(
                    imageVector = Icons.Default.RocketLaunch,
                    contentDescription = null,
                    tint = DarLinkDarkBg,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "বুস্ট করে ওপেন করুন (Launch Boosted)",
                    color = DarLinkDarkBg,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
