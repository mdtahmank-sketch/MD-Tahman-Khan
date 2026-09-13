package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val packageName: String,
    val genre: String,
    val performanceMode: String = "BEAST",
    val targetFps: Int = 90,
    val touchRateHz: Int = 360,
    val dndEnabled: Boolean = true,
    val networkBoostEnabled: Boolean = true,
    val graphicMode: String = "HDR_VIVID",
    val playTimeMinutes: Int = 0,
    val lastBoostTimestamp: Long = 0L,
    val isCustom: Boolean = false
)

@Entity(tableName = "boost_history")
data class BoostHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val timestamp: Long = System.currentTimeMillis(),
    val gameTitle: String,
    val ramFreedMb: Int,
    val cpuFreqBoost: String,
    val tempCelsius: Float,
    val pingMs: Int
)

@Entity(tableName = "booster_preferences")
data class BoosterPreferenceEntity(
    @PrimaryKey val id: Int = 1,
    val globalPerformanceMode: String = "BEAST",
    val floatingAssistantBar: Boolean = true,
    val antiMistouch: Boolean = true,
    val bypassCharging: Boolean = true,
    val networkDualBonding: Boolean = true,
    val touchSamplingHz: Int = 360,
    val crosshairEnabled: Boolean = false,
    val crosshairColor: String = "CYAN",
    val crosshairShape: String = "CROSS",
    val crosshairSize: Int = 24,
    val voiceChangerEffect: String = "NONE"
)
