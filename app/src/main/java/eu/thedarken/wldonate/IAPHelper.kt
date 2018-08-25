package eu.thedarken.wldonate

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.content.res.AppCompatResources
import com.android.billingclient.api.*
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import javax.inject.Inject


@AppComponent.Scope
class IAPHelper @Inject constructor(@ApplicationContext context: Context) : PurchasesUpdatedListener, BillingClientStateListener {
    val upgradesPublisher = BehaviorSubject.create<Map<Upgrade.Type, Upgrade>>()
    private val billingClient: BillingClient = BillingClient.newBuilder(context).setListener(this).build()

    init {
        billingClient.startConnection(this)
    }

    class CoffeeUpgrade(purchase: Purchase? = null) : Upgrade(Type.DONATION_COFFEE, SKU) {
        companion object {
            const val SKU = "donation.coffee"
        }

        override fun getIcon(context: Context): Drawable = AppCompatResources.getDrawable(context, R.drawable.ic_coffee_white_24dp)!!
        override fun getLabel(context: Context): String = context.getString(R.string.donation_coffee_label)
        override fun getDescription(context: Context): String = context.getString(R.string.donation_coffee_description)
    }

    class PizzaUpgrade(purchase: Purchase? = null) : Upgrade(Type.DONATION_PIZZA, SKU) {
        companion object {
            const val SKU = "donation.pizza"
        }

        override fun getIcon(context: Context): Drawable = AppCompatResources.getDrawable(context, R.drawable.ic_pizza_white_24dp)!!
        override fun getLabel(context: Context): String = context.getString(R.string.donation_pizza_label)
        override fun getDescription(context: Context): String = context.getString(R.string.donation_pizza_description)
    }

    abstract class Upgrade(val type: Type, val sku: String) {
        var purchase: Purchase? = null

        enum class Type {
            DONATION_COFFEE, DONATION_PIZZA
        }

        fun isPurchased(): Boolean = purchase != null

        abstract fun getIcon(context: Context): Drawable
        abstract fun getLabel(context: Context): String
        abstract fun getDescription(context: Context): String
    }

    override fun onBillingSetupFinished(responseCode: Int) {
        Timber.d("onBillingSetupFinished(responseCode=%d)", responseCode)
        if (BillingClient.BillingResponse.OK == responseCode) {
            val purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP)
            Timber.d("queryPurchases(): code=%d, purchases=%s", purchasesResult.responseCode, purchasesResult.purchasesList)
            onPurchasesUpdated(purchasesResult.responseCode, purchasesResult.purchasesList)
        }
    }

    override fun onBillingServiceDisconnected() {
        Timber.d("onBillingServiceDisconnected()")
    }

    override fun onPurchasesUpdated(responseCode: Int, purchases: List<Purchase>?) {
        Timber.d("onPurchasesUpdated(responseCode=%d, purchases=%s)", responseCode, purchases)
        if (purchases != null) notifyOfPurchases(purchases)
    }

    private fun notifyOfPurchases(purchases: List<Purchase>) {
        Timber.d("notifyOfPurchases(%s)", purchases)
        val upgrades = HashMap<Upgrade.Type, Upgrade>()
        upgrades.put(Upgrade.Type.DONATION_COFFEE, CoffeeUpgrade())
        upgrades.put(Upgrade.Type.DONATION_PIZZA, PizzaUpgrade())
        for (p in purchases) {
            val upgrade: Upgrade? = when {
                p.sku.endsWith(CoffeeUpgrade.SKU) -> upgrades[Upgrade.Type.DONATION_COFFEE]
                p.sku.endsWith(PizzaUpgrade.SKU) -> upgrades[Upgrade.Type.DONATION_PIZZA]
                else -> null
            }
            if (upgrade == null) Timber.e("No purchase found for %s", p)
            upgrade?.purchase = p
        }
//        if (BuildConfig.DEBUG) {
//            upgrades[Upgrade.Type.DONATION_COFFEE]?.purchase = Purchase("{}", "{}")
//            upgrades[Upgrade.Type.DONATION_PIZZA]?.purchase = Purchase("{}", "{}")
//        }
        upgradesPublisher.onNext(upgrades)
    }

    fun check() {
        Single.create<Purchase.PurchasesResult> { it.onSuccess(billingClient.queryPurchases(BillingClient.SkuType.INAPP)) }
                .subscribeOn(Schedulers.io())
                .filter { r -> r.responseCode == 0 && r.purchasesList != null }
                .map { it.purchasesList }
                .subscribe({ this.notifyOfPurchases(it) }, { Timber.e(it) })
    }

    fun startDonationFlow(upgrade: Upgrade, activity: Activity) {
        val billingFlowParams = BillingFlowParams.newBuilder()
                .setSku(upgrade.sku)
                .setType(BillingClient.SkuType.INAPP)
                .build()
        billingClient.launchBillingFlow(activity, billingFlowParams)
    }
}