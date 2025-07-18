package com.korop.yaroslavhorach.settings

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.viewModelScope
import com.korop.yaroslavhorach.common.base.BaseViewModel
import com.korop.yaroslavhorach.common.utill.UiMessage
import com.korop.yaroslavhorach.domain.prefs.PrefsRepository
import com.korop.yaroslavhorach.profile.R
import com.korop.yaroslavhorach.settings.model.Language
import com.korop.yaroslavhorach.settings.model.SettingsAction
import com.korop.yaroslavhorach.settings.model.SettingsItemType
import com.korop.yaroslavhorach.settings.model.SettingsItemUi
import com.korop.yaroslavhorach.settings.model.SettingsSectionUi
import com.korop.yaroslavhorach.settings.model.SettingsUiMessage
import com.korop.yaroslavhorach.settings.model.SettingsViewState
import com.korop.yaroslavhorach.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefsRepository: PrefsRepository,
    @ApplicationContext val context: Context
) : BaseViewModel<SettingsViewState, SettingsAction, SettingsUiMessage>() {

    override val pendingActions: MutableSharedFlow<SettingsAction> =
        MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override val state: StateFlow<SettingsViewState> = combine(
        prefsRepository.getUserData(),
        uiMessageManager.message,
    ) { userData, messages ->
        val supportedLanguages = prefsRepository.getSupportedAppLanguages().map(::Language)

        val deviseLanguage = Language((userData.deviceLanguage ?: run {
            val language = supportedLanguages
                .find { it.value == Locale.getDefault().language }?.value ?: "en"
            prefsRepository.changeLanguage(language)
            language
        }))

        SettingsViewState(
            isPremiumUser = userData.isPremium,
            userName = userData.userName,
            sections = listOf(
                SettingsSectionUi(
                    title = UiText.FromString(context.getString(R.string.settings_parameters_section_title_text)),
                    items = listOf(
                        SettingsItemUi(
                            title = UiText.FromString(context.getString(R.string.settings_language_title_text)),
                            subtitle = UiText.FromString(deviseLanguage.getLocalisedDisplayName(context)),
                            type = SettingsItemType.CHANGE_LANGUAGE
                        )
                    )
                ),
                SettingsSectionUi(
                    title = UiText.FromString(context.getString(R.string.settings_daily_challange_title_text)),
                    items = listOf(
                        SettingsItemUi(
                            title = UiText.FromString(context.getString(R.string.settings_daily_challange_mix_activate_title_text)),
                            subtitle = UiText.FromString(context.getString(R.string.settings_daily_challange_mix_activate_subtitle_text)),
                            type = SettingsItemType.ACTIVATE_DAILY_MIX
                        ),
                        SettingsItemUi(
                            title = UiText.FromString(context.getString(R.string.settings_daily_challange_timed_activate_title_text)),
                            subtitle = UiText.FromString(context.getString(R.string.settings_daily_challange_timed_activate_subtitle_text)),
                            type = SettingsItemType.ACTIVATE_15_MINUTES_TOPIC
                        )
                    )
                ),
                SettingsSectionUi(
                    title = UiText.FromString(context.getString(R.string.settings_support_section_title_text)),
                    items = listOf(
                        SettingsItemUi(
                            title = UiText.FromString(context.getString(R.string.setings_rate_app_title_text)),
                            subtitle = UiText.FromString(context.getString(R.string.settings_rate_app_subtilte_text)),
                            type = SettingsItemType.RATE
                        ),
                        SettingsItemUi(
                            title = UiText.FromString(context.getString(R.string.settings_conenct_with_developer_title_text)),
                            subtitle = UiText.FromString(context.getString(R.string.settings_conenct_with_developer_subtitle_text)),
                            type = SettingsItemType.FEEDBACK
                        )
                    )
                ),
            ),
            isMixTrainingAvailable = userData.isMixTrainingAvailable,
            is15MinutesTrainingAvailable = userData.is15MinutesTrainingAvailable,
            languages = supportedLanguages,
            currentLanguage = deviseLanguage.value,
            uiMessage = messages,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsViewState.Empty
    )

    init {
        pendingActions
            .onEach { event ->
                when (event) {
                    is SettingsAction.OnSettingsItemClicked -> {
                        when (event.type) {
                            SettingsItemType.CHANGE_LANGUAGE -> uiMessageManager.emitMessage(UiMessage(SettingsUiMessage.ShowChooseLanguageBottomSheet))
                            SettingsItemType.RATE -> uiMessageManager.emitMessage(UiMessage(SettingsUiMessage.ToRateApp))
                            SettingsItemType.FEEDBACK -> uiMessageManager.emitMessage(UiMessage(SettingsUiMessage.ToGiveFeedback))
                            else -> {}
                        }
                    }
                    is SettingsAction.OnSettingsItemChecked -> {
                        when (event.type) {
                            SettingsItemType.ACTIVATE_DAILY_MIX -> {
                                prefsRepository.changeMixDailyTrainingActive(event.isChecked)
                                uiMessageManager.emitMessage(UiMessage(SettingsUiMessage.ShowDailyTrainingChangesToast))

                                if (event.isChecked.not() && state.value.is15MinutesTrainingAvailable.not()){
                                    prefsRepository.change15MinutesTopicDailyTrainingActive(true)
                                }
                            }
                            SettingsItemType.ACTIVATE_15_MINUTES_TOPIC -> {
                                prefsRepository.change15MinutesTopicDailyTrainingActive(event.isChecked)
                                uiMessageManager.emitMessage(UiMessage(SettingsUiMessage.ShowDailyTrainingChangesToast))

                                if (event.isChecked.not() && state.value.isMixTrainingAvailable.not()){
                                    prefsRepository.changeMixDailyTrainingActive(true)
                                }
                            }
                            else -> {}
                        }
                    }
                    is SettingsAction.OnLanguageSelected -> {
                        val appLocale: LocaleListCompat = LocaleListCompat.create(Locale(event.language.value))
                        AppCompatDelegate.setApplicationLocales(appLocale)

                        prefsRepository.changeLanguage(event.language.value)
                    }
                    else -> {}
                }
            }
            .launchIn(viewModelScope)
    }
}
