package com.example.data.repository

import com.example.data.local.dao.CustomPresetDao
import com.example.data.local.dao.UnlockedPresetDao
import com.example.data.local.dao.WalletDao
import com.example.data.local.entity.CustomPresetEntity
import com.example.data.local.entity.UnlockedPresetEntity
import com.example.data.local.entity.UserWalletEntity
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import java.util.concurrent.TimeUnit

sealed class UnlockResult {
    object Success : UnlockResult()
    object AlreadyUnlocked : UnlockResult()
    data class InsufficientCoins(val currentCoins: Int, val neededCoins: Int) : UnlockResult()
}

data class DailyRewardClaimResult(
    val success: Boolean,
    val coinsEarned: Int,
    val newStreak: Int,
    val message: String
)

class SensitivityRepository(
    private val walletDao: WalletDao,
    private val unlockedPresetDao: UnlockedPresetDao,
    private val customPresetDao: CustomPresetDao
) {
    val walletFlow: Flow<UserWalletEntity?> = walletDao.getWalletFlow()
    val unlockedPresetIdsFlow: Flow<List<String>> = unlockedPresetDao.getAllUnlockedPresetIdsFlow()
    val customPresetsFlow: Flow<List<CustomPresetEntity>> = customPresetDao.getAllCustomPresetsFlow()

    suspend fun getOrCreateWallet(): UserWalletEntity {
        var wallet = walletDao.getWalletDirect()
        if (wallet == null) {
            wallet = UserWalletEntity(id = 1, coins = 20, streakDays = 0)
            walletDao.insertWallet(wallet)
        }
        return wallet
    }

    private fun getEpochDay(timestamp: Long): Long {
        return TimeUnit.MILLISECONDS.toDays(timestamp)
    }

    fun isDailyRewardAvailable(wallet: UserWalletEntity?): Boolean {
        if (wallet == null || wallet.lastClaimTimestamp == 0L) return true
        val todayEpochDay = getEpochDay(System.currentTimeMillis())
        val lastClaimEpochDay = getEpochDay(wallet.lastClaimTimestamp)
        return todayEpochDay > lastClaimEpochDay
    }

    fun getStreakRewardCoins(dayStreak: Int): Int {
        return when (dayStreak) {
            1 -> 15
            2 -> 20
            3 -> 25
            4 -> 30
            5 -> 40
            6 -> 50
            7 -> 100
            else -> 20
        }
    }

    suspend fun claimDailyReward(): DailyRewardClaimResult {
        val wallet = getOrCreateWallet()
        val now = System.currentTimeMillis()
        val todayEpochDay = getEpochDay(now)
        val lastClaimEpochDay = getEpochDay(wallet.lastClaimTimestamp)

        if (wallet.lastClaimTimestamp != 0L && todayEpochDay == lastClaimEpochDay) {
            return DailyRewardClaimResult(
                success = false,
                coinsEarned = 0,
                newStreak = wallet.streakDays,
                message = "আজকের ডেইলি রিওয়ার্ড ইতিমধ্যে সংগ্রহ করা হয়েছে! আগামীকাল আবার আসুন।"
            )
        }

        val newStreak = if (lastClaimEpochDay == todayEpochDay - 1L) {
            // Consecutive day
            if (wallet.streakDays >= 7) 1 else wallet.streakDays + 1
        } else {
            // Streak broken or first day
            1
        }

        val rewardCoins = getStreakRewardCoins(newStreak)
        val updatedWallet = wallet.copy(
            coins = wallet.coins + rewardCoins,
            lastClaimTimestamp = now,
            streakDays = newStreak,
            totalCoinsEarned = wallet.totalCoinsEarned + rewardCoins
        )
        walletDao.updateWallet(updatedWallet)

        return DailyRewardClaimResult(
            success = true,
            coinsEarned = rewardCoins,
            newStreak = newStreak,
            message = "অভিনন্দন! আপনি $rewardCoins কয়েন রিওয়ার্ড পেয়েছেন! (দিন $newStreak)"
        )
    }

    suspend fun spinLuckyWheel(coinsWon: Int): Boolean {
        val wallet = getOrCreateWallet()
        val now = System.currentTimeMillis()
        val updated = wallet.copy(
            coins = wallet.coins + coinsWon,
            lastSpinTimestamp = now,
            totalCoinsEarned = wallet.totalCoinsEarned + coinsWon
        )
        walletDao.updateWallet(updated)
        return true
    }

    suspend fun addQuizReward(rewardCoins: Int = 10): Boolean {
        val wallet = getOrCreateWallet()
        val now = System.currentTimeMillis()
        val updated = wallet.copy(
            coins = wallet.coins + rewardCoins,
            lastQuizTimestamp = now,
            totalCoinsEarned = wallet.totalCoinsEarned + rewardCoins
        )
        walletDao.updateWallet(updated)
        return true
    }

    suspend fun addAimPracticeReward(rewardCoins: Int = 10): Boolean {
        val wallet = getOrCreateWallet()
        val now = System.currentTimeMillis()
        val updated = wallet.copy(
            coins = wallet.coins + rewardCoins,
            lastAimPracticeTimestamp = now,
            totalCoinsEarned = wallet.totalCoinsEarned + rewardCoins
        )
        walletDao.updateWallet(updated)
        return true
    }

    suspend fun unlockPreset(presetId: String, costCoins: Int = 50): UnlockResult {
        if (unlockedPresetDao.isPresetUnlocked(presetId) > 0) {
            return UnlockResult.AlreadyUnlocked
        }
        val wallet = getOrCreateWallet()
        if (wallet.coins < costCoins) {
            return UnlockResult.InsufficientCoins(currentCoins = wallet.coins, neededCoins = costCoins)
        }

        // Deduct coins and record unlock
        val updatedWallet = wallet.copy(coins = wallet.coins - costCoins)
        walletDao.updateWallet(updatedWallet)
        unlockedPresetDao.unlockPreset(UnlockedPresetEntity(presetId = presetId))
        return UnlockResult.Success
    }

    suspend fun saveCustomPreset(preset: CustomPresetEntity): Long {
        return customPresetDao.insertCustomPreset(preset)
    }

    suspend fun deleteCustomPreset(id: Int) {
        customPresetDao.deleteCustomPreset(id)
    }
}
