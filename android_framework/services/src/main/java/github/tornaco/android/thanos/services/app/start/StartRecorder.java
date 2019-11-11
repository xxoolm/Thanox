package github.tornaco.android.thanos.services.app.start;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import github.tornaco.android.thanos.core.app.start.StartRecord;
import github.tornaco.android.thanos.core.util.DevNull;
import github.tornaco.android.thanos.core.util.Timber;

public class StartRecorder {

    private static final int MAX_ENTRY_SIZE = 1024;
    private final Map<String, LinkedList<StartRecord>> startBlockRecordMap = new ConcurrentHashMap<>();

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

            // Recording.
            LinkedList<StartRecord> recordList = startBlockRecordMap.get(record.getPackageName());
            if (recordList == null) recordList = new LinkedList<>();
            // Trim.
            if (recordList.size() > MAX_ENTRY_SIZE) recordList.pop();
            recordList.addFirst(record);
            startBlockRecordMap.put(record.getPackageName(), recordList);
        } else {
            long pkgAllowedTimes = pkgAllowedTimesMap.containsKey(record.getPackageName()) ? pkgAllowedTimesMap.get(record.getPackageName()) : 0L;
            pkgAllowedTimes += 1;
            pkgAllowedTimesMap.put(record.getPackageName(), pkgAllowedTimes);
        }
    }

    public StartRecord[] getByPackageName(String packageName) {
        LinkedList<StartRecord> records = startBlockRecordMap.get(packageName);
        if (records == null) return new StartRecord[0];
        return records.toArray(new StartRecord[0]);
    }

    public long getStartRecordBlockedCountByPackageName(String pkgName) {
        Long b = pkgBlockedTimesMap.get(pkgName);
        return (b == null ? 0L : b);
    }

    public String[] getStartRecordBlockedPackages() {
        return startBlockRecordMap.keySet().toArray(new String[0]);
    }

    public long getAllBlockedTimes() {
        return allBlockedTimes.get();
    }
}
