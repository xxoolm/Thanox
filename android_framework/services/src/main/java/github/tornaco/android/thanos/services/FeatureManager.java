package github.tornaco.android.thanos.services;

import android.os.Build;

import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.core.util.collection.ArrayMap;

public class FeatureManager {

    private static final ArrayMap<String, Integer> FEATURES_MIN_SDK_MAP = new ArrayMap<>();

    static {
        FEATURES_MIN_SDK_MAP.put(BuildProp.THANOX_FEATURE_PUSH_DELEGATE, Build.VERSION_CODES.M);
        FEATURES_MIN_SDK_MAP.put(BuildProp.THANOX_FEATURE_PROFILE_A11Y, Build.VERSION_CODES.O);
        FEATURES_MIN_SDK_MAP.put(BuildProp.THANOX_FEATURE_PROFILE, Build.VERSION_CODES.O);
        FEATURES_MIN_SDK_MAP.put(BuildProp.THANOX_FEATURE_APP_SMART_SERVICE_STOPPER, Build.VERSION_CODES.M);
        FEATURES_MIN_SDK_MAP.put(BuildProp.THANOX_FEATURE_APP_SMART_STAND_BY, Build.VERSION_CODES.M);
        FEATURES_MIN_SDK_MAP.put(BuildProp.THANOX_FEATURE_APP_TRAMPOLINE, Build.VERSION_CODES.M);
        FEATURES_MIN_SDK_MAP.put(BuildProp.THANOX_FEATURE_PRIVACY_FIELD_IMEI, Build.VERSION_CODES.O);
        FEATURES_MIN_SDK_MAP.put(BuildProp.THANOX_FEATURE_PRIVACY_FIELD_MEID, Build.VERSION_CODES.O);
    }

    public static boolean hasFeature(String feature) {
        if (!FEATURES_MIN_SDK_MAP.containsKey(feature)) {
            return false;
        }
        return FEATURES_MIN_SDK_MAP.get(feature) <= Build.VERSION.SDK_INT;
    }
}
