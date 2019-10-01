package github.tornaco.android.thanos.core.util;

import android.os.Message;
import android.util.Log;
import github.tornaco.java.common.util.ReflectionUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessagePoolSync {
    // private static final Object sPoolSync = new Object();
    public static final Object sPoolSync;

    static {
        sPoolSync = ReflectionUtils.getStaticObjectField(Message.class, "sPoolSync");
        Log.d("MessagePoolSync", "sPoolSync=" + sPoolSync);
    }
}
