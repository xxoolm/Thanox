package github.tornaco.android.thanos.services.profile.handle;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import github.tornaco.android.thanos.services.S;

interface IUI {

    void showShortToast(String msg);

    void showLongToast(String msg);

    void showDialog(String title, String msg, String yes);

    void showNotification(String title, String msg, boolean headsUp);

    class Impl implements IUI {
        private Context context;
        private S s;

        Impl(Context context, S s) {
            this.context = context;
            this.s = s;
        }

        @Override
        public void showShortToast(String msg) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void showLongToast(String msg) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }

        @Override
        public void showDialog(String title, String msg, String yes) {
            new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton(yes, null)
                    .setCancelable(false)
                    .create()
                    .show();
        }

        @Override
        public void showNotification(String title, String msg, boolean headsUp) {

        }
    }
}
