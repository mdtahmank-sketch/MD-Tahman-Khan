package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.LaunchOverlayDialog
import com.example.ui.screens.DarLinkEngineScreen
import com.example.ui.screens.FpsNetworkScreen
import com.example.ui.screens.GamingAssistantScreen
import com.example.ui.screens.XArenaSpaceScreen
import com.example.ui.theme.DarLinkAqua
import com.example.ui.theme.DarLinkCard
import com.example.ui.theme.DarLinkCyan
import com.example.ui.theme.DarLinkDarkBg
import com.example.ui.theme.DarLinkGreen
import com.example.ui.theme.DarLinkOrange
import com.example.ui.theme.DarLinkPurple
import com.example.ui.theme.DarLinkSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.GameBoosterViewModel
import kotlinx.coroutines.flow.collectLatest

enum class BoosterTab(val titleBn: String, val index: Int) {
    XARENA_SPACE("গেম স্পেস", 0),
    DAR_LINK("দার-লিংক ৩.০", 1),
    ASSISTANT("টুলকিট", 2),
    FPS_MONITOR("মনিটর ও পিং", 3)
}

@Composable
fun MainAppScreen(
    viewModel: GameBoosterViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var currentTab by remember { mutableIntStateOf(0) }

    val games by viewModel.games.collectAsState()
    val metrics by viewModel.systemMetrics.collectAsState()
    val preferences by viewModel.preferences.collectAsState()
    val isBoosting by viewModel.isBoosting.collectAsState()
    val gameBeingLaunched by viewModel.gameBeingLaunched.collectAsState()
    val launchProgressText by viewModel.launchProgressText.collectAsState()
    val installedApps by viewModel.installedApps.collectAsState()
    val boostHistory by viewModel.boostHistory.collectAsState()
    val livePing by viewModel.livePing.collectAsState()

    // Listen to VM snackbar messages
    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("main_app_scaffold"),
        containerColor = DarLinkDarkBg,
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.navigationBarsPadding()
            )
        },
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .testTag("xarena_top_bar"),
                color = DarLinkSurface,
                border = BorderStroke(1.dp, DarLinkCyan.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarLinkCyan.copy(alpha = 0.15f))
                                .clickable { viewModel.triggerOneTapBoost() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = "Dar-Link Logo",
                                tint = DarLinkCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "XARENA",
                                    color = TextWhite,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "DAR-LINK 3.0",
                                    color = DarLinkOrange,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Text(
                                text = "ইনফিনিক্স আল্ট্রা গেম বুস্টার",
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }

                    // Live RAM Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = DarLinkCard,
                        border = BorderStroke(1.dp, DarLinkCyan.copy(alpha = 0.35f)),
                        modifier = Modifier.clickable { viewModel.triggerOneTapBoost() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(DarLinkGreen, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "RAM: ${metrics.ramUsagePercent}%",
                                color = TextWhite,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("main_bottom_nav"),
                containerColor = DarLinkSurface,
                tonalElevation = 8.dp
            ) {
                // Tab 0: XArena Space
                NavigationBarItem(
                    selected = currentTab == 0,
                    onClick = { currentTab = 0 },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == 0) Icons.Filled.SportsEsports else Icons.Outlined.SportsEsports,
                            contentDescription = "Game Space",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "গেম স্পেস",
                            fontSize = 10.sp,
                            fontWeight = if (currentTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarLinkDarkBg,
                        selectedTextColor = DarLinkCyan,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted,
                        indicatorColor = DarLinkCyan
                    ),
                    modifier = Modifier.testTag("nav_tab_space")
                )

                // Tab 1: Dar-Link Engine
                NavigationBarItem(
                    selected = currentTab == 1,
                    onClick = { currentTab = 1 },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == 1) Icons.Filled.Bolt else Icons.Outlined.Bolt,
                            contentDescription = "Dar-Link Engine",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "দার-লিংক ৩.০",
                            fontSize = 10.sp,
                            fontWeight = if (currentTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TextWhite,
                        selectedTextColor = DarLinkOrange,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted,
                        indicatorColor = DarLinkOrange
                    ),
                    modifier = Modifier.testTag("nav_tab_engine")
                )

                // Tab 2: Gaming Assistant / Toolkit
                NavigationBarItem(
                    selected = currentTab == 2,
                    onClick = { currentTab = 2 },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == 2) Icons.Filled.Security else Icons.Outlined.Security,
                            contentDescription = "Assistant",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "টুলকিট",
                            fontSize = 10.sp,
                            fontWeight = if (currentTab == 2) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TextWhite,
                        selectedTextColor = DarLinkPurple,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted,
                        indicatorColor = DarLinkPurple
                    ),
                    modifier = Modifier.testTag("nav_tab_assistant")
                )

                // Tab 3: Monitor & Ping
                NavigationBarItem(
                    selected = currentTab == 3,
                    onClick = { currentTab = 3 },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == 3) Icons.Filled.Speed else Icons.Outlined.Speed,
                            contentDescription = "Monitor & Ping",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "মনিটর ও পিং",
                            fontSize = 10.sp,
                            fontWeight = if (currentTab == 3) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarLinkDarkBg,
                        selectedTextColor = DarLinkGreen,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted,
                        indicatorColor = DarLinkGreen
                    ),
                    modifier = Modifier.testTag("nav_tab_monitor")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "booster_tab_crossfade"
            ) { tabIndex ->
                when (tabIndex) {
                    0 -> XArenaSpaceScreen(
                        games = games,
                        metrics = metrics,
                        isBoosting = isBoosting,
                        installedApps = installedApps,
                        onTriggerBoost = { viewModel.triggerOneTapBoost() },
                        onLaunchGame = { game -> viewModel.launchGame(game) },
                        onUpdateGame = { game -> viewModel.updateGame(game) },
                        onDeleteGame = { id, title -> viewModel.deleteGame(id, title) },
                        onAddGame = { title, pkg, genre -> viewModel.addGame(title, pkg, genre) },
                        onRefreshMetrics = { viewModel.refreshMetrics() }
                    )
                    1 -> DarLinkEngineScreen(
                        preferences = preferences,
                        onUpdatePreferences = { viewModel.updatePreferences(it) }
                    )
                    2 -> GamingAssistantScreen(
                        preferences = preferences,
                        onUpdatePreferences = { viewModel.updatePreferences(it) }
                    )
                    3 -> FpsNetworkScreen(
                        metrics = metrics,
                        livePing = livePing,
                        boostHistory = boostHistory,
                        onRunPingTest = { viewModel.runPingTest() },
                        onClearHistory = { viewModel.clearBoostHistory() }
                    )
                }
            }

            // Launch Overlay Modal
            gameBeingLaunched?.let { game ->
                LaunchOverlayDialog(
                    game = game,
                    progressText = launchProgressText,
                    onDismiss = { viewModel.dismissLaunchOverlay() }
                )
            }
        }
    }
}
