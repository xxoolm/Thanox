package github.tornaco.android.thanos.services.profile.handle;

import android.content.Context;

import github.tornaco.android.thanos.services.S;

interface IUI {

    void showShortToast(String msg);

    void showLongToast(String msg);

    void showDialog(String title, String msg, String yes);

    void showNotification(String title, String msg, boolean headsUp);

    class Impl implements IUI {
        private Context context;
        private S s;

        public Impl(Context context, S s) {
            this.context = context;
            this.s = s;
        }

        @Override
        public void showShortToast(String msg) {

        }

        @Override
        public void showLongToast(String msg) {

        }

        @Override
        public void showDialog(String title, String msg, String yes) {

        }

        @Override
        public void showNotification(String title, String msg, boolean headsUp) {

        }
    }
}
