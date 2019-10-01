package github.tornaco.android.thanos.core.util;

import android.os.Message;
import github.tornaco.java.common.util.ReflectionUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageSetCallback {
    // Runnable callback;

    public static Message setCallback(Message message, Runnable r) {
        ReflectionUtils.setObjectField(message, "callback", r);
        return message;
    }
}
