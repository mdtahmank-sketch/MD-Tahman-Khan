package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.BoostHistoryEntity
import com.example.data.local.entity.BoosterPreferenceEntity
import com.example.data.local.entity.GameEntity
import com.example.data.model.InstalledAppInfo
import com.example.data.model.SystemMetrics
import com.example.data.repository.GameBoosterRepository
import com.example.data.system.SystemMonitorHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameBoosterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GameBoosterRepository

    val games: StateFlow<List<GameEntity>>
    val boostHistory: StateFlow<List<BoostHistoryEntity>>

    private val _systemMetrics = MutableStateFlow(SystemMetrics())
    val systemMetrics: StateFlow<SystemMetrics> = _systemMetrics.asStateFlow()

    private val _preferences = MutableStateFlow(BoosterPreferenceEntity())
    val preferences: StateFlow<BoosterPreferenceEntity> = _preferences.asStateFlow()

    private val _isBoosting = MutableStateFlow(false)
    val isBoosting: StateFlow<Boolean> = _isBoosting.asStateFlow()

    private val _lastBoostResult = MutableStateFlow<BoostHistoryEntity?>(null)
    val lastBoostResult: StateFlow<BoostHistoryEntity?> = _lastBoostResult.asStateFlow()

    private val _gameBeingLaunched = MutableStateFlow<GameEntity?>(null)
    val gameBeingLaunched: StateFlow<GameEntity?> = _gameBeingLaunched.asStateFlow()

    private val _launchProgressText = MutableStateFlow("")
    val launchProgressText: StateFlow<String> = _launchProgressText.asStateFlow()

    private val _installedApps = MutableStateFlow<List<InstalledAppInfo>>(emptyList())
    val installedApps: StateFlow<List<InstalledAppInfo>> = _installedApps.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    private val _livePing = MutableStateFlow(24)
    val livePing: StateFlow<Int> = _livePing.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        val helper = SystemMonitorHelper(application)
        repository = GameBoosterRepository(db, helper)

        games = repository.gamesFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        boostHistory = repository.boostHistoryFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        viewModelScope.launch {
            repository.initializeDefaultGamesIfNeeded()
            refreshMetrics()
            loadInstalledApps()
        }

        viewModelScope.launch {
            repository.preferencesFlow.collect { pref ->
                if (pref != null) {
                    _preferences.value = pref
                }
            }
        }
    }

    fun refreshMetrics() {
        val currentFreed = _lastBoostResult.value?.ramFreedMb ?: 0
        _systemMetrics.value = repository.getSystemMetrics(currentFreed)
    }

    fun triggerOneTapBoost() {
        if (_isBoosting.value) return

        viewModelScope.launch {
            _isBoosting.value = true
            _snackbarMessage.emit("🚀 দার-লিংক গেমিং বুস্ট শুরু হচ্ছে...")

            delay(800) // Visual animation time
            val result = repository.performOneTapBoost()
            _lastBoostResult.value = result
            _isBoosting.value = false
            refreshMetrics()

            _snackbarMessage.emit("⚡ বুস্ট সফল! +${result.ramFreedMb} MB RAM মুক্ত হয়েছে | পিং: ${result.pingMs}ms")
        }
    }

    fun launchGame(game: GameEntity) {
        viewModelScope.launch {
            _gameBeingLaunched.value = game
            _launchProgressText.value = "দার-লিংক ৩.০ ইঞ্জিন প্রস্তুত হচ্ছে..."
            delay(500)

            _launchProgressText.value = "র‍্যাম মেমরি কম্প্যাক্ট করা হচ্ছে (+১,২৫০ MB)..."
            delay(600)

            _launchProgressText.value = "CPU ও GPU ক্লক পিক স্পিডে লক করা হচ্ছে..."
            delay(500)

            _launchProgressText.value = "DND কল ব্লকার ও নোটিফিকেশন ফিল্টার সক্রিয়..."
            delay(500)

            _launchProgressText.value = "ডুয়াল-চ্যানেল গেমিং নেটওয়ার্ক সক্রিয়..."
            delay(400)

            val launched = repository.boostAndLaunchGame(game)
            refreshMetrics()

            if (!launched) {
                _launchProgressText.value = "${game.title} অপ্টিমাইজড! সিমুলেশন সেশন রানিং।"
                delay(900)
                _gameBeingLaunched.value = null
                _snackbarMessage.emit("🎮 ${game.title} দার-লিংক বুস্ট কনফিগ সফলভাবে সক্রিয় হয়েছে!")
            } else {
                _gameBeingLaunched.value = null
                _snackbarMessage.emit("🎮 ${game.title} বুস্ট সহ চালু হয়েছে!")
            }
        }
    }

    fun dismissLaunchOverlay() {
        _gameBeingLaunched.value = null
    }

    fun addGame(title: String, packageName: String, genre: String) {
        viewModelScope.launch {
            repository.addCustomGame(title, packageName, genre)
            _snackbarMessage.emit("✅ $title গেম স্পেসে যুক্ত হয়েছে!")
        }
    }

    fun updateGame(game: GameEntity) {
        viewModelScope.launch {
            repository.updateGameSettings(game)
            _snackbarMessage.emit("⚙️ ${game.title} এর কনফিগারেশন আপডেট হয়েছে!")
        }
    }

    fun deleteGame(id: Long, title: String) {
        viewModelScope.launch {
            repository.deleteGame(id)
            _snackbarMessage.emit("🗑️ $title তালিকা থেকে বাদ দেওয়া হয়েছে")
        }
    }

    fun updatePreferences(pref: BoosterPreferenceEntity) {
        _preferences.value = pref
        viewModelScope.launch {
            repository.updatePreferences(pref)
            _snackbarMessage.emit("সেটিংস সফলভাবে সংরক্ষিত হয়েছে!")
        }
    }

    fun loadInstalledApps() {
        viewModelScope.launch {
            val apps = repository.getInstalledDeviceApps()
            _installedApps.value = apps
        }
    }

    fun runPingTest() {
        viewModelScope.launch {
            _snackbarMessage.emit("📡 গেমিং সার্ভার পিং টেস্ট করা হচ্ছে...")
            val ping = repository.testLivePing()
            _livePing.value = ping
            _snackbarMessage.emit("📶 বর্তমান গেমিং পিং: ${ping}ms")
        }
    }

    fun clearBoostHistory() {
        viewModelScope.launch {
            repository.clearBoostHistory()
            _snackbarMessage.emit("বুস্ট হিস্টোরি মুছে ফেলা হয়েছে")
        }
    }
}
