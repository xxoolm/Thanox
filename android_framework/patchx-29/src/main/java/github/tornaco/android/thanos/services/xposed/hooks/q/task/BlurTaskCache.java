package github.tornaco.android.thanos.services.xposed.hooks.q.task;

import android.util.LruCache;

import util.Singleton;

/**
 * Created by Tornaco on 2018/5/9 16:30.
 * God bless no bug!
 */
class BlurTaskCache {

    private static final int MAX_ENTRY_SIZE = 12;

    private static final long EXPIRE_TIME_MILLS = 5 * 60 * 1000;

    private LruCache<String, BlurTask> mCache;

    private static final Singleton<BlurTaskCache> sMe
            = new Singleton<BlurTaskCache>() {
        @Override
        protected BlurTaskCache create() {
            return new BlurTaskCache();
        }
    };

    public static BlurTaskCache getInstance() {
        return sMe.get();
    }

    private BlurTaskCache() {
        mCache = new LruCache<>(MAX_ENTRY_SIZE);
    }

    public void put(String key, BlurTask task) {
        if (key != null && task != null) {
            mCache.put(key, task);
        }
    }

    public BlurTask get(String key) {

        BlurTask task = key == null ? null : mCache.get(key);
        if (task == null) return null;
        boolean isDirty = isDirtyTask(task);
        if (isDirty) {
            // Remove.
            mCache.remove(key);
            task.bitmap = null;
            task = null;
            return null;
        }
        return task;
    }

    private static boolean isDirtyTask(BlurTask task) {
        return task.bitmap == null || task.bitmap.isRecycled() || System.currentTimeMillis() - task.updateTimeMills > EXPIRE_TIME_MILLS;
    }
}
