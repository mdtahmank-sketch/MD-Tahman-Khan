package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoNotDisturbOn
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.ScreenShare
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.BoosterPreferenceEntity
import com.example.ui.theme.DarLinkAqua
import com.example.ui.theme.DarLinkCard
import com.example.ui.theme.DarLinkCyan
import com.example.ui.theme.DarLinkDarkBg
import com.example.ui.theme.DarLinkGreen
import com.example.ui.theme.DarLinkOrange
import com.example.ui.theme.DarLinkPurple
import com.example.ui.theme.DarLinkRed
import com.example.ui.theme.DarLinkSurface
import com.example.ui.theme.DarLinkYellow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun GamingAssistantScreen(
    preferences: BoosterPreferenceEntity,
    onUpdatePreferences: (BoosterPreferenceEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var floatingBar by remember(preferences) { mutableStateOf(preferences.floatingAssistantBar) }
    var antiMistouch by remember(preferences) { mutableStateOf(preferences.antiMistouch) }
    var dualNetwork by remember(preferences) { mutableStateOf(preferences.networkDualBonding) }
    var crosshairEnabled by remember(preferences) { mutableStateOf(preferences.crosshairEnabled) }
    var crosshairShape by remember(preferences) { mutableStateOf(preferences.crosshairShape) }
    var crosshairColor by remember(preferences) { mutableStateOf(preferences.crosshairColor) }
    var crosshairSize by remember(preferences) { mutableIntStateOf(preferences.crosshairSize) }
    var activeVoiceEffect by remember(preferences) { mutableStateOf(preferences.voiceChangerEffect) }

    var isPreviewingVoice by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarLinkDarkBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Floating Game Assistant Bar Simulation
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarLinkSurface),
                border = BorderStroke(1.dp, DarLinkCyan.copy(alpha = 0.35f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ScreenShare,
                                contentDescription = null,
                                tint = DarLinkCyan,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ইন-গেম ফ্লোটিং অ্যাসিস্ট্যান্ট বার",
                                color = TextWhite,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Switch(
                            checked = floatingBar,
                            onCheckedChange = {
                                floatingBar = it
                                onUpdatePreferences(preferences.copy(floatingAssistantBar = it))
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = DarLinkCyan,
                                checkedTrackColor = DarLinkCyan.copy(alpha = 0.4f)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "গেম খেলার সময় স্ক্রিনের বাঁ পাশ থেকে সোয়াইপ করলেই ইনফিনিক্স স্টাইল টুলবার ওপেন হবে।",
                        color = TextMuted,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Visual Floating Bar Mockup
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = DarLinkCard,
                        border = BorderStroke(1.dp, DarLinkCyan.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FloatingToolbarItem(icon = Icons.Default.Speed, label = "89 FPS", active = true, color = DarLinkGreen)
                            FloatingToolbarItem(icon = Icons.Default.DoNotDisturbOn, label = "DND", active = true, color = DarLinkOrange)
                            FloatingToolbarItem(icon = Icons.Default.Mic, label = "ভয়েস", active = activeVoiceEffect != "NONE", color = DarLinkPurple)
                            FloatingToolbarItem(icon = Icons.Default.CenterFocusStrong, label = "ক্রসহেয়ার", active = crosshairEnabled, color = DarLinkCyan)
                            FloatingToolbarItem(icon = Icons.Default.Chat, label = "ম্যাসেঞ্জার", active = false, color = TextMuted)
                        }
                    }
                }
            }
        }

        // DND Suite & Call Blocker
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarLinkSurface),
                border = BorderStroke(1.dp, DarLinkOrange.copy(alpha = 0.25f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DoNotDisturbOn,
                            contentDescription = null,
                            tint = DarLinkOrange,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ডিএনডি গেমিং ফিল্টার (কল ও নোটিফিকেশন ব্লকার)",
                            color = TextWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    DndFeatureRow(
                        title = "ইনকামিং কল অটো-রিজেক্ট",
                        subtitle = "খেলার সময় হঠাৎ কল এসে গেম মিনিমাইজ হওয়া বন্ধ রাখবে",
                        icon = Icons.Default.CallEnd,
                        color = DarLinkRed
                    )

                    DndFeatureRow(
                        title = "হেডস-আপ নোটিফিকেশন ব্লক",
                        subtitle = "হোয়াটসঅ্যাপ, মেসেঞ্জার ও এসএমএস পপ-আপ স্ক্রিনে আসবে না",
                        icon = Icons.Default.NotificationsOff,
                        color = DarLinkOrange
                    )

                    DndFeatureRow(
                        title = "অ্যালার্ম ও মিডিয়া মিউট",
                        subtitle = "গেম ছাড়া অন্য সিস্টেমের অনাকাঙ্ক্ষিত সাউন্ড মিউট রাখবে",
                        icon = Icons.Default.VolumeUp,
                        color = DarLinkYellow
                    )
                }
            }
        }

        // Anti-Mistouch (মিসটাচ প্রটেকশন)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarLinkSurface),
                border = BorderStroke(1.dp, DarLinkCyan.copy(alpha = 0.25f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = DarLinkCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "অ্যান্টি-মিসটাচ মোড (Mistouch Guard)",
                                color = TextWhite,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Switch(
                            checked = antiMistouch,
                            onCheckedChange = {
                                antiMistouch = it
                                onUpdatePreferences(preferences.copy(antiMistouch = it))
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = DarLinkCyan,
                                checkedTrackColor = DarLinkCyan.copy(alpha = 0.4f)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "ইনটেনসিভ ফাইট বা ড্র্যাগ শটের সময় ভুলবশত স্ট্যাটাস বার নামানো বা ফুলস্ক্রিন হোম সোয়াইপ প্রতিরোধ করে।",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Dual-Channel Network Boost
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarLinkSurface),
                border = BorderStroke(1.dp, DarLinkGreen.copy(alpha = 0.25f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.WifiTethering,
                                contentDescription = null,
                                tint = DarLinkGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ডুয়াল নেটওয়ার্ক অ্যাক্সিলারেটর",
                                color = TextWhite,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Switch(
                            checked = dualNetwork,
                            onCheckedChange = {
                                dualNetwork = it
                                onUpdatePreferences(preferences.copy(networkDualBonding = it))
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = DarLinkGreen,
                                checkedTrackColor = DarLinkGreen.copy(alpha = 0.4f)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "ওয়াইফাই ও ৪জি/৫জি উভয় পথ একসাথে ব্যবহার করে পিং ফ্ল্যাকচুয়েশন এবং প্যাকেট লস শূন্যে নামিয়ে আনে।",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Custom Tactical Crosshair Maker
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarLinkSurface),
                border = BorderStroke(1.dp, DarLinkCyan.copy(alpha = 0.35f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CenterFocusStrong,
                                contentDescription = null,
                                tint = DarLinkCyan,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "কাস্টম এইম ক্রসহেয়ার (Crosshair HUD)",
                                color = TextWhite,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Switch(
                            checked = crosshairEnabled,
                            onCheckedChange = {
                                crosshairEnabled = it
                                onUpdatePreferences(preferences.copy(crosshairEnabled = it))
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = DarLinkCyan,
                                checkedTrackColor = DarLinkCyan.copy(alpha = 0.4f)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "স্নাইপার ও শর্টগান দিয়ে নো-স্কোপ হেডশট মারার জন্য অন-স্ক্রিন ট্যাকটিক্যাল ক্রসহেয়ার।",
                        color = TextMuted,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Live Interactive Crosshair Target Canvas
                    val drawColor = when (crosshairColor) {
                        "RED" -> DarLinkRed
                        "GREEN" -> DarLinkGreen
                        "YELLOW" -> DarLinkYellow
                        else -> DarLinkCyan
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(DarLinkDarkBg)
                            .border(1.dp, DarLinkCyan.copy(alpha = 0.3f), RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val centerX = size.width / 2
                            val centerY = size.height / 2

                            // Draw subtle target concentric grid rings
                            drawCircle(
                                color = Color(0xFF1E2538),
                                radius = 90f,
                                center = Offset(centerX, centerY),
                                style = Stroke(width = 1.5f)
                            )
                            drawCircle(
                                color = Color(0xFF161A28),
                                radius = 160f,
                                center = Offset(centerX, centerY),
                                style = Stroke(width = 1.5f)
                            )

                            // Crosshair rendering
                            val s = crosshairSize.toFloat() * 1.5f

                            when (crosshairShape) {
                                "DOT" -> {
                                    drawCircle(
                                        color = drawColor,
                                        radius = s / 3f,
                                        center = Offset(centerX, centerY)
                                    )
                                }
                                "CIRCLE" -> {
                                    drawCircle(
                                        color = drawColor,
                                        radius = s,
                                        center = Offset(centerX, centerY),
                                        style = Stroke(width = 3.5f)
                                    )
                                    drawCircle(
                                        color = drawColor,
                                        radius = 3.5f,
                                        center = Offset(centerX, centerY)
                                    )
                                }
                                "T_SHAPE" -> {
                                    drawLine(
                                        color = drawColor,
                                        start = Offset(centerX - s, centerY),
                                        end = Offset(centerX + s, centerY),
                                        strokeWidth = 4f
                                    )
                                    drawLine(
                                        color = drawColor,
                                        start = Offset(centerX, centerY),
                                        end = Offset(centerX, centerY + s),
                                        strokeWidth = 4f
                                    )
                                }
                                else -> { // CROSS
                                    drawLine(
                                        color = drawColor,
                                        start = Offset(centerX - s, centerY),
                                        end = Offset(centerX - 8f, centerY),
                                        strokeWidth = 3.5f
                                    )
                                    drawLine(
                                        color = drawColor,
                                        start = Offset(centerX + 8f, centerY),
                                        end = Offset(centerX + s, centerY),
                                        strokeWidth = 3.5f
                                    )
                                    drawLine(
                                        color = drawColor,
                                        start = Offset(centerX, centerY - s),
                                        end = Offset(centerX, centerY - 8f),
                                        strokeWidth = 3.5f
                                    )
                                    drawLine(
                                        color = drawColor,
                                        start = Offset(centerX, centerY + 8f),
                                        end = Offset(centerX, centerY + s),
                                        strokeWidth = 3.5f
                                    )
                                    drawCircle(
                                        color = drawColor,
                                        radius = 2.5f,
                                        center = Offset(centerX, centerY)
                                    )
                                }
                            }
                        }

                        Text(
                            text = "লাইভ প্রিভিউ (Test Target)",
                            color = TextMuted.copy(alpha = 0.6f),
                            fontSize = 10.sp,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Shape selector
                    Text(text = "ক্রসহেয়ার স্টাইল:", color = TextMuted, fontSize = 12.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("CROSS" to "প্লাস (+)", "DOT" to "ডট (•)", "CIRCLE" to "সার্কেল (⊕)", "T_SHAPE" to "টি (T)").forEach { (shape, name) ->
                            FilterChip(
                                selected = crosshairShape == shape,
                                onClick = {
                                    crosshairShape = shape
                                    onUpdatePreferences(preferences.copy(crosshairShape = shape))
                                },
                                label = { Text(name, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = DarLinkCyan,
                                    selectedLabelColor = DarLinkDarkBg
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Color selector
                    Text(text = "ক্রসহেয়ার কালার:", color = TextMuted, fontSize = 12.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf("CYAN" to DarLinkCyan, "GREEN" to DarLinkGreen, "RED" to DarLinkRed, "YELLOW" to DarLinkYellow).forEach { (colorKey, color) ->
                            val isChosen = crosshairColor == colorKey
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .clickable {
                                        crosshairColor = colorKey
                                        onUpdatePreferences(preferences.copy(crosshairColor = colorKey))
                                    }
                                    .border(
                                        if (isChosen) 2.5.dp else 0.dp,
                                        if (isChosen) TextWhite else Color.Transparent,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isChosen) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = if (colorKey == "YELLOW" || colorKey == "CYAN") Color.Black else Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Size Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "সাইজ:", color = TextMuted, fontSize = 12.sp)
                        Text(text = "${crosshairSize}dp", color = DarLinkCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = crosshairSize.toFloat(),
                        onValueChange = {
                            crosshairSize = it.toInt()
                            onUpdatePreferences(preferences.copy(crosshairSize = it.toInt()))
                        },
                        valueRange = 14f..38f,
                        colors = SliderDefaults.colors(
                            thumbColor = DarLinkCyan,
                            activeTrackColor = DarLinkCyan
                        )
                    )
                }
            }
        }

        // Magic Voice Changer
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarLinkSurface),
                border = BorderStroke(1.dp, DarLinkPurple.copy(alpha = 0.35f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = null,
                            tint = DarLinkPurple,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ম্যাজিক ভয়েস চেঞ্জার (XArena Voice Mod)",
                            color = TextWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "গেম খেলার সময় স্কোয়াড টিমমেটদের সাথে কথা বলার জন্য আপনার মাইক্রোফোন ভয়েস পরিবর্তন করুন।",
                        color = TextMuted,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    val effects = listOf(
                        Triple("NONE", "অরিজিনাল ভয়েস", "স্বাভাবিক কণ্ঠ"),
                        Triple("ROBOT", "🤖 সাইবার রোবট", "মেটালিক মেকা ভয়েস"),
                        Triple("GIRL", "👩 মেয়ের কণ্ঠ", "সুইট অ্যান্ড কিউট পিচ"),
                        Triple("ALIEN", "👽 এলিয়েন", "স্পেস ট্রাভেলার ভয়েস"),
                        Triple("SOLDIER", "🎖️ কমান্ডো", "ডিপ আর্মি কমব্যাট ভয়েস")
                    )

                    effects.forEach { (effectId, name, desc) ->
                        val isSelected = activeVoiceEffect == effectId
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    activeVoiceEffect = effectId
                                    onUpdatePreferences(preferences.copy(voiceChangerEffect = effectId))
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) DarLinkCard else Color.Transparent,
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) DarLinkPurple else DarLinkCard.copy(alpha = 0.4f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = name,
                                        color = if (isSelected) DarLinkPurple else TextWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = desc,
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                }

                                if (isSelected) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = DarLinkPurple.copy(alpha = 0.2f),
                                        border = BorderStroke(1.dp, DarLinkPurple)
                                    ) {
                                        Text(
                                            text = "সক্রিয়",
                                            color = DarLinkPurple,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FloatingToolbarItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    active: Boolean,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (active) color.copy(alpha = 0.18f) else Color.Transparent)
                .border(1.dp, if (active) color.copy(alpha = 0.6f) else Color.DarkGray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (active) color else TextMuted,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = if (active) color else TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun DndFeatureRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    var enabled by remember { mutableStateOf(true) }

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
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    color = TextWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }
        }

        Switch(
            checked = enabled,
            onCheckedChange = { enabled = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = color,
                checkedTrackColor = color.copy(alpha = 0.4f)
            )
        )
    }
}
