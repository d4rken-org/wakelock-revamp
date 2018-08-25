package eu.thedarken.wldonate.main.ui


import android.app.Activity
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.v4.app.Fragment
import dagger.android.AndroidInjector
import eu.darken.mvpbakery.injection.ComponentSource
import eu.darken.mvpbakery.injection.ManualInjector
import eu.thedarken.wldonate.ExampleApplicationMock
import eu.thedarken.wldonate.main.ui.onboarding.OnboardingFragment
import eu.thedarken.wldonate.main.ui.onboarding.OnboardingFragmentComponent
import eu.thedarken.wldonate.main.ui.onboarding.OnboardingFragmentPresenter
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doAnswer
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class MainActivityTest {


    @get:Rule var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule var activityRule = ActivityTestRule(MainActivity::class.java, true, false)

    private lateinit var app: ExampleApplicationMock

    @Mock lateinit var mainPresenter: MainActivityPresenter
    @Mock lateinit var mainComponent: MainActivityComponent

    @Mock lateinit var fragmentInjector: ComponentSource<Fragment>
    @Mock lateinit var navigator: Navigator
    @Mock lateinit var onboardingFragmentPresenter: OnboardingFragmentPresenter
    @Mock lateinit var onboardingFragmentComponent: OnboardingFragmentComponent

    @Before
    fun setUp() {
        app = InstrumentationRegistry.getTargetContext().applicationContext as ExampleApplicationMock
        app.setActivityComponentSource(ActivityInjector())

        doAnswer { invocation ->
            val mainActivity = invocation.getArgument<MainActivity>(0)
            mainActivity.fragmentInjector = fragmentInjector
            mainActivity.navigator = navigator
            null
        }.`when`(mainComponent).inject(any())
        `when`(mainComponent.presenter).thenReturn(mainPresenter)
        `when`(mainPresenter.component).thenReturn(mainComponent)


        doAnswer { invocation -> null }.`when`(onboardingFragmentComponent).inject(any<OnboardingFragment>())
        `when`(fragmentInjector.get(any())).then { invocation -> onboardingFragmentComponent }
        `when`(onboardingFragmentComponent.presenter).thenReturn(onboardingFragmentPresenter)
        `when`(onboardingFragmentPresenter.component).thenReturn(onboardingFragmentComponent)
    }

    inner class ActivityInjector : ManualInjector<Activity> {

        override fun get(instance: Activity): AndroidInjector<Activity> {
            @Suppress("UNCHECKED_CAST")
            return mainComponent as AndroidInjector<Activity>
        }

        override fun inject(instance: Activity) {
            mainComponent.inject(instance as MainActivity)
        }

    }

    @Test
    @Throws(Throwable::class)
    fun testShowOnboarding() {
        activityRule.launchActivity(null)
    }
}
