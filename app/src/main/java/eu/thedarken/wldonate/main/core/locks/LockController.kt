package eu.thedarken.wldonate.main.core.locks

import eu.thedarken.wldonate.AppComponent
import eu.thedarken.wldonate.main.core.GeneralSettings
import io.reactivex.Completable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import javax.inject.Inject

@AppComponent.Scope
class LockController @Inject constructor(
        locks: Map<@JvmSuppressWildcards Lock.Type, @JvmSuppressWildcards Lock>,
        val settings: GeneralSettings
) {

    val locksPub: BehaviorSubject<Map<Lock.Type, Lock>> = BehaviorSubject.createDefault(locks)

    private fun notifyOfChanges() {
        locksPub.onNext(locksPub.value!!)
    }

    @Synchronized
    fun acquireExclusive(desired: Collection<Lock.Type>): Completable {
        Timber.i("Acquiring %s", desired)
        return locksPub.firstOrError()
                .map { lockMap ->
                    lockMap.forEach {
                        if (desired.contains(it.key)) {
                            it.value.acquire()
                        } else {
                            it.value.release()
                        }
                    }
                }
                .doOnSuccess {
                    settings.setActive(!desired.isEmpty())
                    notifyOfChanges()
                }
                .ignoreElement()
    }

}