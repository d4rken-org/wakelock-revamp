package eu.thedarken.wldonate.main.ui.manager

import eu.darken.mvpbakery.base.Presenter
import eu.darken.mvpbakery.injection.ComponentPresenter
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
) : ComponentPresenter<ManagerFragmentPresenter.View, ManagerFragmentComponent>() {
    enum class LocksState {
        NONE, PAUSED, ACTIVE
    }

    internal var locksState: LocksState = LocksState.NONE
    private var lockSub: Disposable = Disposables.disposed()

    override fun onBindChange(view: View?) {
        super.onBindChange(view)
        if (view != null && lockSub.isDisposed) {
            controller.locksPub
                    .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe { locks: Map<Lock.Type, Lock> ->
                        withView {
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

    interface View : Presenter.View {
        fun showLocks(locks: List<Lock>, saved: Collection<Lock.Type>)

        fun updatePausedInfo(locksState: LocksState, onCallOption: Boolean)
    }
}
