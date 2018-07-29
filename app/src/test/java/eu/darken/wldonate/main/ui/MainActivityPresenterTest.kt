package eu.thedarken.wldonate.main.ui


import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule


class MainActivityPresenterTest {
    @get:Rule var rule: MockitoRule = MockitoJUnit.rule()
    @Mock lateinit var view: OnboardingActivityPresenter.View
    @Mock lateinit var someRepo: SomeRepo

    @Test
    fun testGetEmojis() {
        `when`(someRepo.isShowFragment).thenReturn(true)
        val mainPresenter = OnboardingActivityPresenter(someRepo)
        verify(someRepo, never()).isShowFragment
        mainPresenter.onBindChange(view)
        verify(someRepo).isShowFragment
        verify(view).showExampleFragment()
    }
}
