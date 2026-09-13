package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.local.entity.GameEntity
import com.example.data.model.InstalledAppInfo
import com.example.data.model.SystemMetrics
import com.example.ui.components.GameCardItem
import com.example.ui.components.SystemHudDashboard
import com.example.ui.components.TurboBoostButton
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun XArenaSpaceScreen(
    games: List<GameEntity>,
    metrics: SystemMetrics,
    isBoosting: Boolean,
    installedApps: List<InstalledAppInfo>,
    onTriggerBoost: () -> Unit,
    onLaunchGame: (GameEntity) -> Unit,
    onUpdateGame: (GameEntity) -> Unit,
    onDeleteGame: (Long, String) -> Unit,
    onAddGame: (String, String, String) -> Unit,
    onRefreshMetrics: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddGameSheet by remember { mutableStateOf(false) }
    var selectedGameForSettings by remember { mutableStateOf<GameEntity?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarLinkDarkBg)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Banner with Dar-Link Branding
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(135.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DarLinkSurface),
                    border = BorderStroke(1.dp, DarLinkCyan.copy(alpha = 0.35f))
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.darlink_engine_banner),
                            contentDescription = "Dar-Link Engine Banner",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(
                                            DarLinkDarkBg.copy(alpha = 0.92f),
                                            DarLinkDarkBg.copy(alpha = 0.70f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = DarLinkCyan.copy(alpha = 0.2f),
                                    border = BorderStroke(1.dp, DarLinkCyan)
                                ) {
                                    Text(
                                        text = "INFINIX GAMING",
                                        color = DarLinkCyan,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "XARENA 3.0",
                                    color = DarLinkOrange,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Dar-Link গেমিং স্পেস",
                                color = TextWhite,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "হাইপার ফ্রেম রেট • নো ল্যাগ • অ্যান্টি-মিসটাচ",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Real-time System HUD
            item {
                SystemHudDashboard(
                    metrics = metrics,
                    onCleanMemoryClick = onTriggerBoost
                )
            }

            // Big 1-Tap Turbo Boost Button
            item {
                TurboBoostButton(
                    isBoosting = isBoosting,
                    onClick = onTriggerBoost,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Games Header with Add Game Action
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "আমার গেম লাইব্রেরি (Game Dock)",
                            color = TextWhite,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${games.size} টি গেম অপটিমাইজেশনের জন্য তালিকাভুক্ত",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    Button(
                        onClick = { showAddGameSheet = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarLinkSurface),
                        border = BorderStroke(1.dp, DarLinkCyan.copy(alpha = 0.4f)),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("add_game_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Game",
                            tint = DarLinkCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+ গেম যোগ করুন",
                            color = DarLinkCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Game Cards
            items(games, key = { it.id }) { game ->
                GameCardItem(
                    game = game,
                    onLaunchClick = { onLaunchGame(game) },
                    onSettingsClick = { selectedGameForSettings = game }
                )
            }
        }

        // Add Game Bottom Sheet
        if (showAddGameSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAddGameSheet = false },
                containerColor = DarLinkSurface,
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "গেম স্পেসে অ্যাপ বা গেম যোগ করুন",
                        color = TextWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "আপনার ফোনের যেকোনো অ্যাপ দার-লিংক বুস্টারে যোগ করুন",
                        color = TextMuted,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_apps_field"),
                        placeholder = { Text("গেম বা অ্যাপের নাম খুঁজুন...", color = TextMuted) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = DarLinkCyan)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DarLinkCyan,
                            unfocusedBorderColor = DarLinkCyan.copy(alpha = 0.3f),
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    val filteredApps = installedApps.filter {
                        it.appName.contains(searchQuery, ignoreCase = true)
                    }

                    if (filteredApps.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "কোনো অ্যাপ পাওয়া যায়নি বা লোড হচ্ছে...",
                                color = TextMuted,
                                fontSize = 13.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filteredApps) { app ->
                                val alreadyAdded = games.any { it.packageName == app.packageName }
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(enabled = !alreadyAdded) {
                                            onAddGame(app.appName, app.packageName, if (app.isGame) "Mobile Game" else "Custom App")
                                            showAddGameSheet = false
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    color = DarLinkCard,
                                    border = BorderStroke(1.dp, if (alreadyAdded) Color.Transparent else DarLinkCyan.copy(alpha = 0.2f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(DarLinkCyan.copy(alpha = 0.15f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.SportsEsports,
                                                    contentDescription = null,
                                                    tint = DarLinkCyan,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column {
                                                Text(
                                                    text = app.appName,
                                                    color = TextWhite,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                                Text(
                                                    text = app.packageName,
                                                    color = TextMuted,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }

                                        if (alreadyAdded) {
                                            Text(
                                                text = "যুক্ত আছে",
                                                color = DarLinkGreen,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        } else {
                                            Text(
                                                text = "+ যোগ করুন",
                                                color = DarLinkCyan,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Game Settings Dialog
        selectedGameForSettings?.let { game ->
            GameSettingsDialog(
                game = game,
                onDismiss = { selectedGameForSettings = null },
                onSave = { updatedGame ->
                    onUpdateGame(updatedGame)
                    selectedGameForSettings = null
                },
                onDelete = {
                    onDeleteGame(game.id, game.title)
                    selectedGameForSettings = null
                }
            )
        }
    }
}

@Composable
fun GameSettingsDialog(
    game: GameEntity,
    onDismiss: () -> Unit,
    onSave: (GameEntity) -> Unit,
    onDelete: () -> Unit
) {
    var mode by remember { mutableStateOf(game.performanceMode) }
    var targetFps by remember { mutableStateOf(game.targetFps) }
    var dndEnabled by remember { mutableStateOf(game.dndEnabled) }
    var networkBoost by remember { mutableStateOf(game.networkBoostEnabled) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarLinkSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Gamepad,
                    contentDescription = null,
                    tint = DarLinkCyan,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${game.title} কনফিগারেশন",
                    color = TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Performance mode selection
                Text(text = "পারফরম্যান্স ইঞ্জিন মোড:", color = TextMuted, fontSize = 12.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = mode == "BEAST",
                        onClick = { mode = "BEAST" },
                        label = { Text("⚡ বিস্ট (Max)") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DarLinkOrange,
                            selectedLabelColor = TextWhite
                        )
                    )
                    FilterChip(
                        selected = mode == "BALANCED",
                        onClick = { mode = "BALANCED" },
                        label = { Text("⚖️ ব্যালান্সড") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DarLinkCyan,
                            selectedLabelColor = DarLinkDarkBg
                        )
                    )
                }

                // FPS Target
                Text(text = "টার্গেট ফ্রেম রেট (FPS):", color = TextMuted, fontSize = 12.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(60, 90, 120).forEach { fps ->
                        FilterChip(
                            selected = targetFps == fps,
                            onClick = { targetFps = fps },
                            label = { Text("$fps FPS") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DarLinkCyan,
                                selectedLabelColor = DarLinkDarkBg
                            )
                        )
                    }
                }

                // Switches
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "DND কল ও নোটিফিকেশন ব্লকার", color = TextWhite, fontSize = 13.sp)
                        Text(text = "খেলার সময় কল ও পপ-আপ রদ করুন", color = TextMuted, fontSize = 10.sp)
                    }
                    Switch(
                        checked = dndEnabled,
                        onCheckedChange = { dndEnabled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = DarLinkCyan, checkedTrackColor = DarLinkCyan.copy(alpha = 0.4f))
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "ডুয়াল নেটওয়ার্ক এক্সিলারেশন", color = TextWhite, fontSize = 13.sp)
                        Text(text = "ওয়াইফাই + ডাটা লো ল্যাগ মোড", color = TextMuted, fontSize = 10.sp)
                    }
                    Switch(
                        checked = networkBoost,
                        onCheckedChange = { networkBoost = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = DarLinkCyan, checkedTrackColor = DarLinkCyan.copy(alpha = 0.4f))
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        game.copy(
                            performanceMode = mode,
                            targetFps = targetFps,
                            dndEnabled = dndEnabled,
                            networkBoostEnabled = networkBoost
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = DarLinkCyan)
            ) {
                Text(text = "সংরক্ষণ করুন", color = DarLinkDarkBg, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            Row {
                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(contentColor = DarLinkRed)
                ) {
                    Text(text = "মুছে ফেলুন")
                }
                TextButton(onClick = onDismiss) {
                    Text(text = "বাতিল", color = TextMuted)
                }
            }
        }
    )
}
