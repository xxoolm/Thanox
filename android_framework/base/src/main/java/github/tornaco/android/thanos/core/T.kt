package github.tornaco.android.thanos.core

import android.content.Context
import android.os.Environment
import android.os.UserHandle
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.core.app.activity.ActivityStackSupervisor
import java.io.File

object T {

    @JvmStatic
    fun baseServerDir(): File {
        val systemFile = File(Environment.getDataDirectory(), "system")
        return File(systemFile, "thanos")
    }

    @JvmStatic
    fun baseServerDataDir(): File {
        return File(baseServerDir(), "data/u/" + UserHandle.USER_SYSTEM)
    }

    @JvmStatic
    fun baseServerTmpDir(): File {
        return File(baseServerDir(), "tmp")
    }

    @JvmStatic
    fun baseServerLoggingDir(): File {
        return File(baseServerDir(), "logging")
    }

    @JvmStatic
    fun profileRulesDir(): File {
        return File(baseServerDataDir(), "rules")
    }

    @JvmStatic
    fun profileEnabledRulesRepoFile(): File {
        return File(profileRulesDir(), "enabled_rules.xml")
    }

    @JvmStatic
    fun globalRuleVarsRepoFile(): File {
        return File(profileRulesDir(), "global_rule_vars.xml")
    }

    @JvmStatic
    fun startBlockerRepoFile(): File {
        return File(baseServerDataDir(), "start_blocking_pkgs.xml")
    }

    @JvmStatic
    fun bgRestrictRepoFile(): File {
        return File(baseServerDataDir(), "bg_restrict_pkgs.xml")
    }

    @JvmStatic
    fun cleanUpOnTaskRemovalRepoFile(): File {
        return File(baseServerDataDir(), "clean_up_on_task_removed_pkgs.xml")
    }

    @JvmStatic
    fun recentTaskBlurRepoFile(): File {
        return File(baseServerDataDir(), "recent_task_blur_pkgs.xml")
    }

    @JvmStatic
    fun smartStandByRepoFile(): File {
        return File(baseServerDataDir(), "smart_stand_by_pkgs.xml")
    }

    @JvmStatic
    fun recentTaskExcludingSettingsRepoFile(): File {
        return File(baseServerDataDir(), "recent_task_excluding_settings.xml")
    }

    @JvmStatic
    fun appLockRepoFile(): File {
        return File(baseServerDataDir(), "app_lock_pkgs.xml")
    }

    @JvmStatic
    fun componentReplacementRepoFile(): File {
        return File(baseServerDataDir(), "component_replacements.xml")
    }

    @JvmStatic
    fun appOpsRepoFile(): File {
        return File(baseServerDataDir(), "app_ops.xml")
    }

    @JvmStatic
    fun pushChannelsFile(): File {
        return File(baseServerDataDir(), "push_channels.xml")
    }

    @JvmStatic
    fun privacyPkgSettingsFile(): File {
        return File(baseServerDataDir(), "priv_pkgs.xml")
    }

    @JvmStatic
    fun privacyDeviceIdFile(): File {
        return File(baseServerDataDir(), "priv_device_id.xml")
    }

    @JvmStatic
    fun privacyLine1NumFile(): File {
        return File(baseServerDataDir(), "priv_line1_num.xml")
    }

    @JvmStatic
    fun privacySimNumFile(): File {
        return File(baseServerDataDir(), "priv_sim_num.xml")
    }

    @JvmStatic
    fun privacyAndroidIdFile(): File {
        return File(baseServerDataDir(), "priv_android_id.xml")
    }

    @JvmStatic
    fun privacyInstalledPkgsReturnEmptyFile(): File {
        return File(baseServerDataDir(), "priv_installed_empty.xml")
    }

    @JvmStatic
    fun opRemindOpsFile(): File {
        return File(baseServerDataDir(), "reminding_ops.xml")
    }

    @JvmStatic
    fun opRemindPkgFile(): File {
        return File(baseServerDataDir(), "op_reminding_pkgs.xml")
    }

    @JvmStatic
    fun opTemplateFile(): File {
        return File(baseServerDataDir(), "op_template.xml")
    }

