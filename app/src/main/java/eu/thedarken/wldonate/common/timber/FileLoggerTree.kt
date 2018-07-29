package eu.thedarken.wldonate.common.timber

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import eu.thedarken.wldonate.App
import eu.thedarken.wldonate.BuildConfig
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter


@SuppressLint("LogNotTimber")
class FileLoggerTree @SuppressLint("SetWorldReadable")
@Throws(IOException::class)
constructor(context: Context) : Timber.Tree() {
    private var logWriter: OutputStreamWriter? = null
    val logFile: File
    private val context: Context

    init {
        this.context = context.applicationContext

        val logDir = File(this.context.externalCacheDir, "logfiles")

        logDir.mkdirs()
        logFile = File(logDir, BuildConfig.APPLICATION_ID + "_logfile_" + System.currentTimeMillis() + ".log")

        if (logFile.createNewFile()) Log.i(TAG, "File logger writing to " + logFile.path)
        if (logFile.setReadable(true, false)) Log.i(TAG, "Debug run log read permission set")

        try {
            logWriter = OutputStreamWriter(FileOutputStream(logFile))
            logWriter!!.write("=== BEGIN ===\n")
            logWriter!!.write("Logfile: " + logFile + "\n")
            logWriter!!.flush()
            Log.i(TAG, "File logger started.")
            Runtime.getRuntime().addShutdownHook(Thread(Runnable { this.printEnd() }))
        } catch (e: IOException) {
            e.printStackTrace()

            logFile.delete()
            if (logWriter != null) logWriter!!.close()
        }

    }

    @Synchronized private fun printEnd() {
        if (logWriter == null) return
        try {
            logWriter!!.write("=== END ===\n")
            logWriter!!.close()
        } catch (ignore: IOException) {
        }

        Log.i(TAG, "File logger stopped.")
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (logWriter == null) return
        try {
            logWriter!!.write(System.currentTimeMillis().toString() + " " + priorityToString(priority) + "/" + tag + ": " + message + "\n")
            logWriter!!.flush()
        } catch (e: IOException) {
            Timber.tag(TAG).e(e)
            if (logWriter != null) {
                try {
                    logWriter!!.close()
                } catch (ignore: IOException) {
                }

            }
            logWriter = null
        }

    }

    companion object {
        internal val TAG = App.logTag("FileLoggerTree")

        private fun priorityToString(priority: Int): String {
            return when (priority) {
                Log.ERROR -> "E"
                Log.WARN -> "W"
                Log.INFO -> "I"
                Log.DEBUG -> "D"
                Log.VERBOSE -> "V"
                else -> priority.toString()
            }
        }
    }

}
