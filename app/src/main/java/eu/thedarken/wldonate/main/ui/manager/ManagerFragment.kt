package eu.thedarken.wldonate.main.ui.manager

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import eu.thedarken.wldonate.R
import eu.thedarken.wldonate.common.mvpbakery.base.MVPBakery
import eu.thedarken.wldonate.common.mvpbakery.base.ViewModelRetainer
import eu.thedarken.wldonate.common.mvpbakery.injection.InjectedPresenter
import eu.thedarken.wldonate.common.mvpbakery.injection.PresenterInjectionCallback
import eu.thedarken.wldonate.common.smart.SmartFragment
import eu.thedarken.wldonate.main.core.locks.Lock
import javax.inject.Inject


class ManagerFragment : SmartFragment(), ManagerFragmentPresenter.View {

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.recyclerview)
    lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView

    @BindView(R.id.info_card_paused_desc)
    lateinit var pauseDesc: TextView

    @BindView(R.id.info_card_paused)
    lateinit var pauseBox: View

    @Inject
    @JvmField
    var presenter: ManagerFragmentPresenter? = null

    private var donationLevel: Float = -1.0f

    private val adapter: LockAdapter = LockAdapter(object : LockAdapter.Callback {
        override fun onLockClicked(lock: Lock) {
            presenter?.onToggleLock(lock)
        }

        override fun onLockLongClicked(lock: Lock) {

        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.manager_fragment, container, false)
        addUnbinder(ButterKnife.bind(this, layout))
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val dividerDecorator = androidx.recyclerview.widget.DividerItemDecoration(requireActivity(), androidx.recyclerview.widget.DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerDecorator)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        pauseBox.setOnClickListener { View.OnClickListener { presenter?.onPauseLocks() } }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        MVPBakery.builder<ManagerFragmentPresenter.View, ManagerFragmentPresenter>()
                .presenterFactory(InjectedPresenter(this))
                .presenterRetainer(ViewModelRetainer(this))
                .addPresenterCallback(PresenterInjectionCallback(this))
                .attach(this)

        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.app_name)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_manager, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val pause = menu.findItem(R.id.action_pause)
        pause.isVisible = presenter?.locksState != ManagerFragmentPresenter.LocksState.NONE
        pause.icon = AppCompatResources.getDrawable(requireContext(), if (presenter?.locksState == ManagerFragmentPresenter.LocksState.ACTIVE) R.drawable.ic_pause_circle_filled_white_24dp else R.drawable.ic_play_circle_filled_white_24dp)
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                presenter?.onShowSettings()
                true
            }
            R.id.action_pause -> {
                presenter?.onPauseLocks()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showLocks(locks: List<Lock>, saved: Collection<Lock.Type>) {
        adapter.setData(locks, saved)
        adapter.notifyDataSetChanged()
    }

    override fun updatePausedInfo(locksState: ManagerFragmentPresenter.LocksState, onCallOption: Boolean) {
        pauseBox.visibility = if (locksState == ManagerFragmentPresenter.LocksState.PAUSED) View.VISIBLE else View.GONE
        val sb = StringBuilder()
        sb.append("• ").append(getString(R.string.pause_description_manually_resume))
        if (onCallOption) sb.append("\n").append("• ").append(getString(R.string.pause_description_call_only))
        pauseDesc.text = sb.toString()
        requireActivity().invalidateOptionsMenu()
    }
}
