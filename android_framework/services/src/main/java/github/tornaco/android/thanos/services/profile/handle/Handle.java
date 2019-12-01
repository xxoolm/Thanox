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
    Activity {
        @Override
        public Object getHandle(Context context, S s) {
            return new IActivity.Impl(context, s);
        }
    },
    Power {
        @Override
        public Object getHandle(Context context, S s) {
            return new IPower.Impl(context, s);
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
    },
    Hw {
        @Override
        Object getHandle(Context context, S s) {
            return new IHW.Impl(context, s);
        }
    };

    abstract Object getHandle(Context context, S s);

    public static Facts inject(Context context, S s, Facts facts) {
        for (Handle handle : values()) {
            String name = handle.name().toLowerCase();
            if (facts.get(name) != null)
                throw new IllegalStateException("Dup handle name: " + name);
            facts.put(name, handle.getHandle(context, s));
        }
        return facts;
    }
}
