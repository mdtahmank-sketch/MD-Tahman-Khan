package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SensitivityPreset
import com.example.ui.theme.FireOrangePrimary
import com.example.ui.theme.FireRedAccent
import com.example.ui.theme.GameDarkSurface
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.GoldCoinGlow
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.VipBadgeBg

@Composable
fun SensitivityCard(
    preset: SensitivityPreset,
    isUnlocked: Boolean,
    userCoins: Int,
    onUnlockClick: () -> Unit,
    onCopyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(!preset.isPremium || isUnlocked) }
    var copiedRecently by remember { mutableStateOf(false) }

    val isLocked = preset.isPremium && !isUnlocked

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("preset_card_${preset.id}")
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        color = GameDarkSurface,
        tonalElevation = if (preset.isPremium) 8.dp else 4.dp,
        border = androidx.compose.foundation.BorderStroke(
            width = if (preset.isPremium && isUnlocked) 1.5.dp else if (isLocked) 1.dp else 0.8.dp,
            color = if (preset.isPremium && isUnlocked) {
                GoldCoin
            } else if (isLocked) {
                Color(0xFFFF9100).copy(alpha = 0.6f)
            } else {
                Color(0xFF2E314D)
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Card Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (preset.isPremium) {
                                        VipBadgeBg
                                    } else {
                                        Color(0xFF1E2838)
                                    }
                                )
                                .border(
                                    0.5.dp,
                                    if (preset.isPremium) GoldCoin else NeonCyan.copy(alpha = 0.5f),
                                    RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = preset.badge,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (preset.isPremium) GoldCoinGlow else NeonCyan
                            )
                        }

                        if (preset.isPremium) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isUnlocked) Color(0xFF0F3818) else Color(0xFF381515))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (isUnlocked) "আনলকড ✓" else "৫০ কয়েন লক",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isUnlocked) NeonGreen else Color(0xFFFF7070)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = preset.titleBn,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )

                    Text(
                        text = preset.subtitle,
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                // Lock/Unlock or Expand Toggle Icon
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            if (isLocked) Color(0xFF331600)
                            else if (preset.isPremium) Color(0xFF28220A)
                            else Color(0xFF1E2033)
                        )
                        .clickable { expanded = !expanded },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isLocked) Icons.Default.Lock
                        else if (expanded) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand or Lock",
                        tint = if (isLocked) GoldCoin
                        else if (preset.isPremium) GoldCoinGlow
                        else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Description
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = preset.descriptionBn,
                fontSize = 12.sp,
                color = Color(0xFFAEB2D0),
                lineHeight = 16.sp
            )

            // Locked Premium Preview Overlay
            if (isLocked) {
                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1A1312))
                        .border(1.dp, Color(0xFF4D2B0F), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Locked",
                                tint = GoldCoin,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "প্রিমিয়াম ভিআইপি সেন্সিটিভিটি লকড",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldCoinGlow
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "এই বিশেষ ভিআইপি সেন্সিটিভিটি আনলক করতে ৫০ কয়েন প্রয়োজন। ডেইলি রিওয়ার্ড থেকে কয়েন আয় করুন!",
                            fontSize = 11.sp,
                            color = Color(0xFFD4BBA5),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = onUnlockClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FireOrangePrimary
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("unlock_button_${preset.id}")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.MonetizationOn,
                                    contentDescription = "Coin",
                                    tint = GoldCoin,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "৫০ কয়েন দিয়ে আনলক করুন",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            } else {
                // Content shown when unlocked / free
                AnimatedVisibility(
                    visible = expanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        // Slider bars
                        SensitivitySliderBar(
                            nameBn = "সাধারণ",
                            nameEn = "General",
                            value = preset.general,
                            accentColor = FireOrangePrimary
                        )
                        SensitivitySliderBar(
                            nameBn = "রেড ডট",
                            nameEn = "Red Dot",
                            value = preset.redDot,
                            accentColor = FireRedAccent
                        )
                        SensitivitySliderBar(
                            nameBn = "২X স্কোপ",
                            nameEn = "2X Scope",
                            value = preset.scope2x,
                            accentColor = NeonCyan
                        )
                        SensitivitySliderBar(
                            nameBn = "৪X স্কোপ",
                            nameEn = "4X Scope",
                            value = preset.scope4x,
                            accentColor = NeonCyan
                        )
                        SensitivitySliderBar(
                            nameBn = "স্নাইপার স্কোপ",
                            nameEn = "Sniper Scope",
                            value = preset.sniperScope,
                            accentColor = GoldCoin
                        )
                        SensitivitySliderBar(
                            nameBn = "ফ্রি লুক",
                            nameEn = "Free Look",
                            value = preset.freeLook,
                            accentColor = TextMuted
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Fire Button Preview & DPI
                        FireButtonPreview(
                            fireButtonSize = preset.fireButtonSize,
                            dpi = preset.dpi
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Headshot Secret Tip Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF1E2135))
                                .border(0.5.dp, NeonCyan.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                .padding(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(
                                    imageVector = Icons.Default.SportsEsports,
                                    contentDescription = "Headshot Tip",
                                    tint = NeonCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "হেডশট ড্র্যাগ টেকনিক টিপস:",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NeonCyan
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = preset.headshotTipBn,
                                        fontSize = 11.sp,
                                        color = TextWhite,
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Copy Button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    copiedRecently = true
                                    onCopyClick()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (copiedRecently) NeonGreen else Color(0xFF272A45)
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("copy_button_${preset.id}")
                            ) {
                                Icon(
                                    imageVector = if (copiedRecently) Icons.Default.Check else Icons.Default.ContentCopy,
                                    contentDescription = "Copy",
                                    tint = if (copiedRecently) Color.Black else Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (copiedRecently) "কপি হয়েছে ✓" else "সেন্সিটিভিটি কপি করুন",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (copiedRecently) Color.Black else Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
