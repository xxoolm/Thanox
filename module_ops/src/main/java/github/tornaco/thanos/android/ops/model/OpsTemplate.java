package github.tornaco.thanos.android.ops.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import github.tornaco.android.thanos.core.secure.ops.AppOpsManager;
import github.tornaco.thanos.android.ops.R;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OpsTemplate {

    public final int[] ops;
    public final boolean[] showPerms;

    @StringRes
    public final int titleRes;
    @StringRes
    public final int summaryRes;
    @DrawableRes
    public final int iconRes;

    public final int sort;

    public static final OpsTemplate LOCATION_TEMPLATE = new OpsTemplate(
            new int[]{AppOpsManager.OP_COARSE_LOCATION,
                    AppOpsManager.OP_FINE_LOCATION,
                    AppOpsManager.OP_GPS,
                    AppOpsManager.OP_WIFI_SCAN,
                    AppOpsManager.OP_NEIGHBORING_CELLS,
                    AppOpsManager.OP_MONITOR_LOCATION,
                    AppOpsManager.OP_MONITOR_HIGH_POWER_LOCATION},
            new boolean[]{true,
                    true,
                    false,
                    false,
                    false,
                    false,
                    false,
            },
            R.string.module_ops_category_location,
            R.string.module_ops_category_location,
            R.drawable.ic_settings_fill,
            0
    );

    public static final OpsTemplate PERSONAL_TEMPLATE = new OpsTemplate(
            new int[]{AppOpsManager.OP_READ_CONTACTS,
                    AppOpsManager.OP_WRITE_CONTACTS,
                    AppOpsManager.OP_READ_CALL_LOG,
                    AppOpsManager.OP_WRITE_CALL_LOG,
                    AppOpsManager.OP_READ_CALENDAR,
                    AppOpsManager.OP_WRITE_CALENDAR,
                    AppOpsManager.OP_READ_CLIPBOARD,
                    AppOpsManager.OP_WRITE_CLIPBOARD},
            new boolean[]{true,
                    true,
                    true,
                    true,
                    true,
                    true,
                    false,
                    false},
            R.string.module_ops_category_personal,
            R.string.module_ops_category_personal,
            R.drawable.ic_settings_fill,
            1
    );

    public static final OpsTemplate MESSAGING_TEMPLATE = new OpsTemplate(
            new int[]{AppOpsManager.OP_READ_SMS,
                    AppOpsManager.OP_RECEIVE_SMS,
                    AppOpsManager.OP_RECEIVE_EMERGECY_SMS,
                    AppOpsManager.OP_RECEIVE_MMS,
                    AppOpsManager.OP_RECEIVE_WAP_PUSH,
                    AppOpsManager.OP_WRITE_SMS,
                    AppOpsManager.OP_SEND_SMS,
                    AppOpsManager.OP_READ_ICC_SMS,
                    AppOpsManager.OP_WRITE_ICC_SMS},
            new boolean[]{true,
                    true,
                    true,
                    true,
                    true,
                    true,
                    true,
                    true,
                    true},
            R.string.module_ops_category_message,
            R.string.module_ops_category_message,
            R.drawable.ic_settings_fill,
            2
    );

    public static final OpsTemplate MEDIA_TEMPLATE = new OpsTemplate(
            new int[]{AppOpsManager.OP_VIBRATE,
                    AppOpsManager.OP_CAMERA,
                    AppOpsManager.OP_RECORD_AUDIO,
                    AppOpsManager.OP_PLAY_AUDIO,
                    AppOpsManager.OP_TAKE_MEDIA_BUTTONS,
                    AppOpsManager.OP_TAKE_AUDIO_FOCUS,
                    AppOpsManager.OP_AUDIO_MASTER_VOLUME,
                    AppOpsManager.OP_AUDIO_VOICE_VOLUME,
                    AppOpsManager.OP_AUDIO_RING_VOLUME,
                    AppOpsManager.OP_AUDIO_MEDIA_VOLUME,
                    AppOpsManager.OP_AUDIO_ALARM_VOLUME,
                    AppOpsManager.OP_AUDIO_NOTIFICATION_VOLUME,
                    AppOpsManager.OP_AUDIO_BLUETOOTH_VOLUME,
                    AppOpsManager.OP_AUDIO_ACCESSIBILITY_VOLUME,
                    AppOpsManager.OP_MUTE_MICROPHONE},
            new boolean[]{false,
                    true,
                    true,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false},
            R.string.module_ops_category_media,
            R.string.module_ops_category_media,
            R.drawable.ic_settings_fill,
            3
    );

    public static final OpsTemplate DEVICE_TEMPLATE = new OpsTemplate(
            new int[]{AppOpsManager.OP_POST_NOTIFICATION,
                    AppOpsManager.OP_ACCESS_NOTIFICATIONS,
                    AppOpsManager.OP_CALL_PHONE,
                    AppOpsManager.OP_WRITE_SETTINGS,
                    AppOpsManager.OP_SYSTEM_ALERT_WINDOW,
                    AppOpsManager.OP_WAKE_LOCK,
                    AppOpsManager.OP_PROJECT_MEDIA,
                    AppOpsManager.OP_ACTIVATE_VPN,
                    AppOpsManager.OP_ASSIST_STRUCTURE,
                    AppOpsManager.OP_ASSIST_SCREENSHOT,
                    AppOpsManager.OP_CHANGE_WIFI_STATE},
            new boolean[]{false,
                    true,
                    true,
                    true,
                    true,
                    true,
                    false,
                    false,
                    false,
                    false,
                    true,
                    true,
                    true,
                    true},
            R.string.module_ops_category_device,
            R.string.module_ops_category_device,
            R.drawable.ic_settings_fill,
            4
    );

    public static final OpsTemplate RUN_IN_BACKGROUND_TEMPLATE = new OpsTemplate(
            new int[]{AppOpsManager.OP_RUN_IN_BACKGROUND},
            new boolean[]{false},
            R.string.module_ops_category_bg,
            R.string.module_ops_category_bg,
            R.drawable.ic_settings_fill,
            5
    );


    // this template should contain all ops which are not part of any other template in
    // ALL_TEMPLATES
    public static final OpsTemplate REMAINING_TEMPLATE = new OpsTemplate(
            new int[]{AppOpsManager.OP_GET_USAGE_STATS,
                    AppOpsManager.OP_TOAST_WINDOW,
                    AppOpsManager.OP_WRITE_WALLPAPER,
                    AppOpsManager.OP_READ_PHONE_STATE,
                    AppOpsManager.OP_ADD_VOICEMAIL,
                    AppOpsManager.OP_USE_SIP,
                    AppOpsManager.OP_PROCESS_OUTGOING_CALLS,
                    AppOpsManager.OP_USE_FINGERPRINT,
                    AppOpsManager.OP_BODY_SENSORS,
                    AppOpsManager.OP_READ_CELL_BROADCASTS,
                    AppOpsManager.OP_MOCK_LOCATION,
                    AppOpsManager.OP_READ_EXTERNAL_STORAGE,
                    AppOpsManager.OP_WRITE_EXTERNAL_STORAGE,
                    AppOpsManager.OP_TURN_SCREEN_ON,
                    AppOpsManager.OP_GET_ACCOUNTS},
            new boolean[]{true,
                    true,
                    true,
                    true,
                    true,
                    true,
                    true,
                    true,
                    true,
                    true,
                    true,
                    true,
                    true,
                    true,
                    true},
            R.string.module_ops_category_remaning,
            R.string.module_ops_category_remaning,
            R.drawable.ic_settings_fill,
            6
    );

    public static final OpsTemplate[] ALL_TEMPLATES = new OpsTemplate[]{
            LOCATION_TEMPLATE, PERSONAL_TEMPLATE, MESSAGING_TEMPLATE,
            MEDIA_TEMPLATE, DEVICE_TEMPLATE, RUN_IN_BACKGROUND_TEMPLATE
    };

    // this template contains all permissions grouped by templates
    public static final OpsTemplate[] ALL_PERMS_TEMPLATES = new OpsTemplate[]{
            LOCATION_TEMPLATE, PERSONAL_TEMPLATE, MESSAGING_TEMPLATE,
            MEDIA_TEMPLATE, DEVICE_TEMPLATE, RUN_IN_BACKGROUND_TEMPLATE,
            REMAINING_TEMPLATE
    };
}
