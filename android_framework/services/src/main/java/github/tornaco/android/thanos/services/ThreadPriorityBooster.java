//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package github.tornaco.android.thanos.services;

import android.os.Process;
import github.tornaco.android.thanos.services.apihint.Beta;

@Beta
public class ThreadPriorityBooster {
    private volatile int mBoostToPriority;
    private final int mLockGuardIndex;

    private final ThreadLocal<ThreadPriorityBooster.PriorityState> mThreadState = new ThreadLocal<ThreadPriorityBooster.PriorityState>() {
        protected ThreadPriorityBooster.PriorityState initialValue() {
            return new ThreadPriorityBooster.PriorityState();
        }
    };

    public ThreadPriorityBooster(int boostToPriority, int lockGuardIndex) {
        this.mBoostToPriority = boostToPriority;
        this.mLockGuardIndex = lockGuardIndex;
    }

    public void boost() {
        int tid = Process.myTid();
        int prevPriority = Process.getThreadPriority(tid);
        ThreadPriorityBooster.PriorityState state = (ThreadPriorityBooster.PriorityState) this.mThreadState.get();
        if (state.regionCounter == 0) {
            state.prevPriority = prevPriority;
            if (prevPriority > this.mBoostToPriority) {
                Process.setThreadPriority(tid, this.mBoostToPriority);
            }
        }

        ++state.regionCounter;
    }

    public void reset() {
        ThreadPriorityBooster.PriorityState state = (ThreadPriorityBooster.PriorityState) this.mThreadState.get();
        --state.regionCounter;
        int currentPriority = Process.getThreadPriority(Process.myTid());
        if (state.regionCounter == 0 && state.prevPriority != currentPriority) {
            Process.setThreadPriority(Process.myTid(), state.prevPriority);
        }

    }

    protected void setBoostToPriority(int priority) {
        this.mBoostToPriority = priority;
        ThreadPriorityBooster.PriorityState state = (ThreadPriorityBooster.PriorityState) this.mThreadState.get();
        int tid = Process.myTid();
        int prevPriority = Process.getThreadPriority(tid);
        if (state.regionCounter != 0 && prevPriority != priority) {
            Process.setThreadPriority(tid, priority);
        }

    }

    private static class PriorityState {
        int regionCounter;
        int prevPriority;

        private PriorityState() {
        }
    }
}
