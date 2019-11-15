package util;

import java.io.Closeable;
import java.io.IOException;

public class IoUtils {
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {

            }
        }
    }
}
