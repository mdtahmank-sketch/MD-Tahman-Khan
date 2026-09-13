package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.BoostHistoryEntity
import com.example.data.local.entity.BoosterPreferenceEntity
import com.example.data.local.entity.GameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM games ORDER BY lastBoostTimestamp DESC, id ASC")
    fun getAllGamesFlow(): Flow<List<GameEntity>>

    @Query("SELECT * FROM games ORDER BY lastBoostTimestamp DESC, id ASC")
    suspend fun getAllGamesDirect(): List<GameEntity>

    @Query("SELECT COUNT(*) FROM games")
    suspend fun getGameCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: GameEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<GameEntity>)

    @Update
    suspend fun updateGame(game: GameEntity)

    @Query("DELETE FROM games WHERE id = :id")
    suspend fun deleteGame(id: Long)

    @Query("UPDATE games SET lastBoostTimestamp = :timestamp, playTimeMinutes = playTimeMinutes + :additionalMinutes WHERE id = :id")
    suspend fun recordGameBoost(id: Long, timestamp: Long, additionalMinutes: Int)
}

@Dao
interface BoostHistoryDao {
    @Query("SELECT * FROM boost_history ORDER BY timestamp DESC LIMIT 20")
    fun getRecentHistoryFlow(): Flow<List<BoostHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(item: BoostHistoryEntity): Long

    @Query("DELETE FROM boost_history")
    suspend fun clearHistory()
}

@Dao
interface BoosterPreferenceDao {
    @Query("SELECT * FROM booster_preferences WHERE id = 1 LIMIT 1")
    fun getPreferencesFlow(): Flow<BoosterPreferenceEntity?>

    @Query("SELECT * FROM booster_preferences WHERE id = 1 LIMIT 1")
    suspend fun getPreferencesDirect(): BoosterPreferenceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePreferences(pref: BoosterPreferenceEntity)
}
