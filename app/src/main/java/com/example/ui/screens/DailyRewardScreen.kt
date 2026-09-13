package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.UserWalletEntity
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

@Composable
fun DailyRewardScreen(
    viewModel: SensitivityViewModel,
    onNavigateToVip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val wallet by viewModel.wallet.collectAsState()
    val isSpinning by viewModel.isSpinning.collectAsState()
    val currentQuiz by viewModel.currentQuizQuestion.collectAsState()

    val isRewardAvailable = viewModel.isDailyRewardAvailable()
    val streakDays = wallet.streakDays

    var lastWonSpinCoins by remember { mutableStateOf<Int?>(null) }
    var quizFeedback by remember { mutableStateOf<String?>(null) }
    var selectedQuizOption by remember { mutableStateOf<Int?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Balance & Streak Card
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("daily_reward_balance_card"),
                shape = RoundedCornerShape(20.dp),
                color = GameDarkSurface,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    Brush.horizontalGradient(listOf(GoldCoin.copy(alpha = 0.6f), FireOrangePrimary.copy(alpha = 0.4f)))
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF261D10), Color(0xFF141522))
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "আপনার বর্তমান ব্যালেন্স",
                                    fontSize = 12.sp,
                                    color = Color(0xFFDEC3A2)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.MonetizationOn,
                                        contentDescription = "Coin",
                                        tint = GoldCoin,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${wallet.coins} কয়েন",
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.Black,
                                        color = GoldCoinGlow
                                    )
                                }
                            }

                            // Streak Badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF381F0D))
                                    .border(1.dp, FireOrangePrimary, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Stars,
                                            contentDescription = "Streak",
                                            tint = FireOrangeLight,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "স্ট্রিক",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = FireOrangeLight
                                        )
                                    }
                                    Text(
                                        text = "দিন ${wallet.streakDays}/৭",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "💡 ৫০ কয়েন জমিয়ে যেকোনো প্রিমিয়াম ভিআইপি সেন্সিটিভিটি আনলক করুন!",
                            fontSize = 12.sp,
                            color = NeonCyan
                        )
                    }
                }
            }
        }

        // 7-Day Daily Reward Streak Matrix
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = GameDarkSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF26283D))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Calendar",
                                tint = FireOrangePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "৭-দিনের ডেইলি চেক-ইন রিওয়ার্ড",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Days Grid 1-7
                    val streakDaysRewards = listOf(15, 20, 25, 30, 40, 50, 100)
                    val nextClaimDay = if (isRewardAvailable) {
                        (streakDays % 7) + 1
                    } else {
                        streakDays
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (i in 1..7) {
                            val rewardAmount = streakDaysRewards[i - 1]
                            val isClaimed = !isRewardAvailable && i <= streakDays
                            val isCurrentTarget = isRewardAvailable && i == nextClaimDay
                            val isSpecial = i == 6 || i == 7

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 2.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        when {
                                            isClaimed -> Color(0xFF132B1B)
                                            isCurrentTarget -> Color(0xFF45240B)
                                            isSpecial -> Color(0xFF281C10)
                                            else -> Color(0xFF1C1E2E)
                                        }
                                    )
                                    .border(
                                        width = if (isCurrentTarget) 1.5.dp else 0.8.dp,
                                        color = when {
                                            isClaimed -> NeonGreen
                                            isCurrentTarget -> GoldCoin
                                            isSpecial -> Color(0xFFB8860B)
                                            else -> Color(0xFF2B2E47)
                                        },
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(vertical = 8.dp, horizontal = 2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "দিন $i",
                                        fontSize = 10.sp,
                                        color = if (isClaimed) NeonGreen else TextMuted
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    if (isClaimed) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Claimed",
                                            tint = NeonGreen,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    } else {
                                        Text(
                                            text = "+$rewardAmount",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isCurrentTarget) GoldCoinGlow else TextWhite
                                        )
                                        Text(
                                            text = "🪙",
                                            fontSize = 9.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Claim Button
                    Button(
                        onClick = {
                            viewModel.claimDailyReward { /* Result handled in VM flow */ }
                        },
                        enabled = isRewardAvailable,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FireOrangePrimary,
                            disabledContainerColor = Color(0xFF232535)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("claim_daily_reward_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isRewardAvailable) Icons.Default.CardGiftcard else Icons.Default.Check,
                                contentDescription = "Claim",
                                tint = if (isRewardAvailable) Color.White else Color(0xFF7E8199)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isRewardAvailable) {
                                    val nextReward = streakDaysRewards[((streakDays) % 7)]
                                    "আজকের রিওয়ার্ড সংগ্রহ করুন (+$nextReward কয়েন)"
                                } else {
                                    "আজকের রিওয়ার্ড ইতিমধ্যে সংগৃহীত ✓ (আগামীকাল আবার আসুন)"
                                },
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isRewardAvailable) Color.White else Color(0xFF7E8199)
                            )
                        }
                    }
                }
            }
        }

        // Lucky Spin / Mystery Drop Section
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = GameDarkSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF26283D))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Spin",
                            tint = GoldCoin,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "লাকি স্পিন ও মিস্ট্রি কয়েন বক্স",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "স্পিন ঘুরিয়ে ১০ থেকে ৫০ পর্যন্ত ফ্রি কয়েন জিতে নিন!",
                        fontSize = 12.sp,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Wheel / Prize Preview
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1B1D2C))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            if (isSpinning) {
                                CircularProgressIndicator(
                                    color = GoldCoin,
                                    modifier = Modifier.size(44.dp),
                                    strokeWidth = 4.dp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "স্পিন ঘুরছে... কয়েন লোড হচ্ছে...",
                                    fontSize = 12.sp,
                                    color = GoldCoinGlow,
                                    fontWeight = FontWeight.Medium
                                )
                            } else {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val samples = listOf("১০🪙", "২০🪙", "৩০🪙", "৫০🪙")
                                    samples.forEach { tag ->
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(0xFF282B42))
                                                .border(0.5.dp, GoldCoin.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = tag,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = GoldCoinGlow
                                            )
                                        }
                                    }
                                }

                                if (lastWonSpinCoins != null) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "🎉 আপনি স্পিনে $lastWonSpinCoins কয়েন জিতেছেন!",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NeonGreen
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            viewModel.spinWheel { won ->
                                lastWonSpinCoins = won
                            }
                        },
                        enabled = !isSpinning,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD47900)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("spin_wheel_button")
                    ) {
                        Text(
                            text = if (isSpinning) "স্পিন হচ্ছে..." else "লাকি স্পিন ঘোরান (+কয়েন)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Free Fire Headshot Quiz Challenge
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = GameDarkSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF26283D))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = "Quiz",
                                tint = NeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ফ্রি ফায়ার হেডশট কুইজ",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF0F3628))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "+১০ 🪙 রিওয়ার্ড",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = currentQuiz.questionBn,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextWhite,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Quiz Options
                    currentQuiz.optionsBn.forEachIndexed { index, option ->
                        val isSelected = selectedQuizOption == index
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSelected) Color(0xFF2E2B4A) else Color(0xFF1B1D2C)
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) NeonCyan else Color(0xFF2B2E48),
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    selectedQuizOption = index
                                    viewModel.submitQuizAnswer(index) { success, msg ->
                                        quizFeedback = msg
                                    }
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${('ক'.code + index).toChar()}.",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonCyan
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = option,
                                    fontSize = 12.sp,
                                    color = TextWhite
                                )
                            }
                        }
                    }

                    if (quizFeedback != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = quizFeedback ?: "",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (quizFeedback?.contains("সঠিক") == true) NeonGreen else Color(0xFFFF6666)
                        )
                    }
                }
            }
        }

        // Quick Unlock CTA banner
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToVip() },
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF241A10),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldCoin.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "৫০ কয়েন আছে? ভিআইপি আনলক করুন",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldCoinGlow
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Ruok FF, White444 ও Raistar এসপোর্ট সেন্সিটিভিটি আনলক করুন",
                            fontSize = 11.sp,
                            color = Color(0xFFDFCEBC)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.LockOpen,
                        contentDescription = "Unlock VIP",
                        tint = GoldCoin,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