    @JvmStatic
    fun opSettingsFile(): File {
        return File(baseServerDataDir(), "op_settings.xml")
    }

    @JvmStatic
    fun screenOnNotificationPkgsFile(): File {
        return File(baseServerDataDir(), "screen_on_notification_pkgs.xml")
    }

    @JvmStatic
    fun serviceInstallName(): String {
        return Context.TV_INPUT_SERVICE
    }

    @JvmStatic
    fun serviceContextName(): String {
        return "thanos"
    }

    @JvmStatic
    fun serviceSilenceNotificationChannel(): String {
        return "dev.tornaco.notification.channel.id.Thanos-DEFAULT"
    }

    object Settings {
        // Server Settings.
        @JvmField
        val PREF_BY_PASS_SYSTEM_APPS_ENABLED =
            ThanosFeature("PREF_BY_PASS_SYSTEM_APPS_ENABLED", true)
        @JvmField
        val PREF_START_BLOCKER_ENABLED = ThanosFeature("PREF_START_BLOCKER_ENABLED", false)

        @JvmField
        val PREF_BG_RESTRICT_ENABLED = ThanosFeature("PREF_BG_RESTRICT_ENABLED", false)
        @JvmField
        val PREF_BG_TASK_CLEAN_UP_SKIP_AUDIO_FOCUSED =
            ThanosFeature("PREF_BG_TASK_CLEAN_UP_SKIP_AUDIO_FOCUSED", false)
        @JvmField
        val PREF_BG_TASK_CLEAN_UP_SKIP_NOTIFICATION =
            ThanosFeature("PREF_BG_TASK_CLEAN_UP_SKIP_NOTIFICATION", false)
        @JvmField
        val PREF_BG_TASK_CLEAN_UP_DELAY_MILLS =
            ThanosFeature("PREF_BG_TASK_CLEAN_UP_DELAY_MILLS", 0L /*Noop*/)
        @JvmField
        val PREF_BG_TASK_CLEAN_UP_SKIP_WHEN_HAS_RECENT_TASK =
            ThanosFeature("PREF_BG_TASK_CLEAN_UP_SKIP_WHEN_HAS_RECENT_TASK", false)

        @JvmField
        val PREF_CLEAN_UP_ON_TASK_REMOVED = ThanosFeature("PREF_CLEAN_UP_ON_TASK_REMOVED", false)

        @JvmField
        val PREF_SHOW_BG_RESTRICT_APPS_NOTIFICATION_ENABLED =
            ThanosFeature("PREF_SHOW_BG_RESTRICT_APPS_NOTIFICATION_ENABLED", false)

        @JvmField
        val PREF_RECENT_TASK_BLUR_ENABLED =
            ThanosFeature("PREF_RECENT_TASK_BLUR_ENABLED", false)

        @JvmField
        val PREF_SMART_STANDBY_ENABLED =
            ThanosFeature("PREF_SMART_STANDBY_ENABLED", false)

        @JvmField
        val PREF_ACTIVITY_TRAMPOLINE_ENABLED =
            ThanosFeature("PREF_ACTIVITY_TRAMPOLINE_ENABLED", false)

        @JvmField
        val PREF_SHOW_CURRENT_ACTIVITY_COMPONENT_ENABLED =
            ThanosFeature("PREF_SHOW_CURRENT_ACTIVITY_COMPONENT_ENABLED", false)

        @JvmField
        val PREF_APP_LOCK_ENABLED = ThanosFeature("PREF_APP_LOCK_ENABLED", false)
        @JvmField
        val PREF_APP_LOCK_METHOD =
            ThanosFeature("PREF_APP_LOCK_METHOD", ActivityStackSupervisor.LockerMethod.NONE)
        @JvmField
        val PREF_APP_LOCK_FP_ENABLED = ThanosFeature("PREF_APP_LOCK_FP_ENABLED", false)
        @JvmField
        val PREF_APP_LOCK_WORKAROUND_ENABLED =
            ThanosFeature("PREF_APP_LOCK_WORKAROUND_ENABLED", false)
        @JvmField
        val PREF_APP_LOCK_KEY_PREFIX_ =
            ThanosFeature("PREF_APP_LOCK_KEY_PREFIX_", "PREF_APP_LOCK_KEY_PREFIX_")
        @JvmField
        val PREF_APP_LOCK_WORKAROUND_DELAY = ThanosFeature("PREF_APP_LOCK_WORKAROUND_DELAY", 300L)

