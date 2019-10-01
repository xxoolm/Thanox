package github.tornaco.android.thanos.services.app.start;

import github.tornaco.android.thanos.core.app.start.StartRecord;
import github.tornaco.android.thanos.core.util.DevNull;
import github.tornaco.android.thanos.core.util.Timber;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class StartRecorder {

    private static final int MAX_ENTRY_SIZE = 1024;
    private final Map<String, LinkedList<StartRecord>> startRecordMap = new ConcurrentHashMap<>();

    private final AtomicLong allBlockedTimes = new AtomicLong(0L);
    private final Map<String, Long> pkgBlockedTimesMap = new ConcurrentHashMap<>();
    private final Map<String, Long> pkgAllowedTimesMap = new ConcurrentHashMap<>();

    public void add(StartRecord record) {
        Timber.v("%s", record);
        if (record.getPackageName() == null) return;

        // Increase logging times.
        if (!record.getResult().res) {
            DevNull.accept(allBlockedTimes.incrementAndGet());
            long pkgBlockTimes = pkgBlockedTimesMap.containsKey(record.getPackageName()) ? pkgBlockedTimesMap.get(record.getPackageName()) : 0L;
            pkgBlockTimes += 1;
            pkgBlockedTimesMap.put(record.getPackageName(), pkgBlockTimes);
        } else {
            long pkgAllowedTimes = pkgAllowedTimesMap.containsKey(record.getPackageName()) ? pkgAllowedTimesMap.get(record.getPackageName()) : 0L;
            pkgAllowedTimes += 1;
            pkgAllowedTimesMap.put(record.getPackageName(), pkgAllowedTimes);
        }

        LinkedList<StartRecord> recordList = startRecordMap.get(record.getPackageName());
        if (recordList == null) recordList = new LinkedList<>();
        // Trim.
        if (recordList.size() > MAX_ENTRY_SIZE) recordList.pop();
        recordList.addFirst(record);
        startRecordMap.put(record.getPackageName(), recordList);
    }

    public StartRecord[] getByPackageName(String packageName) {
        return startRecordMap.get(packageName).toArray(new StartRecord[0]);
    }

    public long getAllBlockedTimes() {
        return allBlockedTimes.get();
    }
}
