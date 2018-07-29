package eu.thedarken.wldonate.main.ui.fragment

import eu.darken.mvpbakery.base.Presenter
import eu.darken.mvpbakery.injection.ComponentPresenter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ExampleFragmentComponent.Scope
class ExampleFragmentPresenter @Inject
internal constructor(private val someRepo: SomeRepo) : ComponentPresenter<ExampleFragmentPresenter.View, ExampleFragmentComponent>() {
    private var emojiSub = Disposables.disposed()

    override fun onBindChange(view: View?) {
        super.onBindChange(view)
        subToEmojis()
    }

    private fun subToEmojis() {
        if (view != null && emojiSub.isDisposed) {
            emojiSub = someRepo.emoji
                    .repeatWhen { errors -> errors.flatMapSingle { error -> Single.timer(1, TimeUnit.SECONDS) } }
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { emoji -> onView { v -> v.showEmoji(emoji) } }
        } else {
            emojiSub.dispose()
        }
    }

    fun onGetNewEmoji() {
        emojiSub.dispose()
        subToEmojis()
    }

    interface View : Presenter.View {
        fun showEmoji(emoji: String)
    }
}
