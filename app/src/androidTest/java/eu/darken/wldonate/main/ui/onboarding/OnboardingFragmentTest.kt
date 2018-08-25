package eu.thedarken.wldonate.main.ui.manager

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.v4.app.Fragment
import dagger.android.AndroidInjector
import eu.darken.mvpbakery.injection.ManualInjector
import eu.thedarken.wldonate.main.ui.onboarding.OnboardingFragment
import eu.thedarken.wldonate.main.ui.onboarding.OnboardingFragmentComponent
import eu.thedarken.wldonate.main.ui.onboarding.OnboardingFragmentPresenter
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import testhelper.FragmentTestRule

class OnboardingFragmentTest {

    @get:Rule var mockitoRule: MockitoRule = MockitoJUnit.rule()
    @get:Rule var fragmentRule = FragmentTestRule(OnboardingFragment::class.java)

    @Mock lateinit var presenter: OnboardingFragmentPresenter
    @Mock lateinit var component: OnboardingFragmentComponent


    private val injector = object : ManualInjector<Fragment> {
        override fun get(instance: Fragment): AndroidInjector<Fragment> {
            @Suppress("UNCHECKED_CAST")
            return component as AndroidInjector<Fragment>
        }

        override fun inject(fragment: Fragment) {

        }
    }

    @Before
    fun setUp() {
        doAnswer { invocation ->
            val exampleFragment = invocation.getArgument<OnboardingFragment>(0)
            exampleFragment.presenter = presenter
            null
        }.`when`(component).inject(any())
        `when`(component.presenter).thenReturn(presenter)

        doAnswer { invocationOnMock -> null }.`when`(presenter).onBindChange(any<OnboardingFragmentPresenter.View>())
        `when`(presenter.component).thenReturn(component)
        fragmentRule.setManualInjector(injector)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun testStartClick() {
        fragmentRule.launchActivity(null)

        onView(withId(R.id.action_start)).check(matches(isDisplayed()))

        onView(withId(R.id.action_start)).perform(click())
        verify(presenter).onFinishOnboarding()
    }

}
