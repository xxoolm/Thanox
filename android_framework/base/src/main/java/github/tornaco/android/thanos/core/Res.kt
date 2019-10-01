package github.tornaco.android.thanos.core

abstract class Res {

    object Strings {

        const val STRING_WHILE_LIST_PACKAGES = "white_list_packages"
        const val STRING_WHILE_LIST_PACKAGES_HOOKS = "white_list_packages_hooks"
        const val STRING_APPLOCK_WHITE_LIST_ACTIVITY = "app_lock_white_list_activity"
        const val STRING_START_BLOCKER_CALLER_WHITELIST = "start_blocker_caller_whitelist"
        const val OP_REMIND_WHITELIST = "op_remind_whitelist"

        const val STRING_SERVICE_SILENCE_NOTIFICATION_CHANNEL = "service_silence_notification_channel"
        const val STRING_SERVICE_NOTIFICATION_OVERRIDE_THANOS = "service_notification_override_thanos"

        const val STRING_SERVICE_NOTIFICATION_TITLE_BG_RESTRICT_PROCESS_CHANGED =
            "service_notification_title_bg_restrict_process_changed"

        const val STRING_SERVICE_NOTIFICATION_CONTENT_BG_RESTRICT_PROCESS_CHANGED =
            "service_notification_content_bg_restrict_process_changed"

        const val STRING_SERVICE_NOTIFICATION_ACTION_BG_RESTRICT_PROCESS_CHANGED_CLEAR =
            "service_notification_action_bg_restrict_process_changed_clear"

        const val STRING_SERVICE_NOTIFICATION_ACTION_BG_RESTRICT_PROCESS_CHANGED_VIEW =
            "service_notification_action_bg_restrict_process_changed_view"

        const val STRING_SERVICE_NOTIFICATION_TITLE_OP_START_REMIND =
            "service_notification_title_op_start_remind"

        const val STRING_SERVICE_OP_LABEL_CAMERA = "service_op_label_camera"
        const val STRING_SERVICE_OP_LABEL_RECORD_AUDIO = "service_op_label_record_audio"
        const val STRING_SERVICE_OP_LABEL_PLAY_AUDIO = "service_op_label_play_audio"

    }

    object Drawables {
        const val DRAWABLE_ROCKET_2_FILL = "ic_rocket_fill_system"
        const val DRAWABLE_EYE_CLOSE_FILL = "ic_eye_close_fill_system"
        const val DRAWABLE_CAMERA_FILL = "ic_camera_fill"
        const val DRAWABLE_MIC_FILL = "ic_mic_2_fill"
        const val DRAWABLE_MUSIC_FILL = "module_ops_ic_music_fill"
    }

}

