package eu.thedarken.wldonate.main.ui.settings

import android.content.pm.PackageManager
import eu.thedarken.wldonate.BuildConfig
import eu.thedarken.wldonate.common.mvpbakery.base.Presenter
import eu.thedarken.wldonate.common.mvpbakery.injection.ComponentPresenter
import eu.thedarken.wldonate.main.core.GeneralSettings
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@SettingsFragmentComponent.Scope
class SettingsFragmentPresenter @Inject constructor(
        val packageManager: PackageManager,
        val settings: GeneralSettings
) : ComponentPresenter<SettingsFragmentPresenter.View, SettingsFragmentComponent>() {


    override fun onBindChange(view: View?) {
        super.onBindChange(view)
        Single
                .create<String> {
                    val packageInfo = packageManager.getPackageInfo(BuildConfig.APPLICATION_ID, 0)
                    it.onSuccess(packageInfo.versionName + "(" + packageInfo.versionCode + ")")
                }
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe { version -> withView { it.updateVersion(version) } }
    }


    interface View : Presenter.View {
        fun updateVersion(version: String)
    }
}
