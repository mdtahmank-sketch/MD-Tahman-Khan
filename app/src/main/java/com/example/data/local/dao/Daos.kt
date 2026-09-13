package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.CustomPresetEntity
import com.example.data.local.entity.UnlockedPresetEntity
import com.example.data.local.entity.UserWalletEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {
    @Query("SELECT * FROM user_wallet WHERE id = 1 LIMIT 1")
    fun getWalletFlow(): Flow<UserWalletEntity?>

    @Query("SELECT * FROM user_wallet WHERE id = 1 LIMIT 1")
    suspend fun getWalletDirect(): UserWalletEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallet(wallet: UserWalletEntity)

    @Update
    suspend fun updateWallet(wallet: UserWalletEntity)
}

@Dao
interface UnlockedPresetDao {
    @Query("SELECT presetId FROM unlocked_presets")
    fun getAllUnlockedPresetIdsFlow(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun unlockPreset(entity: UnlockedPresetEntity)

    @Query("SELECT COUNT(*) FROM unlocked_presets WHERE presetId = :presetId")
    suspend fun isPresetUnlocked(presetId: String): Int
}

@Dao
interface CustomPresetDao {
    @Query("SELECT * FROM custom_presets ORDER BY createdAt DESC")
    fun getAllCustomPresetsFlow(): Flow<List<CustomPresetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomPreset(preset: CustomPresetEntity): Long

    @Query("DELETE FROM custom_presets WHERE id = :id")
    suspend fun deleteCustomPreset(id: Int)
}
