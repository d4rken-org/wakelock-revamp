package eu.thedarken.wldonate

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout

import eu.darken.mvpbakery.injection.ManualInjector
import eu.darken.mvpbakery.injection.fragment.HasManualFragmentInjector

class FragmentTestActivity : AppCompatActivity(), HasManualFragmentInjector {

    internal var manualInjector: ManualInjector<Fragment>? = null

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = LinearLayout(this)
        view.id = 1
        setContentView(view)
    }

    fun setManualInjector(manualInjector: ManualInjector<Fragment>) {
        this.manualInjector = manualInjector
    }

    override fun supportFragmentInjector(): ManualInjector<Fragment>? {
        return manualInjector
    }

}
