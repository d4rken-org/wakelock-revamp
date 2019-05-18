package eu.thedarken.wldonate

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import eu.darken.mvpbakery.injection.ManualInjector
import eu.darken.mvpbakery.injection.fragment.HasManualFragmentInjector

class FragmentTestActivity : AppCompatActivity(), HasManualFragmentInjector {

    private lateinit var manualInjector: ManualInjector<Fragment>

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

    override fun supportFragmentInjector(): ManualInjector<Fragment> {
        return manualInjector
    }

}
