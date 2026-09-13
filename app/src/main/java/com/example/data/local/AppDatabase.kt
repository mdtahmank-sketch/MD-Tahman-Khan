package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.BoostHistoryDao
import com.example.data.local.dao.BoosterPreferenceDao
import com.example.data.local.dao.CustomPresetDao
import com.example.data.local.dao.GameDao
import com.example.data.local.dao.UnlockedPresetDao
import com.example.data.local.dao.WalletDao
import com.example.data.local.entity.BoostHistoryEntity
import com.example.data.local.entity.BoosterPreferenceEntity
import com.example.data.local.entity.CustomPresetEntity
import com.example.data.local.entity.GameEntity
import com.example.data.local.entity.UnlockedPresetEntity
import com.example.data.local.entity.UserWalletEntity

@Database(
    entities = [
        UserWalletEntity::class,
        UnlockedPresetEntity::class,
        CustomPresetEntity::class,
        GameEntity::class,
        BoostHistoryEntity::class,
        BoosterPreferenceEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun walletDao(): WalletDao
    abstract fun unlockedPresetDao(): UnlockedPresetDao
    abstract fun customPresetDao(): CustomPresetDao
    abstract fun gameDao(): GameDao
    abstract fun boostHistoryDao(): BoostHistoryDao
    abstract fun boosterPreferenceDao(): BoosterPreferenceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "darlink_xarena_booster_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
