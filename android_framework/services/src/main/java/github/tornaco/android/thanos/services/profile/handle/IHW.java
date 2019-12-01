package github.tornaco.android.thanos.services.profile.handle;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;

import github.tornaco.android.thanos.services.S;

interface IHW {

    boolean enableWifi();

    boolean disableWifi();

    boolean isWifiEnabled();

    boolean enableLocation();

    boolean disableLocation();

    boolean isLocationEnabled();

    boolean enableBT();

    boolean disableBT();

    boolean isBTEnabled();

    class Impl implements IHW {

        private Context context;
        private S s;

        Impl(Context context, S s) {
            this.context = context;
            this.s = s;
        }

        @Override
        public boolean enableWifi() {
            @SuppressLint("WifiManagerPotentialLeak")
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            return wifiManager.setWifiEnabled(true);
        }

        @Override
        public boolean disableWifi() {
            @SuppressLint("WifiManagerPotentialLeak")
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            return wifiManager.setWifiEnabled(false);
        }

        @Override
        public boolean isWifiEnabled() {
            @SuppressLint("WifiManagerPotentialLeak")
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            return wifiManager.isWifiEnabled();
        }

        @Override
        public boolean enableLocation() {
            return setLocationEnabled(true);
        }

        @Override
        public boolean disableLocation() {
            return setLocationEnabled(false);
        }

        /**
         * Enable or disable location in settings.
         *
         * <p>This will attempt to enable/disable every type of location setting
         * (e.g. high and balanced power).
         *
         * <p>If enabling, a user consent dialog will pop up prompting the user to accept.
         * If the user doesn't accept, network location won't be enabled.
         *
         * @return true if attempt to change setting was successful.
         */
        private boolean setLocationEnabled(boolean enabled) {
            int currentUserId = UserHandle.getCallingUserId();
            if (isUserLocationRestricted(currentUserId)) {
                return false;
            }
            final ContentResolver cr = context.getContentResolver();
            // When enabling location, a user consent dialog will pop up, and the
            // setting won't be fully enabled until the user accepts the agreement.
            int mode = enabled
                    ? Settings.Secure.LOCATION_MODE_HIGH_ACCURACY : Settings.Secure.LOCATION_MODE_OFF;
            // QuickSettings always runs as the owner, so specifically set the settings
            // for the current foreground user.
            return Settings.Secure
                    .putIntForUser(cr, Settings.Secure.LOCATION_MODE, mode, currentUserId);
        }


        /**
         * Returns true if the current user is restricted from using location.
         */
        private boolean isUserLocationRestricted(int userId) {
            final UserManager um = (UserManager) context.getSystemService(Context.USER_SERVICE);
            return um.hasUserRestriction(
                    UserManager.DISALLOW_SHARE_LOCATION,
                    new UserHandle(userId));
        }

        /**
         * Returns true if location isn't disabled in settings.
         */
        @Override
        public boolean isLocationEnabled() {
            ContentResolver resolver = context.getContentResolver();
            // QuickSettings always runs as the owner, so specifically retrieve the settings
            // for the current foreground user.
            int mode = Settings.Secure.getIntForUser(resolver, Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF, UserHandle.getCallingUserId());
            return mode != Settings.Secure.LOCATION_MODE_OFF;
        }


        @Override
        public boolean enableBT() {
            BluetoothAdapter bluetoothadapter = BluetoothAdapter.getDefaultAdapter();
            return bluetoothadapter.enable();
        }

        @Override
        public boolean disableBT() {
            BluetoothAdapter bluetoothadapter = BluetoothAdapter.getDefaultAdapter();
            return bluetoothadapter.disable();
        }

        @Override
        public boolean isBTEnabled() {
            BluetoothAdapter bluetoothadapter = BluetoothAdapter.getDefaultAdapter();
            return bluetoothadapter.isEnabled();
        }
    }
}
