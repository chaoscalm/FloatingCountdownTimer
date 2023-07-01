package xyz.tberghuis.floatingtimer.tmp.iaphalo

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.LaunchedEffect
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.ProductDetailsResponseListener
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesResponseListener
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import kotlin.coroutines.Continuation
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import xyz.tberghuis.floatingtimer.logd

class BillingDataSource(
  context: Context
) : PurchasesUpdatedListener {

  private val billingClient = BillingClient.newBuilder(context)
    .setListener(this)
    .enablePendingPurchases()
    .build()

  // doitwrong: this is wack but it works
  private var purchasesUpdatedContinuation: Continuation<BillingResult>? = null

  override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
    logd("purchasesUpdatedListener")
    purchasesUpdatedContinuation?.resume(billingResult)
    if (billingResult.responseCode != BillingClient.BillingResponseCode.OK || purchases.isNullOrEmpty()) {
      return
    }
    for (purchase in purchases) {
      acknowledgePurchase(purchase)
    }
  }

  private fun acknowledgePurchase(purchase: Purchase) {
    if (!purchase.isAcknowledged) {
      val params = AcknowledgePurchaseParams.newBuilder()
        .setPurchaseToken(purchase.purchaseToken)
        .build()
      billingClient.acknowledgePurchase(
        params
      ) { billingResult ->
        logd("acknowledgePurchase billingResult $billingResult")
      }
    }
  }

  suspend fun startBillingConnection(): BillingResult = suspendCoroutine { continuation ->
    val billingClientStateListener = object : BillingClientStateListener {
      override fun onBillingServiceDisconnected() {
        // probably best to let the app crash if this called???
        logd("onBillingServiceDisconnected")
      }

      override fun onBillingSetupFinished(billingResult: BillingResult) {
        continuation.resume(billingResult)
      }
    }
    billingClient.startConnection(billingClientStateListener)
  }

  // rewrite with my own result type, or arrow either
  // allow me to show snackbars on error cases

  // startBillingConnection must be called first
  suspend fun checkHaloColourPurchased(
    updateHaloColourPurchased: suspend () -> Unit = {}
  ) {
    // query product details
    val purchased = isHaloColourPurchased(billingClient)
    if (purchased) {
      updateHaloColourPurchased()
    }
  }

  fun endBillingConnection() {
    logd("Terminating connection")
    billingClient.endConnection()
  }

  // todo return PurchaseHaloColourChangeResult
  // future.txt use arrow.kt Either
  suspend fun purchaseHaloColourChange(activity: Activity): BillingResult {
    // will this get cancelled properly
    // according to structured concurrency???
    val scope = CoroutineScope(coroutineContext)
    return suspendCoroutine { continuation ->
      // this is a hack, shouldn't be a problem if using fresh BillingClientWrapper
      // for each call to purchaseHaloColourChange()
      purchasesUpdatedContinuation = continuation
      scope.launch(IO) {
        val productDetails = getHaloColourProductDetails(billingClient)

        val productDetailsParams =
          BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(productDetails)
            .build()

        val params = BillingFlowParams.newBuilder()
          .setProductDetailsParamsList(listOf(productDetailsParams))
          .build()
        billingClient.launchBillingFlow(activity, params)
      }
    }
  }
}

/////////////////////////////////////////////////////////

private suspend fun getHaloColourProductDetails(billingClient: BillingClient): ProductDetails =
  suspendCoroutine { continuation ->
    val productId = "halo_colour"
    val product = QueryProductDetailsParams.Product.newBuilder()
      .setProductId(productId)
      .setProductType(BillingClient.ProductType.INAPP)
      .build()

    val params = QueryProductDetailsParams.newBuilder().setProductList(listOf(product)).build()

    val productDetailsResponseListener =
      ProductDetailsResponseListener { billingResult, productDetailsList ->
        logd("productDetailsResponseListener")
        logd("billingResult $billingResult")
        logd("productDetailsList $productDetailsList")

        val productDetails = productDetailsList.find {
          it.productId == "halo_colour"
        }

        continuation.resume(productDetails!!)

      }
    billingClient.queryProductDetailsAsync(params, productDetailsResponseListener)
  }

private suspend fun isHaloColourPurchased(
  billingClient: BillingClient,
): Boolean =
  suspendCoroutine { continuation ->
    val queryPurchasesParams =
      QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build()

    val purchasesResponseListener =
      PurchasesResponseListener { billingResult: BillingResult, purchases: MutableList<Purchase> ->
        logd("purchasesResponseListener")
        logd("billingResult $billingResult")
        logd("purchases $purchases")
        var purchased = false
        when (billingResult.responseCode) {
          BillingClient.BillingResponseCode.OK -> {
            for (purchase in purchases) {
              if (purchase.products.contains("halo_colour")) {
                purchased = true
                break
              }
            }
          }

          else -> {
            // TODO snackbar debugMessage
          }
        }
        continuation.resume(purchased)
      }
    billingClient.queryPurchasesAsync(queryPurchasesParams, purchasesResponseListener)
  }