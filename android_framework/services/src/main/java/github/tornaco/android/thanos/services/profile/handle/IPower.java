package github.tornaco.android.thanos.services.profile.handle;

import android.content.Context;

import github.tornaco.android.thanos.services.S;

interface IPower {

    void sleep(long delay);

    void wakeup(long delay);

    class Impl implements IPower {
        private Context context;
        private S s;

        Impl(Context context, S s) {
            this.context = context;
            this.s = s;
        }

        @Override
        public void sleep(long delay) {
            s.getPowerService().goToSleep();
        }

        @Override
        public void wakeup(long delay) {
        }
    }
}
