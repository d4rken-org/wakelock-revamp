package eu.thedarken.wldonate.main.ui.manager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import eu.thedarken.wldonate.IAPHelper
import eu.thedarken.wldonate.R

class DonateAdapter constructor(private val donations: List<IAPHelper.Upgrade>) : BaseAdapter() {
    override fun getCount(): Int = donations.size

    override fun getItem(position: Int): IAPHelper.Upgrade = donations[position]

    override fun getView(position: Int, _convertView: View?, parent: ViewGroup): View {
        var convertView = _convertView
        val holder: ViewHolder

        if (convertView == null) {
            holder = ViewHolder(parent)
            convertView = holder.view
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        holder.bind(getItem(position))
        return convertView
    }

    override fun getItemId(position: Int): Long = donations[position].hashCode().toLong()

    class ViewHolder(parent: ViewGroup) {
        @BindView(R.id.icon) lateinit var icon: ImageView
        @BindView(R.id.primary) lateinit var primary: TextView
        @BindView(R.id.secondary) lateinit var secondary: TextView

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.donation_adapter_line, parent, false)

        init {
            ButterKnife.bind(this, view)
        }

        fun bind(upgrade: IAPHelper.Upgrade) {
            icon.setImageDrawable(upgrade.getIcon(view.context))
            primary.text = upgrade.getLabel(view.context)
            secondary.text = upgrade.getDescription(view.context)
        }
    }
}