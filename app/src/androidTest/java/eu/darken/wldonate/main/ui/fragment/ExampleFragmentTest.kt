package eu.thedarken.wldonate.main.ui.fragment

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.v4.app.Fragment
import dagger.android.AndroidInjector
import eu.darken.mvpbakery.injection.ManualInjector
import eu.thedarken.wldonate.R
import eu.thedarken.wldonate.main.ui.fragment.ExampleFragment
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

class ExampleFragmentTest {

    @get:Rule var mockitoRule: MockitoRule = MockitoJUnit.rule()
    @get:Rule var fragmentRule = FragmentTestRule(ExampleFragment::class.java)

    @Mock lateinit var presenter: ExampleFragmentPresenter
    @Mock lateinit var component: ExampleFragmentComponent


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
            val exampleFragment = invocation.getArgument<ExampleFragment>(0)
            exampleFragment.presenter = presenter
            null
        }.`when`(component).inject(any())
        `when`(component.presenter).thenReturn(presenter)

        doAnswer { invocationOnMock -> null }.`when`(presenter).onBindChange(any<ExampleFragmentPresenter.View>())
        `when`(presenter.component).thenReturn(component)
        fragmentRule.setManualInjector(injector)
    }

    @After
    fun tearDown() {

    }

    @Test
    @Throws(Throwable::class)
    fun testShowEmoji() {
        fragmentRule.launchActivity(null)
        onView(withId(R.id.emoji_text)).check(matches(withText("Hello World!")))
        fragmentRule.runOnUiThread { fragmentRule.fragment.showEmoji("test") }
        onView(withId(R.id.emoji_text)).check(matches(withText("test")))
    }

    @Test
    fun testFABClick() {
        fragmentRule.launchActivity(null)

        onView(withId(R.id.fab)).perform(click())
        verify(presenter).onGetNewEmoji()
    }

}
