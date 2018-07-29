package eu.thedarken.wldonate.common.timber

import android.util.Log
import timber.log.Timber
import java.util.*


class BugsnagTree : Timber.Tree() {

    // Adding one to the initial size accounts for the add before remove.
    private val buffer = ArrayDeque<String>(BUFFER_SIZE + 1)

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        var message = message
        message = System.currentTimeMillis().toString() + " " + priorityToString(priority) + "/" + tag + ": " + message
        synchronized(buffer) {
            buffer.addLast(message)
            if (buffer.size > BUFFER_SIZE) {
                buffer.removeFirst()
            }
        }
    }

    fun update(error: com.bugsnag.android.Error) {
        synchronized(buffer) {
            var i = 1
            for (message in buffer) error.addToTab("Log", String.format(Locale.US, "%03d", i++), message)
            error.addToTab("Log", String.format(Locale.US, "%03d", i), Log.getStackTraceString(error.exception))
        }
    }

    companion object {
        private val BUFFER_SIZE = 200

        private fun priorityToString(priority: Int): String {
            when (priority) {
                Log.ERROR -> return "E"
                Log.WARN -> return "W"
                Log.INFO -> return "I"
                Log.DEBUG -> return "D"
                Log.VERBOSE -> return "V"
                else -> return priority.toString()
            }
        }
    }
}
