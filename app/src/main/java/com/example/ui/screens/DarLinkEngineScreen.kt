package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DisplaySettings
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.TouchApp
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.BoosterPreferenceEntity
import com.example.data.model.GraphicEnhancement
import com.example.data.model.PerformanceMode
import com.example.ui.theme.DarLinkAqua
import com.example.ui.theme.DarLinkCard
import com.example.ui.theme.DarLinkCyan
import com.example.ui.theme.DarLinkDarkBg
import com.example.ui.theme.DarLinkGreen
import com.example.ui.theme.DarLinkOrange
import com.example.ui.theme.DarLinkPurple
import com.example.ui.theme.DarLinkRed
import com.example.ui.theme.DarLinkSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun DarLinkEngineScreen(
    preferences: BoosterPreferenceEntity,
    onUpdatePreferences: (BoosterPreferenceEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedMode by remember(preferences) { mutableStateOf(preferences.globalPerformanceMode) }
    var touchRate by remember(preferences) { mutableIntStateOf(preferences.touchSamplingHz) }
    var bypassCharging by remember(preferences) { mutableStateOf(preferences.bypassCharging) }
    var antiMistouch by remember(preferences) { mutableStateOf(preferences.antiMistouch) }
    var floatingBar by remember(preferences) { mutableStateOf(preferences.floatingAssistantBar) }
    var selectedGraphic by remember { mutableStateOf(GraphicEnhancement.HDR_VIVID) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarLinkDarkBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Engine Branding Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarLinkSurface),
                border = BorderStroke(1.dp, Brush.horizontalGradient(listOf(DarLinkCyan, DarLinkOrange)))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(DarLinkCyan.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = DarLinkCyan,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "DAR-LINK 3.0 ENGINE",
                                    color = TextWhite,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "ইনফিনিক্স হার্ডওয়্যার অপ্টিমাইজেশন কোর",
                                    color = DarLinkCyan,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarLinkGreen.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, DarLinkGreen.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "অ্যাক্টিভ",
                                color = DarLinkGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "গেম খেলার সময় প্রসেসর কোর, জিপিইউ রেন্ডারিং, রিফ্রেশ রেট এবং থার্মাল কন্ট্রোল সর্বোচ্চ ক্ষমতায় পরিচালিত হয়।",
                        color = TextMuted,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Performance Mode Selector Cards
        item {
            Text(
                text = "পারফরম্যান্স ইঞ্জিন মোড নির্বাচন:",
                color = TextWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(PerformanceMode.entries.size) { index ->
            val mode = PerformanceMode.entries[index]
            val isSelected = selectedMode == mode.name

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedMode = mode.name
                        onUpdatePreferences(preferences.copy(globalPerformanceMode = mode.name))
                    }
                    .testTag("mode_${mode.name}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = if (isSelected) DarLinkCard else DarLinkSurface),
                border = BorderStroke(
                    if (isSelected) 1.5.dp else 1.dp,
                    if (isSelected) mode.color else DarLinkCard
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(mode.color.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (mode == PerformanceMode.BEAST) Icons.Default.Speed else if (mode == PerformanceMode.BALANCED) Icons.Default.ElectricBolt else Icons.Default.BatteryChargingFull,
                                contentDescription = null,
                                tint = mode.color,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = mode.titleBn,
                                color = TextWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = mode.subtitleBn,
                                color = TextMuted,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selected",
                            tint = mode.color,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        // Touch Sampling Rate Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarLinkSurface),
                border = BorderStroke(1.dp, DarLinkCyan.copy(alpha = 0.25f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TouchApp,
                                contentDescription = null,
                                tint = DarLinkCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "টাচ স্যাম্পলিং ওভারড্রাইভ",
                                color = TextWhite,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = DarLinkCyan.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "$touchRate Hz",
                                color = DarLinkCyan,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "ফ্রি ফায়ার ও পাবজি গেমে দ্রুত হেডশট ড্র্যাগ ও একুরেট নিশানা করার জন্য রেসপন্স স্পিড বৃদ্ধি করে।",
                        color = TextMuted,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(180, 240, 360).forEach { rate ->
                            FilterChip(
                                selected = touchRate == rate,
                                onClick = {
                                    touchRate = rate
                                    onUpdatePreferences(preferences.copy(touchSamplingHz = rate))
                                },
                                label = { Text("$rate Hz") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = DarLinkCyan,
                                    selectedLabelColor = DarLinkDarkBg,
                                    containerColor = DarLinkCard,
                                    labelColor = TextWhite
                                )
                            )
                        }
                    }
                }
            }
        }

        // Graphic Optimization Filters
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarLinkSurface),
                border = BorderStroke(1.dp, DarLinkPurple.copy(alpha = 0.25f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DisplaySettings,
                            contentDescription = null,
                            tint = DarLinkPurple,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "দার-লিংক গ্রাফিক্স এনহ্যান্সার",
                            color = TextWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "কালার ফিল্টারিং ও শ্যাডো বুস্টারের মাধ্যমে দূরের ও লুকিয়ে থাকা শত্রুদের স্পষ্ট দেখা যায়।",
                        color = TextMuted,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    GraphicEnhancement.entries.forEach { filter ->
                        val isFilterSelected = selectedGraphic == filter
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { selectedGraphic = filter },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isFilterSelected) DarLinkCard else Color.Transparent,
                            border = BorderStroke(
                                1.dp,
                                if (isFilterSelected) DarLinkPurple else DarLinkCard.copy(alpha = 0.5f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "${filter.labelBn} (${filter.labelEn})",
                                        color = if (isFilterSelected) DarLinkPurple else TextWhite,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = filter.descriptionBn,
                                        color = TextMuted,
                                        fontSize = 10.sp
                                    )
                                }
                                if (isFilterSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = DarLinkPurple,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bypass Charging & Thermal Protection
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Thermostat,
                                contentDescription = null,
                                tint = DarLinkOrange,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "বাইপাস চার্জিং প্রটেকশন",
                                    color = TextWhite,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "চার্জিং অবস্থায় ব্যাটারিতে চাপ না দিয়ে সরাসরি মাদারবোর্ডে বিদ্যুৎ সরবরাহ করে (ফোন ৪.৫°C পর্যন্ত ঠান্ডা রাখে)",
                                    color = TextMuted,
                                    fontSize = 10.sp,
                                    lineHeight = 14.sp
                                )
                            }
                        }

                        Switch(
                            checked = bypassCharging,
                            onCheckedChange = {
                                bypassCharging = it
                                onUpdatePreferences(preferences.copy(bypassCharging = it))
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = DarLinkOrange,
                                checkedTrackColor = DarLinkOrange.copy(alpha = 0.4f)
                            )
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = DarLinkCyan,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "অ্যান্টি-থ্রটলিং স্ট্যাবিলাইজার",
                                    color = TextWhite,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "দীর্ঘক্ষণ খেলার পরও ফ্রেম ড্রপ এবং হিটিং ল্যাগ লক প্রতিরোধ করে",
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            }
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
                }
            }
        }
    }
}
