package eu.thedarken.wldonate.main.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import eu.thedarken.wldonate.R
import eu.thedarken.wldonate.common.mvpbakery.base.MVPBakery
import eu.thedarken.wldonate.common.mvpbakery.base.ViewModelRetainer
import eu.thedarken.wldonate.common.mvpbakery.injection.InjectedPresenter
import eu.thedarken.wldonate.common.mvpbakery.injection.PresenterInjectionCallback
import eu.thedarken.wldonate.common.smart.SmartFragment
import javax.inject.Inject

class OnboardingFragment : SmartFragment(), OnboardingFragmentPresenter.View {

    @BindView(R.id.action_start) lateinit var startAction: View
    @BindView(R.id.legacy_icon) lateinit var legacyIcon: View
    @BindView(R.id.legacy_description) lateinit var legacyDescription: View
    @BindView(R.id.legacy_action) lateinit var legacyAction: View

    @Inject lateinit var presenter: OnboardingFragmentPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.onboarding_fragment, container, false)
        addUnbinder(ButterKnife.bind(this, layout))
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        legacyAction.setOnClickListener { presenter.onDownloadLegacy() }
        startAction.setOnClickListener { presenter.onFinishOnboarding() }

        super.onViewCreated(view, savedInstanceState)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        MVPBakery.builder<OnboardingFragmentPresenter.View, OnboardingFragmentPresenter>()
                .presenterFactory(InjectedPresenter(this))
                .presenterRetainer(ViewModelRetainer(this))
                .addPresenterCallback(PresenterInjectionCallback(this))
                .attach(this)

        super.onActivityCreated(savedInstanceState)
    }

    override fun showLegacyExplanation(show: Boolean) {
        legacyIcon.visibility = if (show) View.VISIBLE else View.GONE
        legacyDescription.visibility = if (show) View.VISIBLE else View.GONE
        legacyAction.visibility = if (show) View.VISIBLE else View.GONE
    }
}
