package com.korop.yaroslavhorach.designsystem.screens.premium

import androidx.lifecycle.viewModelScope
import com.korop.yaroslavhorach.common.base.BaseViewModel
import com.korop.yaroslavhorach.common.utill.UiMessage
import com.korop.yaroslavhorach.designsystem.R
import com.korop.yaroslavhorach.designsystem.screens.premium.model.PremiumAction
import com.korop.yaroslavhorach.designsystem.screens.premium.model.PremiumUiMessage
import com.korop.yaroslavhorach.designsystem.screens.premium.model.PremiumVariant
import com.korop.yaroslavhorach.designsystem.screens.premium.model.PremiumViewState
import com.korop.yaroslavhorach.domain.prefs.PrefsRepository
import com.korop.yaroslavhorach.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    prefsRepository: PrefsRepository
) : BaseViewModel<PremiumViewState, PremiumAction, PremiumUiMessage>() {

    override val pendingActions: MutableSharedFlow<PremiumAction> = MutableSharedFlow()

    private val variants: MutableStateFlow<List<PremiumVariant>> = MutableStateFlow(emptyList())

    override val state: StateFlow<PremiumViewState> = combine(
        variants,
        uiMessageManager.message
    ) { variants, message ->
        PremiumViewState(variants = variants, message)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PremiumViewState.Empty
    )

    init {
        variants.value = listOf(
            PremiumVariant.Month(UiText.FromResource(R.string.premium_variant_mounth_title_text), UiText.FromString("99 ₴/місяць"), UiText.Empty),
            PremiumVariant.SixMonth(UiText.FromResource(R.string.premium_variant_6_mounth_title_text),
                UiText.FromString("239 ₴/півроку"),
                UiText.FromString("39 ₴/місяць")
            ),
            PremiumVariant.Forever(
                UiText.FromResource(R.string.premium_variant_permanent_title_text),
                UiText.FromString("399 ₴/∞"),
                UiText.FromResource(R.string.premium_variant_the_best_bage_title_text)
            )
        )

        pendingActions
            .onEach { event ->
                when (event) {
                    is PremiumAction.OnGetPremiumClicked -> {
                        uiMessageManager.emitMessage(UiMessage(PremiumUiMessage.NavigateToSuccess))
                        prefsRepository.activatePremium()
                    }
                    is PremiumAction.OnVariantChosen -> {
                        variants.update { list ->
                            list.map { variant ->
                                when (variant) {
                                    is PremiumVariant.Month -> variant.copy(
                                        isSelected = variant.title == event.premiumVariant.title
                                    )
                                    is PremiumVariant.SixMonth -> variant.copy(
                                        isSelected = variant.title == event.premiumVariant.title
                                    )
                                    is PremiumVariant.Forever -> variant.copy(
                                        isSelected = variant.title == event.premiumVariant.title
                                    )
                                }
                            }
                        }
                    }
                    else -> error("Action $event is not handled")
                }
            }
            .launchIn(viewModelScope)
    }
}