        @JvmField
        val PREF_PRIVACY_ENABLED = ThanosFeature("PREF_PRIVACY_ENABLED", false)

        @JvmField
        val PREF_PRIVACY_N_ENABLED = ThanosFeature("PREF_PRIVACY_N_ENABLED", true)

        @JvmField
        val PREF_SCREEN_ON_NOTIFICATION_ENABLED =
            ThanosFeature("PREF_SCREEN_ON_NOTIFICATION_ENABLED", false)

        @JvmField
        val PREF_LOGGING_ENABLED = ThanosFeature("PREF_LOGGING_ENABLED", true)

        @JvmField
        val PREF_AUTO_CONFIG_NEW_INSTALLED_APPS_ENABLED =
            ThanosFeature("PREF_AUTO_CONFIG_NEW_INSTALLED_APPS_ENABLED", false)

        @JvmField
        val PREF_PROFILE_ENABLED =
            ThanosFeature("PREF_PROFILE_ENABLED", false)

        @JvmField
        val PREF_OPS_ENABLED = ThanosFeature("PREF_OPS_ENABLED", false)

        @JvmField
        val PREF_FIRST_ACTIVATE =
            ThanosFeature("PREF_FIRST_ACTIVATE_" + BuildProp.FINGERPRINT, true)
    }

    object Actions {
        const val ACTION_FRONT_PKG_CHANGED = "thanox.a.front_pkg.changed"
        const val ACTION_FRONT_PKG_CHANGED_EXTRA_PACKAGE_FROM =
            "thanox.a.extra.front_activity.changed.pkg.from"
        const val ACTION_FRONT_PKG_CHANGED_EXTRA_PACKAGE_TO =
            "thanox.a.extra.front_activity.changed.pkg.to"

        const val ACTION_ACTIVITY_RESUMED = "thanox.a.activity.resumed"
        const val ACTION_ACTIVITY_RESUMED_EXTRA_COMPONENT_NAME =
            "thanox.a.activity.resumed.extra.name"
        const val ACTION_ACTIVITY_RESUMED_EXTRA_PACKAGE_NAME = "thanox.a.activity.resumed.extra.pkg"

        const val ACTION_PACKAGE_STOPPED = "thanox.a.package.stopped"
        const val ACTION_PACKAGE_STOPPED_EXTRA_PACKAGE_NAME = "thanox.a.package.stopped.extra.pkg"

        const val ACTION_TASK_REMOVED = "thanox.a.task.removed"
        const val ACTION_TASK_REMOVED_EXTRA_PACKAGE_NAME = "thanox.a.task.removed.pkg"
        const val ACTION_TASK_REMOVED_EXTRA_USER_ID = "thanox.a.task.removed.pkg.user.id"

        const val ACTION_RUNNING_PROCESS_VIEWER = "thanox.a.running_process.viewer"
        const val ACTION_RUNNING_PROCESS_CLEAR = "thanox.a.running_process.clear"

        const val ACTION_LOCKER_VERIFY_ACTION =
            "github.tornaco.practice.honeycomb.locker.action.VERIFY"
        const val ACTION_LOCKER_VERIFY_EXTRA_PACKAGE = "pkg"
        const val ACTION_LOCKER_VERIFY_EXTRA_REQUEST_CODE = "request_code"
    }

    object Tags {
        const val N_TAG_BG_RESTRICT_APPS_CHANGED = "thanox.n.tag.bg.restrict.apps.changed"
        const val N_TAG_PKG_PRIVACY_DATA_CHEATING = "thanox.n.tag.privacy.pkg.cheating"
        const val N_TAG_THANOX_ACTIVATED = "thanox.n.tag.thanox.core.activated"
    }
}
