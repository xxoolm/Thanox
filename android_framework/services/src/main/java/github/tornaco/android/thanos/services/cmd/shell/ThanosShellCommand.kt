package github.tornaco.android.thanos.services.cmd.shell

import android.os.RemoteException
import github.tornaco.android.thanos.core.IThanos
import github.tornaco.android.thanos.core.util.Timber
import java.io.FileDescriptor
import java.io.PrintWriter

class ThanosShellCommand(val thanos: IThanos.Stub) : ShellCommandCompat() {

    fun dump(
        fd: FileDescriptor?,
        fout: PrintWriter?,
        args: Array<out String>?
    ) {
        exec(thanos, null, fd, null, args)
    }

    @Throws(RemoteException::class)
    override fun onCommand(cmd: String?): Int {
        Timber.w("onCommand: $cmd")
        if (cmd == null) {
            handleDefaultCommands(cmd)
            return 0
        }
        if (cmd == "-h" || cmd == "help" || cmd == "-help") {
            onHelp()
            return 0
        }

        if (cmd == "version") {
            val pw = outPrintWriter
            pw.println("version name: ${thanos.versionName}")
            pw.println("fingerprint: ${thanos.fingerPrint()}")
            return 0
        }

        return 0
    }

    override fun onHelp() {
        val pw = outPrintWriter
        pw.println("Thanox commands:")
        pw.println("    help")
        pw.println("        Print this help text.")
        pw.println("")

        pw.println("    version")
        pw.println("        Show thanox core version info.")
        pw.println("")
    }
}
