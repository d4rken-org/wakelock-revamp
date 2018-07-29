package eu.thedarken.wldonate.main.ui.fragment

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import eu.darken.mvpbakery.base.MVPBakery
import eu.darken.mvpbakery.base.ViewModelRetainer
import eu.darken.mvpbakery.injection.InjectedPresenter
import eu.darken.mvpbakery.injection.PresenterInjectionCallback
import eu.thedarken.wldonate.R
import eu.thedarken.wldonate.common.Check
import eu.thedarken.wldonate.common.smart.SmartFragment
import javax.inject.Inject


class ExampleFragment : SmartFragment(), ExampleFragmentPresenter.View {

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.emoji_text) lateinit var emojiText: TextView

    @Inject lateinit var presenter: ExampleFragmentPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.example_fragment, container, false)
        addUnbinder(ButterKnife.bind(this, layout))
        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        MVPBakery.builder<ExampleFragmentPresenter.View, ExampleFragmentPresenter>()
                .presenterFactory(InjectedPresenter(this))
                .presenterRetainer(ViewModelRetainer(this))
                .addPresenterCallback(PresenterInjectionCallback(this))
                .attach(this)

        super.onActivityCreated(savedInstanceState)

        toolbar.setTitle(R.string.app_name)
        toolbar.subtitle = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_help -> {
                Snackbar.make(Check.notNull(view), R.string.app_name, Snackbar.LENGTH_SHORT).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    @OnClick(R.id.fab)
    fun onFabClicked(fab: View) {
        presenter.onGetNewEmoji()
    }

    override fun showEmoji(emoji: String) {
        emojiText.text = emoji
    }

    companion object {

        fun newInstance(): Fragment {
            return ExampleFragment()
        }
    }
}
