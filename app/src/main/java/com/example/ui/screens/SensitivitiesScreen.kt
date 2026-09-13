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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.data.model.PresetCategory
import com.example.data.model.SensitivityData
import com.example.data.model.SensitivityPreset
import com.example.ui.components.SensitivityCard
import com.example.ui.theme.FireOrangeLight
import com.example.ui.theme.FireOrangePrimary
import com.example.ui.theme.GameDarkBackground
import com.example.ui.theme.GameDarkSurface
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.GoldCoinGlow
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.SensitivityViewModel
import kotlinx.coroutines.launch

@Composable
fun SensitivitiesScreen(
    viewModel: SensitivityViewModel,
    snackbarHostState: SnackbarHostState,
    onNavigateToDailyReward: () -> Unit,
    showOnlyVip: Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val wallet by viewModel.wallet.collectAsState()
    val unlockedIds by viewModel.unlockedPresetIds.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var presetToUnlock by remember { mutableStateOf<SensitivityPreset?>(null) }
    var showHowToApplyDialog by remember { mutableStateOf(false) }

    // Filter presets
    val allPresets = remember { SensitivityData.presets }
    val filteredPresets = allPresets.filter { preset ->
        val matchesVipTab = if (showOnlyVip) preset.isPremium else true
        val matchesCategory = if (showOnlyVip) true else (selectedCategory == PresetCategory.ALL || preset.category == selectedCategory)
        val matchesSearch = searchQuery.isBlank() ||
                preset.titleBn.contains(searchQuery, ignoreCase = true) ||
                preset.titleEn.contains(searchQuery, ignoreCase = true) ||
                preset.subtitle.contains(searchQuery, ignoreCase = true) ||
                preset.badge.contains(searchQuery, ignoreCase = true)

        matchesVipTab && matchesCategory && matchesSearch
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Search & Guide Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = {
                    Text(
                        text = if (showOnlyVip) "ভিআইপি সেন্সিটিভিটি খুঁজুন..." else "ডিভাইস, শটগান বা এমপি৪০ খুঁজুন...",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = FireOrangePrimary,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .testTag("search_presets_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FireOrangePrimary,
                    unfocusedBorderColor = Color(0xFF2B2E48),
                    focusedContainerColor = GameDarkSurface,
                    unfocusedContainerColor = GameDarkSurface,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Guide Button
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1E2135))
                    .border(1.dp, Color(0xFF2B2E48), RoundedCornerShape(12.dp))
                    .clickable { showHowToApplyDialog = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.HelpOutline,
                    contentDescription = "How to Apply",
                    tint = NeonCyan,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // Category Chips (only shown if not in dedicated VIP tab)
        if (!showOnlyVip) {
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PresetCategory.values().forEach { category ->
                    val isSelected = selectedCategory == category
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (isSelected) FireOrangePrimary else Color(0xFF181A28)
                            )
                            .border(
                                1.dp,
                                if (isSelected) FireOrangeLight else Color(0xFF2B2E48),
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { viewModel.setCategory(category) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = category.labelBn,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else TextMuted
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Preset List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header hint for VIP Tab
            if (showOnlyVip) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFF2B1D0E),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldCoin.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "VIP",
                                tint = GoldCoin,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "প্রিমিয়াম ভিআইপি জোন (৫০ কয়েন প্রতি কনফিগ)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldCoinGlow
                                )
                                Text(
                                    text = "বিশ্বসেরা এসপোর্ট প্লেয়ারদের গোপন সেন্সিটিভিটি আনলক করুন। আপনার কাছে আছে: ${wallet.coins} কয়েন।",
                                    fontSize = 11.sp,
                                    color = Color(0xFFDCC8B4)
                                )
                            }
                        }
                    }
                }
            }

            if (filteredPresets.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "কোনো সেন্সিটিভিটি পাওয়া যায়নি!",
                            color = TextMuted,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            items(filteredPresets, key = { it.id }) { preset ->
                val isUnlocked = !preset.isPremium || unlockedIds.contains(preset.id)

                SensitivityCard(
                    preset = preset,
                    isUnlocked = isUnlocked,
                    userCoins = wallet.coins,
                    onUnlockClick = {
                        presetToUnlock = preset
                    },
                    onCopyClick = {
                        val textToCopy = buildString {
                            appendLine("🔥 ${preset.titleBn} (${preset.titleEn}) 🔥")
                            appendLine("• সাধারণ (General): ${preset.general}")
                            appendLine("• রেড ডট (Red Dot): ${preset.redDot}")
                            appendLine("• ২X স্কোপ (2X Scope): ${preset.scope2x}")
                            appendLine("• ৪X স্কোপ (4X Scope): ${preset.scope4x}")
                            appendLine("• স্নাইপার স্কোপ (Sniper): ${preset.sniperScope}")
                            appendLine("• ফ্রি লুক (Free Look): ${preset.freeLook}")
                            appendLine("• ফায়ার বাটন সাইজ: ${preset.fireButtonSize}%")
                            appendLine("• DPI: ${preset.dpi}")
                            appendLine("• হেডশট ট্রিক: ${preset.headshotTipBn}")
                            appendLine("- FF Sensitivity Pro অ্যাপ দ্বারা তৈরি")
                        }
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("FF Sensitivity", textToCopy)
                        clipboard.setPrimaryClip(clip)

                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("✅ সেন্সিটিভিটি ক্লিপবোর্ডে কপি করা হয়েছে!")
                        }
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Unlock Confirmation Dialog
    presetToUnlock?.let { preset ->
        val hasEnoughCoins = wallet.coins >= preset.costCoins

        AlertDialog(
            onDismissRequest = { presetToUnlock = null },
            containerColor = GameDarkSurface,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (hasEnoughCoins) Icons.Default.MonetizationOn else Icons.Default.Lock,
                        contentDescription = "Coin",
                        tint = GoldCoin,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (hasEnoughCoins) "প্রিমিয়াম সেন্সিটিভিটি আনলক" else "কয়েন অপর্যাপ্ত!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = TextWhite
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = preset.titleBn,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = GoldCoinGlow
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    if (hasEnoughCoins) {
                        Text(
                            text = "আপনি কি ৫০ কয়েন খরচ করে এই ভিআইপি এসপোর্ট সেন্সিটিভিটি আনলক করতে চান?\n\nআপনার বর্তমান ব্যালেন্স: ${wallet.coins} কয়েন\nআনলক করার পর থাকবে: ${wallet.coins - 50} কয়েন",
                            fontSize = 12.sp,
                            color = TextWhite,
                            lineHeight = 16.sp
                        )
                    } else {
                        Text(
                            text = "এই ভিআইপি সেন্সিটিভিটি আনলক করতে ৫০ কয়েন প্রয়োজন।\n\nআপনার বর্তমান ব্যালেন্স: ${wallet.coins} কয়েন\nআরও প্রয়োজন: ${50 - wallet.coins} কয়েন।\n\nডেইলি রিওয়ার্ড ও লাকি স্পিন থেকে ফ্রি কয়েন আয় করুন!",
                            fontSize = 12.sp,
                            color = Color(0xFFFF9999),
                            lineHeight = 16.sp
                        )
                    }
                }
            },
            confirmButton = {
                if (hasEnoughCoins) {
                    Button(
                        onClick = {
                            viewModel.unlockPreset(preset) { /* Result handled */ }
                            presetToUnlock = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = FireOrangePrimary)
                    ) {
                        Text(text = "৫০ কয়েন দিয়ে আনলক করুন", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = {
                            presetToUnlock = null
                            onNavigateToDailyReward()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldCoin)
                    ) {
                        Text(
                            text = "ডেইলি রিওয়ার্ড থেকে কয়েন নিন",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { presetToUnlock = null }) {
                    Text(text = "বাতিল", color = TextMuted)
                }
            }
        )
    }

    // How to Apply in Free Fire Dialog
    if (showHowToApplyDialog) {
        AlertDialog(
            onDismissRequest = { showHowToApplyDialog = false },
            containerColor = GameDarkSurface,
            title = {
                Text(
                    text = "🎮 ফ্রি ফায়ারে কিভাবে সেট করবেন?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextWhite
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "১. Free Fire / Free Fire MAX গেমটি ওপেন করুন।",
                        fontSize = 12.sp,
                        color = TextWhite
                    )
                    Text(
                        text = "২. উপরে ডানপাশের Settings (গিয়ার আইকন) এ ক্লিক করুন।",
                        fontSize = 12.sp,
                        color = TextWhite
                    )
                    Text(
                        text = "৩. বামপাশের Sensitivity ট্যাবে প্রবেশ করুন।",
                        fontSize = 12.sp,
                        color = TextWhite
                    )
                    Text(
                        text = "৪. এই অ্যাপ থেকে দেখানো মানগুলো হুবহু সেট করুন।",
                        fontSize = 12.sp,
                        color = TextWhite
                    )
                    Text(
                        text = "৫. Controls > Custom HUD এ গিয়ে Fire Button সাইজটি পরিবর্তন করুন।",
                        fontSize = 12.sp,
                        color = TextWhite
                    )
                    Text(
                        text = "৬. ট্রেনিং গ্রাউন্ডে ৫ মিনিট J-ড্র্যাগ প্র্যাকটিস করুন!",
                        fontSize = 12.sp,
                        color = NeonCyan,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showHowToApplyDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = FireOrangePrimary)
                ) {
                    Text(text = "বুঝেছি", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
