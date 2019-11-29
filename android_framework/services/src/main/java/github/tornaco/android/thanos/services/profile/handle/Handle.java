package github.tornaco.android.thanos.services.profile.handle;

import android.content.Context;

import org.jeasy.rules.api.Facts;

import github.tornaco.android.thanos.services.S;

public enum Handle {

    Killer {
        @Override
        public Object getHandle(Context context, S s) {
            return new IKiller.Impl(context, s);
        }
    },
    Launcher {
        @Override
        public Object getHandle(Context context, S s) {
            return new ILauncher.Impl(context, s);
        }
    },
    Screen {
        @Override
        public Object getHandle(Context context, S s) {
            return new IScreen.Impl(context, s);
        }
    },
    Task {
        @Override
        public Object getHandle(Context context, S s) {
            return new ITask.Impl(context, s);
        }
    },
    Ui {
        @Override
        public Object getHandle(Context context, S s) {
            return new IUI.Impl(context, s);
        }
    };

    abstract Object getHandle(Context context, S s);

    public static Facts inject(Context context, S s, Facts facts) {
        for (Handle handle : values()) {
            facts.put(handle.name().toLowerCase(), handle.getHandle(context, s));
        }
        return facts;
    }
}
