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
            val permanentSubscription = billingManager.queryPermanentSubscriptionProduct()
            val sixMonthSubscription = billingManager.query6MonthSubscriptions()
            val monthSubscription = billingManager.queryMonthSubscriptions()

            variants.value = listOf(
                PremiumVariant.Month(
                    UiText.FromResource(R.string.premium_variant_mounth_title_text),
                    UiText.FromResource(
                        R.string.premium_variant_prise_mounthly_format,
                        extractAmountAndCurrency(monthSubscription.joinToString())?.first.toString()
                                + " " +  extractAmountAndCurrency(monthSubscription.joinToString())?.second
                    ),
                    UiText.Empty
                ),
                PremiumVariant.SixMonth(
                    UiText.FromResource(R.string.premium_variant_6_mounth_title_text),
                    UiText.FromResource(
                        R.string.premium_variant_prise_half_a_year_roemat,
                        extractAmountAndCurrency(sixMonthSubscription.joinToString())?.first.toString()
                                + " " +  extractAmountAndCurrency(sixMonthSubscription.joinToString())?.second
                    ),
                    UiText.FromResource(
                        R.string.premium_variant_prise_mounthly_format,
                        ((extractAmountAndCurrency(sixMonthSubscription.joinToString())?.first ?: 0.0) / 6).roundToInt()
                            .toString() + " " + extractAmountAndCurrency(sixMonthSubscription.joinToString())?.second
                    )
                ),
                PremiumVariant.Forever(
                    UiText.FromResource(R.string.premium_variant_permanent_title_text),
                    UiText.FromString(
                        extractAmountAndCurrency(permanentSubscription.joinToString())?.first.toString()
                                + " " +  extractAmountAndCurrency(permanentSubscription.joinToString())?.second + "/∞"
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

    private fun extractAmountAndCurrency(logString: String): Pair<Double, String>? {
        val jsonRegex = Regex("""jsonString='(.*?)'""", RegexOption.DOT_MATCHES_ALL)
        val jsonMatch = jsonRegex.find(logString) ?: return null
        val jsonString = jsonMatch.groupValues[1]

        val priceRegex = Regex(""""formattedPrice"\s*:\s*"(.*?)"""")
        val priceMatch = priceRegex.find(jsonString)
        val formattedPrice = priceMatch?.groupValues?.get(1) ?: return null

        val match = Regex("""([\d,.]+)[\s\u00A0]*(\D+)""").find(formattedPrice)
            ?: return null

        val amountString = match.groupValues[1].replace(",", ".")
        val currencyRaw = match.groupValues[2].trim()

        val amount = amountString.toDoubleOrNull() ?: return null
        val currencySymbol = mapCurrencySymbol(currencyRaw)

        return Pair(amount, currencySymbol)
    }

    private fun mapCurrencySymbol(code: String): String {
        return when (code) {
            "UAH" -> "₴"
            "USD" -> "$"
            "EUR" -> "€"
            "GBP" -> "£"
            else -> code
        }
    }
}