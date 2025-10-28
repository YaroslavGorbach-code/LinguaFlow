package com.korop.yaroslavhorach.common.helpers

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryProductDetails
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resumeWithException

@Singleton
class BillingManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
     var billingClient: BillingClient? = null
    private var listener: PurchaseListener? = null

    companion object {
        const val IN_APP_PERMANENT_SUBSCRIPTION = "talksy_permanent_access"
        const val IN_MONTHLY_SUBSCRIPTION = "mounthly"
        const val IN_6_MONTHLY_SUBSCRIPTION = "6_mounths"
    }

    init {
        initBillingClient()
    }

    suspend fun queryPermanentSubscriptionProduct(): List<ProductDetails> {
        ensureBillingConnected()

        return queryProductDetails(
            listOf(IN_APP_PERMANENT_SUBSCRIPTION),
            BillingClient.ProductType.INAPP
        )
    }

    suspend fun query6MonthSubscriptions(): List<ProductDetails> {
        ensureBillingConnected()

        return queryProductDetails(
            listOf(IN_6_MONTHLY_SUBSCRIPTION),
            BillingClient.ProductType.SUBS
        )
    }

    suspend fun queryMonthSubscriptions(): List<ProductDetails> {
        ensureBillingConnected()

        return queryProductDetails(
            listOf(IN_MONTHLY_SUBSCRIPTION),
            BillingClient.ProductType.SUBS
        )
    }

    fun setOnAcknowledgedListener(purchaseListener: PurchaseListener) {
        listener = purchaseListener
    }

    fun showOneTimeProduct(activity: Activity) {
        scope.launch {
            val products = queryProductDetails(
                listOf(IN_APP_PERMANENT_SUBSCRIPTION),
                BillingClient.ProductType.INAPP
            )
            products.firstOrNull()?.let { productDetails ->
                launchOnTimeProductBillingFlow(activity, productDetails)
            }
        }
    }

    fun showSubscription(activity: Activity, subscriptionId: String) {
        scope.launch {
            val products = queryProductDetails(
                listOf(subscriptionId),
                BillingClient.ProductType.SUBS
            )
            products.firstOrNull()?.let { productDetails ->
                launchSubscriptionBillingFlow(activity, productDetails)
            }
        }
    }

    private fun initBillingClient() {
        billingClient = BillingClient.newBuilder(context)
            .enablePendingPurchases(
                PendingPurchasesParams
                    .newBuilder()
                    .enableOneTimeProducts()
                    .build()
            )
            .setListener { billingResult, purchases ->
                if (purchases.isNullOrEmpty()){
                    listener?.onUserHasNoPurchases()
                }
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                    purchases.forEach(::handlePurchase)
                }
            }
            .build()
    }

    private suspend fun ensureBillingConnected() {
        if (billingClient?.isReady == true) return

        suspendCancellableCoroutine { continuation ->
            billingClient?.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(result: BillingResult) {
                    if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                        continuation.resume(Unit, {})
                    } else {
                        continuation.resumeWithException(
                            RuntimeException("Billing setup failed: ${result.debugMessage}")
                        )
                    }
                }

                override fun onBillingServiceDisconnected() {
                    continuation.resumeWithException(
                        RuntimeException("Billing service disconnected")
                    )
                }
            })
        }
    }

    private suspend fun launchOnTimeProductBillingFlow(activity: Activity, productDetails: ProductDetails) {
        val params = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(productDetails)
            .build()

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(params))
            .build()

        withContext(Dispatchers.Main) {
            val result = billingClient?.launchBillingFlow(activity, billingFlowParams)

            if (result?.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                listener?.onPurchaseAcknowledged()
            }
        }
    }

    private suspend fun launchSubscriptionBillingFlow(activity: Activity, productDetails: ProductDetails) {
        val offerToken = productDetails
            .subscriptionOfferDetails
            ?.firstOrNull()
            ?.offerToken

        if (offerToken == null) {
            Log.e("BillingManager", "No offer token found for subscription ${productDetails.productId}")
            return
        }

        val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(productDetails)
            .setOfferToken(offerToken)
            .build()

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productDetailsParams))
            .build()

        withContext(Dispatchers.Main) {
            val result = billingClient?.launchBillingFlow(activity, billingFlowParams)

            if (result?.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                listener?.onPurchaseAcknowledged()
            }
        }
    }

    private suspend fun queryProductDetails(
        productIds: List<String>,
        productType: String
    ): List<ProductDetails> {
        if (productIds.isEmpty()) return emptyList()

        val products = productIds.map { productId ->
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(productId)
                .setProductType(productType)
                .build()
        }

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(products)
            .build()

        val result = billingClient?.queryProductDetails(params)
        return result?.productDetailsList ?: emptyList()
    }

    private fun handlePurchase(purchase: Purchase) {
        when {
            purchase.purchaseState == Purchase.PurchaseState.PURCHASED &&
                    !purchase.isAcknowledged -> {
                val acknowledgeParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()

                billingClient?.acknowledgePurchase(acknowledgeParams) {
                    listener?.onPurchaseAcknowledged()
                }
            }
        }
    }

    suspend fun getAllActivePurchases(): List<Purchase>{
        ensureBillingConnected()
        val inAppDeferred = scope.async {
            suspendCancellableCoroutine<List<Purchase>> { cont ->
                billingClient?.queryPurchasesAsync(
                    QueryPurchasesParams.newBuilder()
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                ) { billingResult, purchases ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        cont.resume(purchases, {})
                    } else {
                        cont.resume(emptyList(), {})
                    }
                }
            }
        }

        val subsDeferred = scope.async {
            suspendCancellableCoroutine<List<Purchase>> { cont ->
                billingClient?.queryPurchasesAsync(
                    QueryPurchasesParams.newBuilder()
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                ) { billingResult, purchases ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        cont.resume(purchases, {})
                    } else {
                        cont.resume(emptyList(), {})
                    }
                }
            }
        }

        val inApp = inAppDeferred.await()
        val subs = subsDeferred.await()

        return inApp + subs
    }


    interface PurchaseListener {
        fun onPurchaseAcknowledged()
        fun onUserHasNoPurchases()
    }
}