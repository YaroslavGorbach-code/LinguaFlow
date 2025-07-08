package com.korop.yaroslavhorach.designsystem.screens.premium

import androidx.lifecycle.viewModelScope
import com.korop.yaroslavhorach.common.base.BaseViewModel
import com.korop.yaroslavhorach.common.helpers.BillingManager
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
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val prefsRepository: PrefsRepository,
    val billingManager: BillingManager
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
        viewModelScope.launch {
            val currency = mapCurrencySymbol(billingManager.queryMonthSubscriptions()
                .first()
                .subscriptionOfferDetails
                ?.first()
                ?.pricingPhases?.pricingPhaseList
                ?.first()
                ?.priceCurrencyCode)

            val permanentSubscriptionPrice = billingManager.queryPermanentSubscriptionProduct()
                .first()
                .oneTimePurchaseOfferDetails
                ?.priceAmountMicros?.div(1_000_000.0)

            val sixMonthSubscriptionPrice = billingManager.query6MonthSubscriptions()
                .first()
                .subscriptionOfferDetails
                ?.first()
                ?.pricingPhases?.pricingPhaseList
                ?.first()
                ?.priceAmountMicros?.div(1_000_000.0)

            val monthSubscriptionPrice = billingManager.queryMonthSubscriptions()
                .first()
                .subscriptionOfferDetails
                ?.first()
                ?.pricingPhases?.pricingPhaseList
                ?.first()
                ?.priceAmountMicros?.div(1_000_000.0)


            variants.value = listOf(
                PremiumVariant.Month(
                    UiText.FromResource(R.string.premium_variant_mounth_title_text),
                    UiText.FromResource(
                        R.string.premium_variant_prise_mounthly_format,
                        monthSubscriptionPrice.toString()
                                + " " + mapCurrencySymbol(currency)
                    ),
                    UiText.Empty
                ),
                PremiumVariant.SixMonth(
                    UiText.FromResource(R.string.premium_variant_6_mounth_title_text),
                    UiText.FromResource(
                        R.string.premium_variant_prise_half_a_year_roemat,
                        sixMonthSubscriptionPrice.toString()
                                + " " +  mapCurrencySymbol(currency)
                    ),
                    UiText.FromResource(
                        R.string.premium_variant_prise_mounthly_format,
                        ((sixMonthSubscriptionPrice ?: 0.0) / 6).roundToInt()
                            .toString() + " " +  mapCurrencySymbol(currency)
                    )
                ),
                PremiumVariant.Forever(
                    UiText.FromResource(R.string.premium_variant_permanent_title_text),
                    UiText.FromString(
                        permanentSubscriptionPrice.toString()
                                + " " +  mapCurrencySymbol(currency) + "/∞"
                    ),
                    UiText.FromResource(R.string.premium_variant_the_best_bage_title_text)
                )
            )
        }

        pendingActions
            .onEach { event ->
                when (event) {
                    is PremiumAction.OnGetPremiumClicked -> {
                        when(state.value.variants.find { it.isSelected }){
                            is PremiumVariant.Forever -> {
                                uiMessageManager.emitMessage(UiMessage(PremiumUiMessage.ShowLifeTimeSubscriptionDialog))
                            }
                            is PremiumVariant.Month -> {
                                uiMessageManager.emitMessage(UiMessage(PremiumUiMessage.ShowMonthSubscriptionDialog))
                            }
                            is PremiumVariant.SixMonth -> {
                                uiMessageManager.emitMessage(UiMessage(PremiumUiMessage.Show6MonthSubscriptionDialog))
                            }
                            null -> {}
                        }
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

        billingManager.setOnAcknowledgedListener(object : BillingManager.PurchaseListener {
            override fun onPurchaseAcknowledged(purchase: com.android.billingclient.api.Purchase) {
                viewModelScope.launch {
                    prefsRepository.activatePremium()
                    uiMessageManager.emitMessage(UiMessage(PremiumUiMessage.NavigateToSuccess()))
                }
            }

            override fun onUserHasNoPurchases() {
                viewModelScope.launch {
                    prefsRepository.deactivatePremium()
                }
            }
        })
    }

    private fun mapCurrencySymbol(code: String?): String {
        return when (code?.uppercase()) {
            "UAH" -> "₴"
            "USD" -> "$"
            "EUR" -> "€"
            "GBP" -> "£"
            "JPY" -> "¥"
            "CNY", "RMB" -> "¥"
            "KRW" -> "₩"
            "INR" -> "₹"
            "RUB" -> "₽"
            "TRY" -> "₺"
            "BRL" -> "R$"
            "MXN" -> "MX$"
            "CAD" -> "C$"
            "AUD" -> "A$"
            "NZD" -> "NZ$"
            "CHF" -> "CHF"
            "SEK" -> "kr"
            "NOK" -> "kr"
            "DKK" -> "kr"
            "PLN" -> "zł"
            "CZK" -> "Kč"
            "HUF" -> "Ft"
            "ZAR" -> "R"
            "ILS" -> "₪"
            "SAR" -> "﷼"
            "AED" -> "د.إ"
            "IDR" -> "Rp"
            "VND" -> "₫"
            "THB" -> "฿"
            "MYR" -> "RM"
            "SGD" -> "S$"
            "HKD" -> "HK$"
            "TWD" -> "NT$"
            "ARS" -> "AR$"
            "CLP" -> "CLP$"
            "COP" -> "COL$"
            null -> ""
            else -> code
        }
    }
}