package eu.thedarken.wldonate.common.mvpbakery.base

import android.os.Bundle

interface StateListener {
    fun onRestoreState(inState: Bundle?)
    fun onSaveState(outState: Bundle)
}
