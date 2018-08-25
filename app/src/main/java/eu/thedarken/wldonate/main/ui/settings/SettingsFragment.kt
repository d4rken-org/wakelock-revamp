package eu.thedarken.wldonate.main.ui.settings

import android.Manifest
import android.os.Bundle
import android.support.v7.preference.CheckBoxPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import com.tbruyelle.rxpermissions2.RxPermissions
import eu.darken.mvpbakery.base.MVPBakery
import eu.darken.mvpbakery.base.ViewModelRetainer
import eu.darken.mvpbakery.injection.InjectedPresenter
import eu.darken.mvpbakery.injection.PresenterInjectionCallback
import eu.thedarken.wldonate.R
import eu.thedarken.wldonate.main.core.GeneralSettings


class SettingsFragment : PreferenceFragmentCompat(), SettingsFragmentPresenter.View {
    private lateinit var rxPermissions: RxPermissions
    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = GeneralSettings.PREF_NAME
        addPreferencesFromResource(R.xml.settings_core)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        MVPBakery.builder<SettingsFragmentPresenter.View, SettingsFragmentPresenter>()
                .presenterFactory(InjectedPresenter(this))
                .presenterRetainer(ViewModelRetainer(this))
                .addPresenterCallback(PresenterInjectionCallback(this))
                .attach(this)
        super.onActivityCreated(savedInstanceState)
        rxPermissions = RxPermissions(this)

        (findPreference("core.autostart.call") as CheckBoxPreference).onPreferenceChangeListener = Preference.OnPreferenceChangeListener { pref: Preference?, newValue: Any? ->
            rxPermissions
                    .request(Manifest.permission.READ_PHONE_STATE)
                    .subscribe { granted ->
                        val isChecked = newValue as Boolean
                        val checkBoxPref = pref as CheckBoxPreference
                        if (isChecked && !granted) checkBoxPref.isChecked = false
                    }
            true
        }
    }

    override fun updateVersion(version: String) {
        findPreference("core.version").summary = version
    }
}
