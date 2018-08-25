package eu.thedarken.wldonate.main.ui.manager

import android.app.Activity
import eu.darken.mvpbakery.base.Presenter
import eu.darken.mvpbakery.injection.ComponentPresenter
import eu.thedarken.wldonate.IAPHelper
import eu.thedarken.wldonate.main.core.GeneralSettings
import eu.thedarken.wldonate.main.core.locks.Lock
import eu.thedarken.wldonate.main.core.locks.LockController
import eu.thedarken.wldonate.main.ui.Navigator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@ManagerFragmentComponent.Scope
class ManagerFragmentPresenter @Inject constructor(
        private val navigator: Navigator,
        private val controller: LockController,
        private val settings: GeneralSettings,
        private val iapHelper: IAPHelper
) : ComponentPresenter<ManagerFragmentPresenter.View, ManagerFragmentComponent>() {
    enum class LocksState {
        NONE, PAUSED, ACTIVE
    }

    enum class DonateState {
        UNKNOWN, NONE, SOME, ALL
    }

    internal var locksState: LocksState = LocksState.NONE
    private var lockSub: Disposable = Disposables.disposed()
    private var donationSub: Disposable = Disposables.disposed()

    override fun onBindChange(view: View?) {
        super.onBindChange(view)
        if (view != null && lockSub.isDisposed) {
            controller.locksPub
                    .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe { locks: Map<Lock.Type, Lock> ->
                        onView {
                            val savedLocks = settings.getSavedLocks()
                            it.showLocks(
                                    ArrayList(locks.values),
                                    savedLocks
                            )
                            var hasActive = false
                            locks.values.forEach { if (it.isAcquired()) hasActive = true }
                            locksState = when {
                                !hasActive && !savedLocks.isEmpty() -> LocksState.PAUSED
                                hasActive -> LocksState.ACTIVE
                                else -> LocksState.NONE
                            }
                            it.updatePausedInfo(
                                    locksState,
                                    settings.isAutostartCallEnabled()
                            )
                        }
                    }
        } else if (view == null) {
            lockSub.dispose()
        }

        if (view != null && donationSub.isDisposed) {
            donationSub = iapHelper.upgradesPublisher
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .map {
                        val purchases = ArrayList<IAPHelper.Upgrade.Type>()
                        it.forEach { if (it.value.isPurchased()) purchases.add(it.key) }
                        purchases
                    }
                    .subscribe { purchases ->
                        onView {
                            val level: Float = (purchases.size / IAPHelper.Upgrade.Type.values().size).toFloat()
                            it.updateDonationOptions(level)
                        }
                    }
        } else {
            donationSub.dispose()
        }
    }

    fun onShowSettings() {
        navigator.goToSettings()
    }

    fun onToggleLock(lock: Lock) {
        val savedLocks = settings.getSavedLocks()
        if (savedLocks.contains(lock.getType())) {
            savedLocks.remove(lock.getType())
        } else {
            savedLocks.add(lock.getType())
        }
        settings.saveLocks(savedLocks)

        if (locksState != LocksState.ACTIVE && settings.isAutostartCallEnabled()) {
            savedLocks.clear()
        }

        controller.acquireExclusive(savedLocks)
                .subscribeOn(Schedulers.computation())
                .subscribe()
    }

    fun onPauseLocks() {
        controller.locksPub
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .compose { Lock.acquiredOnly(it) }
                .firstOrError()
                .flatMapCompletable {
                    return@flatMapCompletable when {
                        it.isEmpty() -> controller.acquireExclusive(settings.getSavedLocks())
                        else -> controller.acquireExclusive(Collections.emptySet())
                    }
                }
                .subscribe()
    }

    fun onDonateScreenClicked() {
        iapHelper.upgradesPublisher
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map {
                    val nonPurchased = ArrayList(it.values)
                    it.values.forEach { if (it.isPurchased()) nonPurchased.remove(it) }
                    return@map nonPurchased
                }
                .subscribe { openPurchases -> onView { it.showDonationScreen(openPurchases) } }
    }

    fun onDonate(upgrade: IAPHelper.Upgrade, activity: Activity) {
        iapHelper.startDonationFlow(upgrade, activity)
        onView { it.showThanks() }
    }

    interface View : Presenter.View {
        fun showLocks(locks: List<Lock>, saved: Collection<Lock.Type>)

        fun updatePausedInfo(locksState: LocksState, onCallOption: Boolean)

        fun updateDonationOptions(level: Float)

        fun showDonationScreen(donations: List<IAPHelper.Upgrade>)

        fun showThanks()
    }
}
