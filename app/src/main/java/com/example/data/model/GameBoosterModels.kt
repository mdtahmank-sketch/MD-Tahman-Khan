package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.DarLinkCyan
import com.example.ui.theme.DarLinkGreen
import com.example.ui.theme.DarLinkOrange
import com.example.ui.theme.DarLinkPurple

enum class PerformanceMode(
    val titleBn: String,
    val titleEn: String,
    val subtitleBn: String,
    val color: Color
) {
    BEAST(
        titleBn = "বিস্ট মোড (Beast/Esports)",
        titleEn = "Ultra Performance",
        subtitleBn = "সর্বোচ্চ CPU ও GPU ক্লক স্পিড, টাচ পোলিং ৩৬০Hz এবং সর্বোচ্চ ফ্রেমরেট",
        color = DarLinkOrange
    ),
    BALANCED(
        titleBn = "ব্যালান্সড মোড (Balanced)",
        titleEn = "Adaptive Gaming",
        subtitleBn = "স্থিতিশীল এফপিএস ও নিয়ন্ত্রিত তাপমাত্রা বজায় রেখে মসৃণ গেমিং",
        color = DarLinkCyan
    ),
    BATTERY_SAVER(
        titleBn = "ব্যাটারি সেভার (Power Saver)",
        titleEn = "Long Endurance",
        subtitleBn = "ব্যাটারি খরচ কমায় এবং দীর্ঘক্ষণ গেম খেলায় ডিভাইস ঠান্ডা রাখে",
        color = DarLinkGreen
    )
}

enum class GraphicEnhancement(
    val labelBn: String,
    val labelEn: String,
    val descriptionBn: String
) {
    HDR_VIVID("এইচডিআর কালার", "HDR Vivid", "উজ্জ্বল কালার স্যাচুরেশন ও স্পষ্ট শত্রু শনাক্তকরণ"),
    SHADOW_BOOST("শ্যাডো বুস্ট", "Shadow Boost", "অন্ধকার ঘরের শত্রু স্পষ্ট দেখার জন্য নাইট ভিশন বুস্ট"),
    HIGH_FPS_LOCK("এফপিএস লক", "90/120 FPS Lock", "ফ্রেম ড্রপ প্রতিরোধে মসৃণ ফ্রেম লক"),
    STANDARD("স্ট্যান্ডার্ড", "Standard", "ডিফল্ট সিস্টেম কালার ও কন্ট্রাস্ট")
}

data class SystemMetrics(
    val ramUsedBytes: Long = 0L,
    val ramTotalBytes: Long = 0L,
    val ramUsagePercent: Int = 0,
    val ramFreedLastMb: Int = 0,
    val cpuLoadPercent: Int = 38,
    val cpuFrequencyGhz: Float = 2.4f,
    val gpuLoadPercent: Int = 42,
    val batteryPercent: Int = 85,
    val batteryTempCelsius: Float = 34.8f,
    val isCharging: Boolean = false,
    val networkPingMs: Int = 28,
    val networkType: String = "Wi-Fi 5GHz",
    val fpsEstimate: Int = 90
)

data class GameItem(
    val id: Long = 0L,
    val title: String,
    val packageName: String,
    val genre: String,
    val performanceMode: PerformanceMode = PerformanceMode.BEAST,
    val targetFps: Int = 90,
    val touchRateHz: Int = 360,
    val dndEnabled: Boolean = true,
    val networkBoostEnabled: Boolean = true,
    val graphicEnhancement: GraphicEnhancement = GraphicEnhancement.HDR_VIVID,
    val playTimeMinutes: Int = 0,
    val lastBoostTimestamp: Long = 0L,
    val isInstalled: Boolean = false,
    val isCustom: Boolean = false
)

data class InstalledAppInfo(
    val appName: String,
    val packageName: String,
    val isGame: Boolean
)

data class CrosshairSettings(
    val isEnabled: Boolean = false,
    val colorHex: String = "CYAN",
    val shape: String = "CROSS", // CROSS, DOT, CIRCLE, T_SHAPE
    val sizeDp: Int = 24
)

data class VoiceChangerItem(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val pitchMultiplier: Float,
    val soundSampleText: String
)
