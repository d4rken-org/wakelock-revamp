package testhelper

import androidx.test.rule.ActivityTestRule
import eu.darken.mvpbakery.injection.ManualInjector
import eu.thedarken.wldonate.FragmentTestActivity
import junit.framework.Assert

class FragmentTestRule<FragmentT : androidx.fragment.app.Fragment>(private val fragmentClass: Class<FragmentT>) : ActivityTestRule<FragmentTestActivity>(FragmentTestActivity::class.java, true, false) {
    var fragment: FragmentT? = null
        private set
    internal lateinit var manualInjector: ManualInjector<androidx.fragment.app.Fragment>

    override fun afterActivityLaunched() {
        super.afterActivityLaunched()

        activity.setManualInjector(manualInjector)

        activity.runOnUiThread {
            try {
                //Instantiate and insert the fragment into the container layout
                val manager = activity.supportFragmentManager
                val transaction = manager.beginTransaction()
                fragment = fragmentClass.newInstance()

                transaction.replace(1, fragment!!)
                transaction.commitNow()
            } catch (e: InstantiationException) {
                Assert.fail(
                        String.format("%s: Could not insert %s into TestActivity: %s",
                                javaClass.simpleName,
                                fragmentClass.simpleName,
                                e.message)
                )
            } catch (e: IllegalAccessException) {
                Assert.fail(String.format("%s: Could not insert %s into TestActivity: %s", javaClass.simpleName, fragmentClass.simpleName, e.message))
            }
        }
    }

    fun setManualInjector(manualInjector: ManualInjector<androidx.fragment.app.Fragment>) {
        this.manualInjector = manualInjector
    }
}