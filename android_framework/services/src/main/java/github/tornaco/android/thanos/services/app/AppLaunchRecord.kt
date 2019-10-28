package github.tornaco.android.thanos.services.app


data class AppLaunchRecord(val pkg: String, val launchTime: Long) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AppLaunchRecord

        if (pkg != other.pkg) return false

        return true
    }

    override fun hashCode(): Int {
        return pkg.hashCode()
    }
}
