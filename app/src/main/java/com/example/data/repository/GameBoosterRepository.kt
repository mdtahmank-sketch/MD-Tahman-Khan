package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.entity.BoostHistoryEntity
import com.example.data.local.entity.BoosterPreferenceEntity
import com.example.data.local.entity.GameEntity
import com.example.data.model.GraphicEnhancement
import com.example.data.model.InstalledAppInfo
import com.example.data.model.PerformanceMode
import com.example.data.model.SystemMetrics
import com.example.data.system.SystemMonitorHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GameBoosterRepository(
    private val database: AppDatabase,
    private val systemHelper: SystemMonitorHelper
) {
    private val gameDao = database.gameDao()
    private val boostHistoryDao = database.boostHistoryDao()
    private val preferenceDao = database.boosterPreferenceDao()

    val gamesFlow: Flow<List<GameEntity>> = gameDao.getAllGamesFlow()
    val boostHistoryFlow: Flow<List<BoostHistoryEntity>> = boostHistoryDao.getRecentHistoryFlow()
    val preferencesFlow: Flow<BoosterPreferenceEntity?> = preferenceDao.getPreferencesFlow()

    suspend fun initializeDefaultGamesIfNeeded() = withContext(Dispatchers.IO) {
        val count = gameDao.getGameCount()
        if (count == 0) {
            val defaultGames = listOf(
                GameEntity(
                    title = "Free Fire MAX",
                    packageName = "com.dts.freefiremax",
                    genre = "Battle Royale",
                    performanceMode = "BEAST",
                    targetFps = 90,
                    touchRateHz = 360,
                    dndEnabled = true,
                    networkBoostEnabled = true,
                    graphicMode = "HDR_VIVID",
                    playTimeMinutes = 145,
                    lastBoostTimestamp = System.currentTimeMillis() - 3600000L,
                    isCustom = false
                ),
                GameEntity(
                    title = "PUBG Mobile / BGMI",
                    packageName = "com.tencent.ig",
                    genre = "Battle Royale",
                    performanceMode = "BEAST",
                    targetFps = 90,
                    touchRateHz = 360,
                    dndEnabled = true,
                    networkBoostEnabled = true,
                    graphicMode = "SHADOW_BOOST",
                    playTimeMinutes = 85,
                    lastBoostTimestamp = System.currentTimeMillis() - 7200000L,
                    isCustom = false
                ),
                GameEntity(
                    title = "Call of Duty: Mobile",
                    packageName = "com.activision.callofduty.shooter",
                    genre = "Action FPS",
                    performanceMode = "BEAST",
                    targetFps = 120,
                    touchRateHz = 360,
                    dndEnabled = true,
                    networkBoostEnabled = true,
                    graphicMode = "HIGH_FPS_LOCK",
                    playTimeMinutes = 60,
                    lastBoostTimestamp = System.currentTimeMillis() - 86400000L,
                    isCustom = false
                ),
                GameEntity(
                    title = "Mobile Legends: Bang Bang",
                    packageName = "com.mobile.legends",
                    genre = "MOBA 5v5",
                    performanceMode = "BALANCED",
                    targetFps = 90,
                    touchRateHz = 240,
                    dndEnabled = true,
                    networkBoostEnabled = true,
                    graphicMode = "HDR_VIVID",
                    playTimeMinutes = 35,
                    lastBoostTimestamp = 0L,
                    isCustom = false
                ),
                GameEntity(
                    title = "Genshin Impact",
                    packageName = "com.miHoYo.GenshinImpact",
                    genre = "Open World RPG",
                    performanceMode = "BEAST",
                    targetFps = 60,
                    touchRateHz = 240,
                    dndEnabled = true,
                    networkBoostEnabled = true,
                    graphicMode = "HDR_VIVID",
                    playTimeMinutes = 120,
                    lastBoostTimestamp = 0L,
                    isCustom = false
                ),
                GameEntity(
                    title = "Asphalt 9: Legends",
                    packageName = "com.gameloft.android.ANMP.GloftA9HM",
                    genre = "High-Speed Racing",
                    performanceMode = "BALANCED",
                    targetFps = 60,
                    touchRateHz = 240,
                    dndEnabled = true,
                    networkBoostEnabled = true,
                    graphicMode = "STANDARD",
                    playTimeMinutes = 25,
                    lastBoostTimestamp = 0L,
                    isCustom = false
                )
            )
            gameDao.insertGames(defaultGames)
        }

        val pref = preferenceDao.getPreferencesDirect()
        if (pref == null) {
            preferenceDao.savePreferences(BoosterPreferenceEntity())
        }
    }

    fun getSystemMetrics(lastFreedRamMb: Int = 0): SystemMetrics {
        return systemHelper.getSystemMetrics(lastFreedRamMb)
    }

    suspend fun performOneTapBoost(): BoostHistoryEntity = withContext(Dispatchers.IO) {
        val freedMb = systemHelper.performMemoryCleanup()
        val ping = systemHelper.testLivePing()
        val metrics = systemHelper.getSystemMetrics(freedMb)

        val historyItem = BoostHistoryEntity(
            timestamp = System.currentTimeMillis(),
            gameTitle = "All Games System Boost",
            ramFreedMb = freedMb,
            cpuFreqBoost = "2.4 GHz Peak",
            tempCelsius = metrics.batteryTempCelsius,
            pingMs = ping
        )
        boostHistoryDao.insertHistory(historyItem)
        historyItem
    }

    suspend fun boostAndLaunchGame(game: GameEntity): Boolean = withContext(Dispatchers.IO) {
        val freedMb = systemHelper.performMemoryCleanup()
        val ping = systemHelper.testLivePing()
        val metrics = systemHelper.getSystemMetrics(freedMb)

        boostHistoryDao.insertHistory(
            BoostHistoryEntity(
                timestamp = System.currentTimeMillis(),
                gameTitle = game.title,
                ramFreedMb = freedMb,
                cpuFreqBoost = if (game.performanceMode == "BEAST") "2.8 GHz Turbo" else "2.4 GHz",
                tempCelsius = metrics.batteryTempCelsius,
                pingMs = ping
            )
        )

        gameDao.recordGameBoost(game.id, System.currentTimeMillis(), 15)

        // Launch real app if installed
        systemHelper.launchGame(game.packageName)
    }

    fun isAppInstalled(packageName: String): Boolean {
        return systemHelper.isAppInstalled(packageName)
    }

    suspend fun getInstalledDeviceApps(): List<InstalledAppInfo> {
        return systemHelper.getInstalledUserApps()
    }

    suspend fun addCustomGame(title: String, packageName: String, genre: String = "Game") = withContext(Dispatchers.IO) {
        gameDao.insertGame(
            GameEntity(
                title = title,
                packageName = packageName,
                genre = genre,
                performanceMode = "BEAST",
                targetFps = 90,
                touchRateHz = 360,
                dndEnabled = true,
                networkBoostEnabled = true,
                graphicMode = "HDR_VIVID",
                isCustom = true
            )
        )
    }

    suspend fun updateGameSettings(game: GameEntity) = withContext(Dispatchers.IO) {
        gameDao.updateGame(game)
    }

    suspend fun deleteGame(id: Long) = withContext(Dispatchers.IO) {
        gameDao.deleteGame(id)
    }

    suspend fun updatePreferences(pref: BoosterPreferenceEntity) = withContext(Dispatchers.IO) {
        preferenceDao.savePreferences(pref)
    }

    suspend fun testLivePing(): Int {
        return systemHelper.testLivePing()
    }

    suspend fun clearBoostHistory() = withContext(Dispatchers.IO) {
        boostHistoryDao.clearHistory()
    }
}
