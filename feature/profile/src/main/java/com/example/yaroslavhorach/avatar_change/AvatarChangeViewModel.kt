package com.example.yaroslavhorach.avatar_change

import androidx.lifecycle.viewModelScope
import com.example.yaroslavhorach.avatar_change.model.AvatarChangeAction
import com.example.yaroslavhorach.avatar_change.model.AvatarChangeUiMessage
import com.example.yaroslavhorach.avatar_change.model.AvatarChangeViewState
import com.example.yaroslavhorach.common.base.BaseViewModel
import com.example.yaroslavhorach.common.utill.isSameDay
import com.example.yaroslavhorach.domain.prefs.PrefsRepository
import com.example.yaroslavhorach.profile.R
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AvatarChangeViewModel @Inject constructor(private val prefsRepository: PrefsRepository) :
    BaseViewModel<AvatarChangeViewState, AvatarChangeAction, AvatarChangeUiMessage>() {

    override val pendingActions: MutableSharedFlow<AvatarChangeAction> =
        MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override val state: StateFlow<AvatarChangeViewState> = combine(
        prefsRepository.getUserData(),
        flowOf(prefsRepository.getAvatars()),
        uiMessageManager.message,
    ) { userData, avatars, messages ->
        AvatarChangeViewState(
            isPremiumUser = userData.isPremium,
            avatarResId = userData.avatarResId ?: com.example.yaroslavhorach.designsystem.R.drawable.im_avatar_1,
            userName = userData.userName,
            avatars = avatars,
            uiMessage = messages,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AvatarChangeViewState.Empty
    )

    init {
        pendingActions
            .onEach { event ->
                when (event) {
                    is AvatarChangeAction.OnAvatarChosen -> {
                        prefsRepository.changeAvatar(event.resId)
                    }
                    is AvatarChangeAction.OnNameTyped -> {
                        prefsRepository.changeName(name = event.name)
                    }
                    else -> {}
                }
            }
            .launchIn(viewModelScope)
    }
}
