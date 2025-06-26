package com.example.yaroslavhorach.profile

import androidx.lifecycle.viewModelScope
import com.example.yaroslavhorach.common.base.BaseViewModel
import com.example.yaroslavhorach.common.utill.isSameDay
import com.example.yaroslavhorach.domain.prefs.PrefsRepository
import com.example.yaroslavhorach.profile.model.CalendarDay
import com.example.yaroslavhorach.profile.model.ProfileAction
import com.example.yaroslavhorach.profile.model.ProfileUiMessage
import com.example.yaroslavhorach.profile.model.ProfileViewState
import com.example.yaroslavhorach.profile.model.SpeakingLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val prefsRepository: PrefsRepository) : BaseViewModel<ProfileViewState, ProfileAction, ProfileUiMessage>() {

    override val pendingActions: MutableSharedFlow<ProfileAction> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override val state: StateFlow<ProfileViewState> = combine(
        prefsRepository.getUserData(),
        uiMessageManager.message,
        ) { userData, messages ->
            ProfileViewState(
                avatarResId = userData.avatarResId ?: com.example.yaroslavhorach.designsystem.R.drawable.im_avatar_1,
                userName = userData.userName,
                activeDays = userData.activeDays.count(),
                experience = userData.experience,
                levelOfSpeaking = SpeakingLevel.fromExperience(userData.experience),
                activeDaysInRow = calculateActiveDaysInRowFromTimestamps(userData.activeDays.toSet()),
                lasActiveDays = getLastSevenDays(userData.activeDays),
                uiMessage = messages,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileViewState.Empty
        )

    init {
        pendingActions
            .onEach { event ->
                when (event) {
                    else -> {}
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getLastSevenDays(activeDays: List<Long>): List<CalendarDay> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        val weekDates = mutableListOf<Date>()
        for (i in 0 until 7) {
            weekDates.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return  weekDates
            .map { weekDay ->
                CalendarDay(
                    time = weekDay.time,
                    isActive = activeDays.any { activeDay -> activeDay.isSameDay(weekDay) })
            }
    }

    private fun calculateActiveDaysInRowFromTimestamps(timestamps: Set<Long>): Int {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val activeDates = timestamps
            .map { dateFormat.format(Date(it)) }
            .toSet()

        var count = 0
        val calendar = Calendar.getInstance()

        while (true) {
            val dateStr = dateFormat.format(calendar.time)

            if (activeDates.contains(dateStr)) {
                count++
                calendar.add(Calendar.DAY_OF_YEAR, -1)
            } else {
                break
            }
        }

        return count
    }
}
