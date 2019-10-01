package github.tornaco.android.thanos.services.backup

import android.os.IBinder
import android.os.ParcelFileDescriptor
import android.util.Log
import com.google.common.io.Files
import github.tornaco.android.thanos.core.T
import github.tornaco.android.thanos.core.backup.IBackupAgent
import github.tornaco.android.thanos.core.backup.IBackupCallback
import github.tornaco.android.thanos.core.backup.IFileDescriptorConsumer
import github.tornaco.android.thanos.core.backup.IFileDescriptorInitializer
import github.tornaco.android.thanos.core.util.*
import github.tornaco.android.thanos.services.S
import github.tornaco.android.thanos.services.SystemService
import github.tornaco.java.common.util.IoUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class BackupAgentService(s: S) : SystemService(), IBackupAgent {

    override fun performBackup(
        init: IFileDescriptorInitializer?,
        domain: String?,
        path: String?,
        callback: IBackupCallback?
    ) {
        Timber.d("performBackup...")

        Preconditions.checkNotNull(init)
        Preconditions.checkNotNull(callback)

        // Create tmp dir.
        val tmpDir = T.baseServerTmpDir()

        try {
            @Suppress("UnstableApiUsage")
            Files.createParentDirs(tmpDir)
            Timber.d("tmpDir: $tmpDir")
        } catch (e: IOException) {
            callback!!.onFail(e.localizedMessage)
            Timber.e("createParentDirs fail : " + Log.getStackTraceString(e))
            return
        }

        // Zip all subFiles.
        val startTimeMills = System.currentTimeMillis()
        val name = "Thanox-Backup-" + DateUtils.formatForFileName(startTimeMills) + ".zip"

        try {
            ZipUtils.zip(T.baseServerDataDir().absolutePath, tmpDir.absolutePath, name)
            val zipFile = File(tmpDir, name)
            Timber.d("zipFile: $zipFile")
            val relativePath = toRelativePath(zipFile)
            Timber.d("relativePath: $relativePath")

            init!!.initParcelFileDescriptor(relativePath, relativePath, object : IFileDescriptorConsumer.Stub() {
                override fun acceptAppParcelFileDescriptor(pfd: ParcelFileDescriptor?) {
                    try {
                        if (pfd == null) {
                            callback!!.onFail("ParcelFileDescriptor is null")
                            return
                        }
                        @Suppress("UnstableApiUsage")
                        Files.asByteSource(zipFile)
                            .copyTo(FileOutputStream(pfd.fileDescriptor))
                        Timber.e("performBackup subFile complete: $zipFile")
                        callback!!.onProgress(zipFile.name)
                        callback.onBackupFinished(domain, relativePath)
                    } catch (e: IOException) {
                        Timber.e("IOException performBackup subFile: " + Log.getStackTraceString(e))
                        callback!!.onFail(e.localizedMessage)
                        Timber.e("acceptAppParcelFileDescriptor fail : " + Log.getStackTraceString(e))
                    } finally {
                        FileUtils.deleteDirQuiet(tmpDir)
                        IoUtils.closeQuietly(pfd)
                        Timber.e("IBackupAgent, deleteDirQuiet : $tmpDir")
                    }
                }

            })
        } catch (e: Throwable) {
            callback!!.onFail(e.localizedMessage)
            FileUtils.deleteDirQuiet(tmpDir)
            Timber.e("backup fail : " + Log.getStackTraceString(e))
            Timber.e("deleteDirQuiet : $tmpDir")
        }
    }

    @Suppress("UnstableApiUsage")
    override fun performRestore(
        pfd: ParcelFileDescriptor?,
        domain: String?,
        path: String?,
        callback: IBackupCallback?
    ) {
        Timber.d("performRestore...")
        Preconditions.checkNotNull(pfd)
        Preconditions.checkNotNull(callback)

        // Create tmp dir.
        val tmpDir = T.baseServerTmpDir()
        val tmpZipFile = File(tmpDir, "thanox_restore_file.zip")
        Timber.d("zipFile : $tmpZipFile")
        // Copy to tmp.zip.
        try {
            Files.createParentDirs(tmpZipFile)
            Files.asByteSink(tmpZipFile)
                .writeFrom(FileInputStream(pfd!!.fileDescriptor))
        } catch (e: IOException) {
            Timber.e("IOException copy zip to tmp: " + Log.getStackTraceString(e))
            callback!!.onFail(e.localizedMessage)
            return
        } finally {
            IoUtils.closeQuietly(pfd)
        }
        try {
            ZipUtils.unzip(tmpZipFile.absolutePath, T.baseServerDataDir().absolutePath, false)
            callback!!.onRestoreFinished(domain, path)
        } catch (e: Exception) {
            Timber.e("IOException unzip to tmp: " + Log.getStackTraceString(e))
            callback!!.onFail(e.localizedMessage)
        } finally {
            FileUtils.deleteDirQuiet(tmpDir)
        }
    }

    override fun asBinder(): IBinder {
        return Noop.notSupported()
    }

    private fun toRelativePath(subFile: File): String {
        val dataDir = T.baseServerDir()
        return subFile.absolutePath.replace(dataDir.absolutePath, "", true)
    }
}
