package eu.thedarken.wldonate.main.ui.manager

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import eu.thedarken.wldonate.R
import eu.thedarken.wldonate.main.core.locks.Lock
import eu.thedarken.wldonate.main.core.locks.Lock.Type.*


class LockAdapter(val callback: Callback) : RecyclerView.Adapter<LockAdapter.VH>() {
    private val data: ArrayList<Lock> = ArrayList()
    private val saved: HashSet<Lock.Type> = HashSet()

    interface Callback {
        fun onLockClicked(lock: Lock)

        fun onLockLongClicked(lock: Lock)
    }

    fun setData(data: List<Lock>?, saved: Collection<Lock.Type>?) {
        this@LockAdapter.data.clear()
        this@LockAdapter.saved.clear()
        data?.let { this@LockAdapter.data.addAll(it) }
        saved?.let { this@LockAdapter.saved.addAll(it) }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(parent, callback)
    }

    override fun onBindViewHolder(viewHolder: VH, position: Int) {
        viewHolder.bind(data[position], saved.contains(data[position].getType()))
    }

    class VH(parent: ViewGroup, private val callback: Callback)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.manager_lock_adapter_line, parent, false)) {
        @BindView(R.id.lock_label) lateinit var label: TextView
        @BindView(R.id.lock_description) lateinit var description: TextView
        @BindView(R.id.highlight) lateinit var highlight: View
        @BindView(R.id.lock_icon_cpu) lateinit var cpuIcon: View
        @BindView(R.id.lock_icon_keyboard) lateinit var keyboardIcon: View
        @BindView(R.id.lock_icon_screen) lateinit var screenIcon: View
        @BindView(R.id.lock_icon_wifi) lateinit var wifiIcon: View
        @BindView(R.id.lock_hint) lateinit var lockHint: TextView

        init {
            ButterKnife.bind(this, itemView)
        }

        private fun getContext(): Context {
            return itemView.context
        }

        @Suppress("IntroduceWhenSubject")
        fun bind(lock: Lock, saved: Boolean) {
            label.text = lock.getLabel(getContext())
            description.text = lock.getDescription(getContext())
            itemView.setOnClickListener { callback.onLockClicked(lock) }
            itemView.setOnLongClickListener {
                callback.onLockLongClicked(lock)
                return@setOnLongClickListener true
            }

            highlight.visibility = if (lock.isAcquired() || saved) View.VISIBLE else View.GONE
            when {
                lock.isAcquired() -> highlight.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.secondaryColor))
                saved -> highlight.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primaryColor))
            }

            wifiIcon.visibility = when {
                lock.getType() == WIFI_MODE_SCAN_ONLY || lock.getType() == WIFI_MODE_FULL_HIGH_PERF || lock.getType() == WIFI_MODE_FULL -> View.VISIBLE
                lock.getType() == PARTIAL_WAKE_LOCK -> View.VISIBLE
                else -> View.GONE
            }

            screenIcon.visibility = when {
                lock.getType() == SCREEN_BRIGHT_WAKE_LOCK || lock.getType() == SCREEN_DIM_WAKE_LOCK || lock.getType() == FULL_WAKE_LOCK -> View.VISIBLE
                else -> View.GONE
            }

            keyboardIcon.visibility = when {
                lock.getType() == FULL_WAKE_LOCK -> View.VISIBLE
                else -> View.GONE
            }
            cpuIcon.visibility = when {
                lock.getType() == PARTIAL_WAKE_LOCK -> View.VISIBLE
                else -> View.GONE
            }

            if (lock.getType() == PARTIAL_WAKE_LOCK) {
                lockHint.visibility = View.VISIBLE
                lockHint.setText(R.string.galaxy_note4_issue_hint)
            } else {
                lockHint.visibility = View.GONE
            }
        }
    }
}

