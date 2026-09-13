package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.FireOrangeLight
import com.example.ui.theme.FireOrangePrimary
import com.example.ui.theme.FireRedAccent
import com.example.ui.theme.GameDarkSurface
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.GoldCoinGlow
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.SensitivityViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun AimPracticeScreen(
    viewModel: SensitivityViewModel,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    var headshotCount by remember { mutableIntStateOf(0) }
    var totalShots by remember { mutableIntStateOf(0) }
    var lastHitType by remember { mutableStateOf<String?>(null) }
    var damageNumber by remember { mutableStateOf<String?>(null) }
    var dragSpeedText by remember { mutableStateOf<String?>(null) }
    var bonusClaimed by remember { mutableStateOf(false) }

    var buttonOffsetX by remember { mutableFloatStateOf(0f) }
    var buttonOffsetY by remember { mutableFloatStateOf(0f) }

    val headshotGoal = 5

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Stats & Coin reward
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = GameDarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2B2E48))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "🎯 হেডশট ড্র্যাগ প্র্যাকটিস",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextWhite
                    )
                    Text(
                        text = "ফায়ার বাটন সোজা উপরে ড্র্যাগ করুন",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }

                // Reward Progress
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF281F0E))
                        .border(1.dp, GoldCoin.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = "Coin", tint = GoldCoin, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$headshotCount/$headshotGoal হেডশট (+১০🪙)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldCoinGlow
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Target Simulation Canvas Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF0F101A))
                .border(1.dp, Color(0xFF25283D), RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Target Silhouette Canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val centerX = size.width / 2
                val targetCenterY = size.height * 0.35f

                // Body Silhouette
                drawRoundRect(
                    color = Color(0xFF1E2138),
                    topLeft = Offset(centerX - 45.dp.toPx(), targetCenterY + 18.dp.toPx()),
                    size = androidx.compose.ui.geometry.Size(90.dp.toPx(), 110.dp.toPx()),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx())
                )

                // Head Silhouette
                drawCircle(
                    color = if (lastHitType == "HEADSHOT") FireRedAccent else Color(0xFF2A2E4D),
                    radius = 28.dp.toPx(),
                    center = Offset(centerX, targetCenterY - 18.dp.toPx())
                )

                // Crosshair Reticle lines
                val reticleRadius = 40.dp.toPx()
                drawLine(
                    color = NeonCyan.copy(alpha = 0.4f),
                    start = Offset(centerX - reticleRadius, targetCenterY - 18.dp.toPx()),
                    end = Offset(centerX + reticleRadius, targetCenterY - 18.dp.toPx()),
                    strokeWidth = 1.5.dp.toPx()
                )
                drawLine(
                    color = NeonCyan.copy(alpha = 0.4f),
                    start = Offset(centerX, targetCenterY - 18.dp.toPx() - reticleRadius),
                    end = Offset(centerX, targetCenterY - 18.dp.toPx() + reticleRadius),
                    strokeWidth = 1.5.dp.toPx()
                )
            }

            // Damage Popup Floating Text
            if (damageNumber != null) {
                Box(
                    modifier = Modifier.offset(y = (-80).dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = damageNumber ?: "",
                            fontSize = if (lastHitType == "HEADSHOT") 32.sp else 22.sp,
                            fontWeight = FontWeight.Black,
                            color = if (lastHitType == "HEADSHOT") FireRedAccent else Color(0xFFFFD54F)
                        )
                        if (lastHitType == "HEADSHOT") {
                            Text(
                                text = "💥 RED HEADSHOT 💥",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = FireOrangeLight
                            )
                        }
                    }
                }
            }

            // Bottom Drag Control Area
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ফায়ার বাটন স্পর্শ করে উপরে ড্র্যাগ করুন",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Draggable Fire Button
                    Box(
                        modifier = Modifier
                            .offset { IntOffset(buttonOffsetX.roundToInt(), buttonOffsetY.roundToInt()) }
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(FireRedAccent, FireOrangePrimary)
                                )
                            )
                            .border(3.dp, Color.White.copy(alpha = 0.8f), CircleShape)
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragStart = {
                                        buttonOffsetX = 0f
                                        buttonOffsetY = 0f
                                    },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        buttonOffsetX = (buttonOffsetX + dragAmount.x).coerceIn(-60f, 60f)
                                        buttonOffsetY = (buttonOffsetY + dragAmount.y).coerceIn(-200f, 20f)
                                    },
                                    onDragEnd = {
                                        totalShots++
                                        val verticalUpDrag = -buttonOffsetY
                                        if (verticalUpDrag > 70f) {
                                            // Headshot achieved!
                                            lastHitType = "HEADSHOT"
                                            damageNumber = "550"
                                            dragSpeedText = "পারফেক্ট J-ড্র্যাগ স্পিড! নিখুঁত হেডশট!"
                                            headshotCount++

                                            if (headshotCount >= headshotGoal && !bonusClaimed) {
                                                bonusClaimed = true
                                                viewModel.recordAimPracticeHeadshot { /* rewarded */ }
                                            }
                                        } else if (verticalUpDrag > 20f) {
                                            lastHitType = "BODY"
                                            damageNumber = "78"
                                            dragSpeedText = "বডি শট! আরও দ্রুত উপরে ড্র্যাগ করুন।"
                                        } else {
                                            lastHitType = "MISS"
                                            damageNumber = "MISS"
                                            dragSpeedText = "লক্ষ্য মিস! আঙুল সোজা উপরে টানুন।"
                                        }

                                        // Reset button position
                                        buttonOffsetX = 0f
                                        buttonOffsetY = 0f
                                    }
                                )
                            }
                            .testTag("aim_practice_fire_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdsClick,
                            contentDescription = "Fire",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Feedback Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = GameDarkSurface,
            border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFF282B42))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dragSpeedText ?: "ড্র্যাগ শুরু করুন এবং রিফ্লেক্স পরীক্ষা করুন",
                    fontSize = 12.sp,
                    color = if (lastHitType == "HEADSHOT") NeonGreen else TextWhite,
                    fontWeight = if (lastHitType == "HEADSHOT") FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = {
                        headshotCount = 0
                        totalShots = 0
                        lastHitType = null
                        damageNumber = null
                        dragSpeedText = null
                        bonusClaimed = false
                    }
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reset", tint = TextMuted, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
