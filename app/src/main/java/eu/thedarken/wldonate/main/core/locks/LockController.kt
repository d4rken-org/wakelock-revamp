package eu.thedarken.wldonate.main.core.locks

import eu.thedarken.wldonate.AppComponent
import io.reactivex.Completable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import javax.inject.Inject

@AppComponent.Scope
class LockController @Inject constructor(
        locks: Map<@JvmSuppressWildcards Lock.Type, @JvmSuppressWildcards Lock>
) {

    val locksPub: BehaviorSubject<Map<Lock.Type, Lock>> = BehaviorSubject.createDefault(locks)

    @Synchronized
    fun toggle(lockType: Lock.Type): Completable {
        return Completable.create {
            val lock: Lock = locksPub.blockingFirst().get(lockType)!!
            Timber.i("Toggling: %s", lock)
            if (lock.isAcquired()) lock.release()
            else lock.acquire()
            Timber.i("New lock-state: %s.isAcquired()=%b", lock, lock.isAcquired())
            notifyOfChanges()
        }
    }

    private fun notifyOfChanges() {
        locksPub.onNext(locksPub.value!!)
    }

    @Synchronized
    fun acquireExclusive(desired: Collection<Lock.Type>): Completable {
        Timber.i("Acquiring %s", desired)
        return locksPub.firstOrError()
                .map {
                    it.forEach {
                        if (desired.contains(it.key)) {
                            it.value.acquire()
                        } else {
                            it.value.release()
                        }
                    }
                }
                .doOnSuccess { notifyOfChanges() }
                .ignoreElement()
    }

}