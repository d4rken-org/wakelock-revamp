package eu.thedarken.wldonate.common

import android.content.Context
import eu.thedarken.wldonate.ApplicationContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

class UUIDToken @Inject constructor(@ApplicationContext private var context: Context) {
    companion object {
        private val INSTALL_ID_FILENAME = "uuid-token"
        private val UUID_PATTERN = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")
    }

    private var id: String? = null

    @Synchronized fun id(): String {
        if (id == null) {
            val installIDFile = File(context.filesDir.path, INSTALL_ID_FILENAME)
            try {
                id = if (!installIDFile.exists()) writeInstallationFile(installIDFile)
                else readInstallationFile(installIDFile)

                if (!UUID_PATTERN.matcher(id!!).matches()) id = writeInstallationFile(installIDFile)
            } catch (e: Exception) {
                Timber.e(e)
                id = UUID.randomUUID().toString()
            }

        }
        return id!!
    }

    @Throws(IOException::class)
    private fun readInstallationFile(file: File): String {
        val f = RandomAccessFile(file, "r")
        val bytes = ByteArray(f.length().toInt())
        try {
            f.readFully(bytes)
        } finally {
            try {
                f.close()
            } catch (e: IOException) {
                Timber.e(e)
            }

        }
        return String(bytes)
    }

    @Throws(IOException::class)
    private fun writeInstallationFile(file: File): String {
        val out = FileOutputStream(file)
        val newUUID = UUID.randomUUID().toString()
        try {
            out.write(newUUID.toByteArray())
        } finally {
            try {
                out.close()
            } catch (e: IOException) {
                Timber.e(e)
            }

        }
        return newUUID
    }
}
