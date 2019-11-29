package github.tornaco.android.thanos.services.profile.handle;

import android.content.Context;

import github.tornaco.android.thanos.services.S;

interface IScreen {

    void sleep(long delay);

    void wakeup(long delay);

    class Impl implements IScreen {
        private Context context;
        private S s;

        public Impl(Context context, S s) {
            this.context = context;
            this.s = s;
        }

        @Override
        public void sleep(long delay) {

        }

        @Override
        public void wakeup(long delay) {

        }
    }
}
