package eu.thedarken.wldonate.common.timber

import android.util.Log
import com.bugsnag.android.Event
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

    fun injectLog(event: Event) {
        synchronized(buffer) {
            var i = 1
            for (message in buffer) event.addMetadata("Log", String.format(Locale.US, "%03d", i++), message)
            event.addMetadata("Log", String.format(Locale.US, "%03d", i), Log.getStackTraceString(event.originalError))
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
