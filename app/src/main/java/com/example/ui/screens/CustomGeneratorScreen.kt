package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.CustomPresetEntity
import com.example.ui.components.FireButtonPreview
import com.example.ui.components.SensitivitySliderBar
import com.example.ui.theme.FireOrangeLight
import com.example.ui.theme.FireOrangePrimary
import com.example.ui.theme.FireRedAccent
import com.example.ui.theme.GameDarkSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.SensitivityViewModel
import kotlinx.coroutines.launch

@Composable
fun CustomGeneratorScreen(
    viewModel: SensitivityViewModel,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val customPresets by viewModel.customPresets.collectAsState()

    // Configuration selections
    val phoneBrands = listOf("Samsung", "Xiaomi/Poco", "Realme", "Vivo", "Oppo", "Infinix/Tecno", "OnePlus", "iPhone/iPad", "অন্যান্য")
    val ramOptions = listOf("2GB", "3GB", "4GB", "6GB", "8GB", "12GB+")
    val refreshRates = listOf("60Hz", "90Hz", "120Hz")
    val playStyles = listOf("One-Tap স্পেশাল", "AR রাশার", "ব্যালান্সড অল-রাউন্ডার", "স্নাইপার")

    var selectedBrand by remember { mutableStateOf("Samsung") }
    var selectedRam by remember { mutableStateOf("4GB") }
    var selectedHz by remember { mutableStateOf("90Hz") }
    var selectedStyle by remember { mutableStateOf("One-Tap স্পেশাল") }

    // Calculated Sensitivity Values
    var generalVal by remember { mutableFloatStateOf(95f) }
    var redDotVal by remember { mutableFloatStateOf(90f) }
    var scope2xVal by remember { mutableFloatStateOf(88f) }
    var scope4xVal by remember { mutableFloatStateOf(84f) }
    var sniperVal by remember { mutableFloatStateOf(65f) }
    var freeLookVal by remember { mutableFloatStateOf(70f) }
    var buttonSizeVal by remember { mutableIntStateOf(46) }
    var dpiVal by remember { mutableIntStateOf(420) }

    fun calculateAutoSensi() {
        val ramInt = when (selectedRam) {
            "2GB" -> 2
            "3GB" -> 3
            "4GB" -> 4
            "6GB" -> 6
            "8GB" -> 8
            else -> 12
        }
        val is120 = selectedHz == "120Hz"
        val is90 = selectedHz == "90Hz"

        when {
            ramInt <= 3 -> {
                generalVal = 100f
                redDotVal = 100f
                scope2xVal = 98f
                scope4xVal = 95f
                sniperVal = 70f
                freeLookVal = 80f
                buttonSizeVal = 52
                dpiVal = 360
            }
            ramInt in 4..6 -> {
                generalVal = if (is90) 94f else 96f
                redDotVal = if (is90) 90f else 92f
                scope2xVal = 88f
                scope4xVal = 85f
                sniperVal = 64f
                freeLookVal = 72f
                buttonSizeVal = 46
                dpiVal = if (is90) 440 else 420
            }
            else -> {
                generalVal = if (is120) 89f else 92f
                redDotVal = if (is120) 85f else 88f
                scope2xVal = 82f
                scope4xVal = 78f
                sniperVal = 58f
                freeLookVal = 65f
                buttonSizeVal = 42
                dpiVal = if (is120) 480 else 450
            }
        }

        if (selectedStyle == "One-Tap স্পেশাল") {
            generalVal = (generalVal + 3).coerceAtMost(100f)
            redDotVal = (redDotVal + 2).coerceAtMost(100f)
            buttonSizeVal = (buttonSizeVal - 2).coerceAtLeast(38)
        } else if (selectedStyle == "স্নাইপার") {
            sniperVal = 85f
            generalVal = 90f
            buttonSizeVal = 50
        }
    }

    var customPresetName by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
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
                            imageVector = Icons.Default.Calculate,
                            contentDescription = "Calculator",
                            tint = FireOrangePrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "স্মার্ট ডিভাইস সেন্সিটিভিটি ক্যালকুলেটর",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "আপনার ফোনের মডেল, র‍্যাম ও রিফ্রেশ রেট অনুযায়ী সেরা কনফিগারেশন জেনারেট করুন।",
                        fontSize = 11.sp,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Brand Selection Chips
                    Text(text = "১. ফোন ব্র্যান্ড নির্বাচন করুন:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        phoneBrands.forEach { brand ->
                            val isSelected = selectedBrand == brand
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) FireOrangePrimary else Color(0xFF1B1D2C))
                                    .border(1.dp, if (isSelected) FireOrangeLight else Color(0xFF282B42), RoundedCornerShape(8.dp))
                                    .clickable {
                                        selectedBrand = brand
                                        calculateAutoSensi()
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = brand,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else TextMuted
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // RAM Selection
                    Text(text = "২. ফোনের র‍্যাম (RAM):", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ramOptions.forEach { ram ->
                            val isSelected = selectedRam == ram
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 2.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) NeonCyan else Color(0xFF1B1D2C))
                                    .border(1.dp, if (isSelected) Color.White else Color(0xFF282B42), RoundedCornerShape(8.dp))
                                    .clickable {
                                        selectedRam = ram
                                        calculateAutoSensi()
                                    }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = ram,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.Black else TextWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Screen Hz Selection
                    Text(text = "৩. স্ক্রিন রিফ্রেশ রেট (Hz):", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        refreshRates.forEach { hz ->
                            val isSelected = selectedHz == hz
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) Color(0xFF2E2452) else Color(0xFF1B1D2C))
                                    .border(1.dp, if (isSelected) Color(0xFFB388FF) else Color(0xFF282B42), RoundedCornerShape(8.dp))
                                    .clickable {
                                        selectedHz = hz
                                        calculateAutoSensi()
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = hz,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color(0xFFD1C4E9) else TextWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Calculate Button
                    Button(
                        onClick = { calculateAutoSensi() },
                        colors = ButtonDefaults.buttonColors(containerColor = FireOrangePrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("calculate_sensi_button")
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Calculate", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "সেন্সিটিভিটি জেনারেট করুন", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }

        // Generated Results Card
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = GameDarkSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f))
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
                        Text(
                            text = "🎯 প্রস্তাবিত সেটিংস ($selectedBrand, $selectedRam, $selectedHz)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    SensitivitySliderBar(nameBn = "সাধারণ", nameEn = "General", value = generalVal.toInt(), accentColor = FireOrangePrimary)
                    SensitivitySliderBar(nameBn = "রেড ডট", nameEn = "Red Dot", value = redDotVal.toInt(), accentColor = FireRedAccent)
                    SensitivitySliderBar(nameBn = "২X স্কোপ", nameEn = "2X Scope", value = scope2xVal.toInt(), accentColor = NeonCyan)
                    SensitivitySliderBar(nameBn = "৪X স্কোপ", nameEn = "4X Scope", value = scope4xVal.toInt(), accentColor = NeonCyan)
                    SensitivitySliderBar(nameBn = "স্নাইপার", nameEn = "Sniper Scope", value = sniperVal.toInt(), accentColor = Color(0xFFFFD700))
                    SensitivitySliderBar(nameBn = "ফ্রি লুক", nameEn = "Free Look", value = freeLookVal.toInt(), accentColor = TextMuted)

                    Spacer(modifier = Modifier.height(12.dp))

                    FireButtonPreview(fireButtonSize = buttonSizeVal, dpi = dpiVal)

                    Spacer(modifier = Modifier.height(14.dp))

                    // Name input to save
                    OutlinedTextField(
                        value = customPresetName,
                        onValueChange = { customPresetName = it },
                        placeholder = { Text("প্রিসেটের নাম দিন (যেমন: মাই স্যামসাং হেডশট)", fontSize = 12.sp, color = TextMuted) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("custom_preset_name_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = Color(0xFF282B42),
                            focusedContainerColor = Color(0xFF161826),
                            unfocusedContainerColor = Color(0xFF161826),
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Save Button
                        Button(
                            onClick = {
                                viewModel.saveCustomPreset(
                                    name = customPresetName.ifBlank { "$selectedBrand $selectedRam কনফিগ" },
                                    deviceInfo = "$selectedBrand | $selectedRam | $selectedHz",
                                    general = generalVal.toInt(),
                                    redDot = redDotVal.toInt(),
                                    scope2x = scope2xVal.toInt(),
                                    scope4x = scope4xVal.toInt(),
                                    sniperScope = sniperVal.toInt(),
                                    freeLook = freeLookVal.toInt(),
                                    buttonSize = buttonSizeVal,
                                    dpi = dpiVal
                                )
                                customPresetName = ""
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("✅ প্রিসেট সেভ করা হয়েছে!")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("save_preset_button")
                        ) {
                            Icon(imageVector = Icons.Default.BookmarkAdd, contentDescription = "Save", tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "সেভ করুন", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 12.sp)
                        }

                        // Copy Button
                        Button(
                            onClick = {
                                val textToCopy = buildString {
                                    appendLine("🔥 কাস্টম ফ্রি ফায়ার সেন্সিটিভিটি ($selectedBrand $selectedRam $selectedHz) 🔥")
                                    appendLine("• সাধারণ (General): ${generalVal.toInt()}")
                                    appendLine("• রেড ডট (Red Dot): ${redDotVal.toInt()}")
                                    appendLine("• ২X স্কোপ: ${scope2xVal.toInt()}")
                                    appendLine("• ৪X স্কোপ: ${scope4xVal.toInt()}")
                                    appendLine("• স্নাইপার স্কোপ: ${sniperVal.toInt()}")
                                    appendLine("• ফ্রি লুক: ${freeLookVal.toInt()}")
                                    appendLine("• ফায়ার বাটন: $buttonSizeVal%")
                                    appendLine("• DPI: $dpiVal")
                                    appendLine("- FF Sensitivity Pro অ্যাপ দ্বারা জেনারেটেড")
                                }
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("FF Custom Sensitivity", textToCopy)
                                clipboard.setPrimaryClip(clip)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("✅ সেটিংস ক্লিপবোর্ডে কপি হয়েছে!")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF262945)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("copy_calculated_button")
                        ) {
                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "কপি করুন", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Saved Custom Presets List
        if (customPresets.isNotEmpty()) {
            item {
                Text(
                    text = "📁 আপনার সেভ করা কাস্টম প্রিসেটসমূহ (${customPresets.size})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }

            items(customPresets, key = { it.id }) { preset ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = GameDarkSurface,
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFF2A2D48))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = preset.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextWhite)
                                Text(text = preset.deviceInfo, fontSize = 11.sp, color = TextMuted)
                            }
                            IconButton(onClick = { viewModel.deleteCustomPreset(preset.id) }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFFF5252), modifier = Modifier.size(18.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "General: ${preset.general}", fontSize = 11.sp, color = FireOrangeLight)
                            Text(text = "Red Dot: ${preset.redDot}", fontSize = 11.sp, color = NeonCyan)
                            Text(text = "বাটন: ${preset.fireButtonSize}%", fontSize = 11.sp, color = TextWhite)
                            Text(text = "DPI: ${preset.dpi}", fontSize = 11.sp, color = Color(0xFFFFD700))
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
