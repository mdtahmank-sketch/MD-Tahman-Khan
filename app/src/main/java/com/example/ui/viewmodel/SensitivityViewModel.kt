package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.CustomPresetEntity
import com.example.data.local.entity.UserWalletEntity
import com.example.data.model.PresetCategory
import com.example.data.model.SensitivityData
import com.example.data.model.SensitivityPreset
import com.example.data.repository.DailyRewardClaimResult
import com.example.data.repository.SensitivityRepository
import com.example.data.repository.UnlockResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

data class QuizQuestion(
    val questionBn: String,
    val optionsBn: List<String>,
    val correctIndex: Int,
    val explanationBn: String
)

class SensitivityViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SensitivityRepository

    val wallet: StateFlow<UserWalletEntity>
    val unlockedPresetIds: StateFlow<Set<String>>
    val customPresets: StateFlow<List<CustomPresetEntity>>

    private val _selectedCategory = MutableStateFlow(PresetCategory.ALL)
    val selectedCategory: StateFlow<PresetCategory> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _messageEvents = MutableSharedFlow<String>()
    val messageEvents: SharedFlow<String> = _messageEvents.asSharedFlow()

    private val _isSpinning = MutableStateFlow(false)
    val isSpinning: StateFlow<Boolean> = _isSpinning.asStateFlow()

    // Current Quiz question for earning coins
    private val quizList = listOf(
        QuizQuestion(
            questionBn = "ফ্রি ফায়ারে ওয়ান-ট্যাপ হেডশটের জন্য কোন বাটন ড্র্যাগ সবচেয়ে কার্যকর?",
            optionsBn = listOf("সোজা নিচে ড্র্যাগ", "J-ড্র্যাগ বা রোটেশন ড্র্যাগ", "হালকা ডানে স্থির রাখা", "শুধু ক্রাউচ বাটন"),
            correctIndex = 1,
            explanationBn = "J-ড্র্যাগ এবং রোটেশন ড্র্যাগ এনিমির মাথার উচ্চতায় এইম নিখুঁতভাবে লক করে।"
        ),
        QuizQuestion(
            questionBn = "ক্লোজ রেঞ্জে শটগান দিয়ে হেডশটের জন্য সাধারণ (General) সেন্সিটিভিটি কত রাখা ভালো?",
            optionsBn = listOf("৫০ এর নিচে", "৬৫ - ৭৫", "৯৫ - ১০০", "২০ - ৩০"),
            correctIndex = 2,
            explanationBn = "শটগান ক্লোজ রেঞ্জে ১০০ বা ৯৫+ সেন্সিটিভিটি দ্রুততম ফ্লিক এবং তাৎক্ষণিক হেডশট এনে দেয়।"
        ),
        QuizQuestion(
            questionBn = "ফায়ার বাটনের সাইজ ছোট (৪০-৪৫%) রাখলে কী সুবিধা পাওয়া যায়?",
            optionsBn = listOf("ড্র্যাগ স্পেস বৃদ্ধি পায় ও দ্রুত ফ্লিক হয়", "বুলেট বেশি ড্যামেজ দেয়", "মুভমেন্ট স্লো হয়ে যায়", "রিকয়েল বেড়ে যায়"),
            correctIndex = 0,
            explanationBn = "ফায়ার বাটন ছোট রাখলে আঙুল সোজা উপরে টেনে তোলার জন্য স্ক্রিনে পর্যাপ্ত জায়গা তৈরি হয়।"
        ),
        QuizQuestion(
            questionBn = "AR গানের লং রেঞ্জে লেজার এইমের জন্য কোন স্কোপ সেন্সিটিভিটি গুরুত্বপূর্ণ?",
            optionsBn = listOf("ফ্রি লুক", "৪X স্কোপ ও ২X স্কোপ", "শুধু স্নাইপার স্কোপ", "রেড ডট ০ রাখা"),
            correctIndex = 1,
            explanationBn = "৪X এবং ২X স্কোপ ৮০-৮৮ এর মধ্যে রাখলে দূর থেকে স্প্রে করার সময় বুলেট চারপাশে ছড়িয়ে যায় না।"
        )
    )

    private val _currentQuizIndex = MutableStateFlow(0)
    val currentQuizIndex: StateFlow<Int> = _currentQuizIndex.asStateFlow()
    val currentQuizQuestion: StateFlow<QuizQuestion> = _currentQuizIndex.map { quizList[it % quizList.size] }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), quizList[0])

    init {
        val db = AppDatabase.getDatabase(application)
        repository = SensitivityRepository(
            walletDao = db.walletDao(),
            unlockedPresetDao = db.unlockedPresetDao(),
            customPresetDao = db.customPresetDao()
        )

        wallet = repository.walletFlow
            .map { it ?: UserWalletEntity(id = 1, coins = 20, streakDays = 0) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserWalletEntity())

        unlockedPresetIds = repository.unlockedPresetIdsFlow
            .map { it.toSet() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

        customPresets = repository.customPresetsFlow
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        viewModelScope.launch {
            repository.getOrCreateWallet()
        }
    }

    fun setCategory(category: PresetCategory) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun claimDailyReward(onResult: (DailyRewardClaimResult) -> Unit) {
        viewModelScope.launch {
            val result = repository.claimDailyReward()
            onResult(result)
            _messageEvents.emit(result.message)
        }
    }

    fun isDailyRewardAvailable(): Boolean {
        return repository.isDailyRewardAvailable(wallet.value)
    }

    fun unlockPreset(preset: SensitivityPreset, onResult: (UnlockResult) -> Unit) {
        viewModelScope.launch {
            val result = repository.unlockPreset(preset.id, preset.costCoins)
            when (result) {
                is UnlockResult.Success -> {
                    _messageEvents.emit("🎉 অভিনন্দন! '${preset.titleBn}' সফলভাবে আনলক হয়েছে (৫০ কয়েন খরচ)!")
                }
                is UnlockResult.InsufficientCoins -> {
                    _messageEvents.emit("⚠️ পর্যাপ্ত কয়েন নেই! আপনার আরও ${result.neededCoins - result.currentCoins} কয়েন প্রয়োজন।")
                }
                is UnlockResult.AlreadyUnlocked -> {
                    _messageEvents.emit("এই প্রিসেটটি ইতিমধ্যে আনলক করা রয়েছে।")
                }
            }
            onResult(result)
        }
    }

    fun spinWheel(onSpinComplete: (Int) -> Unit) {
        if (_isSpinning.value) return
        viewModelScope.launch {
            _isSpinning.value = true
            // Possible coins prizes: 10, 15, 20, 25, 30, 40, 50
            val possiblePrizes = listOf(10, 15, 20, 25, 30, 35, 50)
            val wonPrize = possiblePrizes[Random.nextInt(possiblePrizes.size)]
            
            // Wait brief moment for realistic wheel effect
            kotlinx.coroutines.delay(1800)
            repository.spinLuckyWheel(wonPrize)
            _isSpinning.value = false
            _messageEvents.emit("🎁 লাকি স্পিন থেকে আপনি $wonPrize কয়েন জিতেছেন!")
            onSpinComplete(wonPrize)
        }
    }

    fun submitQuizAnswer(selectedIndex: Int, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val q = currentQuizQuestion.value
            if (selectedIndex == q.correctIndex) {
                repository.addQuizReward(10)
                _currentQuizIndex.value = (_currentQuizIndex.value + 1) % quizList.size
                _messageEvents.emit("🎯 সঠিক উত্তর! +১০ কয়েন আপনার ব্যালেন্সে যোগ হয়েছে!")
                onResult(true, "সঠিক উত্তর! +১০ কয়েন অর্জিত।")
            } else {
                onResult(false, "ভুল উত্তর! আবার চেষ্টা করুন। সঠিক উত্তর ছিল: ${q.optionsBn[q.correctIndex]}")
            }
        }
    }

    fun recordAimPracticeHeadshot(onBonusAwarded: (Int) -> Unit) {
        viewModelScope.launch {
            val bonus = 10
            repository.addAimPracticeReward(bonus)
            _messageEvents.emit("🔥 অসাধারণ হেডশট প্র্যাকটিস! +১০ কয়েন অর্জিত!")
            onBonusAwarded(bonus)
        }
    }

    fun saveCustomPreset(
        name: String,
        deviceInfo: String,
        general: Int,
        redDot: Int,
        scope2x: Int,
        scope4x: Int,
        sniperScope: Int,
        freeLook: Int,
        buttonSize: Int,
        dpi: Int
    ) {
        viewModelScope.launch {
            val entity = CustomPresetEntity(
                name = name.ifBlank { "মাই হেডশট সেটিংস" },
                deviceInfo = deviceInfo.ifBlank { "কাস্টম ডিভাইস" },
                general = general,
                redDot = redDot,
                scope2x = scope2x,
                scope4x = scope4x,
                sniperScope = sniperScope,
                freeLook = freeLook,
                fireButtonSize = buttonSize,
                dpi = dpi
            )
            repository.saveCustomPreset(entity)
            _messageEvents.emit("✅ আপনার কাস্টম সেন্সিটিভিটি সফলভাবে সেভ করা হয়েছে!")
        }
    }

    fun deleteCustomPreset(id: Int) {
        viewModelScope.launch {
            repository.deleteCustomPreset(id)
            _messageEvents.emit("কাস্টম প্রিসেট মুছে ফেলা হয়েছে।")
        }
    }
}
