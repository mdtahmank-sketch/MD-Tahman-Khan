package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_wallet")
data class UserWalletEntity(
    @PrimaryKey val id: Int = 1,
    val coins: Int = 20, // Starter bonus coins
    val lastClaimTimestamp: Long = 0L,
    val streakDays: Int = 0,
    val lastSpinTimestamp: Long = 0L,
    val lastQuizTimestamp: Long = 0L,
    val lastAimPracticeTimestamp: Long = 0L,
    val totalCoinsEarned: Int = 20
)

@Entity(tableName = "unlocked_presets")
data class UnlockedPresetEntity(
    @PrimaryKey val presetId: String,
    val unlockedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "custom_presets")
data class CustomPresetEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val deviceInfo: String,
    val general: Int,
    val redDot: Int,
    val scope2x: Int,
    val scope4x: Int,
    val sniperScope: Int,
    val freeLook: Int,
    val fireButtonSize: Int,
    val dpi: Int,
    val createdAt: Long = System.currentTimeMillis()
)
