package eu.thedarken.wldonate.main.ui.onboarding

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import dagger.android.AndroidInjector
import eu.darken.mvpbakery.injection.ManualInjector
import eu.thedarken.wldonate.R
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


    private val injector = object : ManualInjector<androidx.fragment.app.Fragment> {
        override fun get(instance: androidx.fragment.app.Fragment): AndroidInjector<androidx.fragment.app.Fragment> {
            @Suppress("UNCHECKED_CAST")
            return component as AndroidInjector<androidx.fragment.app.Fragment>
        }

        override fun inject(fragment: androidx.fragment.app.Fragment) {

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
