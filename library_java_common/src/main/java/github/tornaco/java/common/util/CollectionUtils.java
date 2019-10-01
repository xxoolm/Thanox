//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package github.tornaco.java.common.util;

import com.google.common.base.Preconditions;

import java.util.Collection;

public abstract class CollectionUtils {
    public static <C> void consumeRemaining(Collection<C> collection, Consumer<C> consumer) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(consumer);
        for (C c : collection) {
            consumer.accept(c);
        }
    }

    public static <C> void consumeRemaining(C[] dataArr, Consumer<C> consumer) {
        Preconditions.checkNotNull(dataArr);
        Preconditions.checkNotNull(consumer);
        for (C c : dataArr) {
            consumer.accept(c);
        }
    }

    public static <C> void consumeRemaining(Iterable<C> collection, Consumer<C> consumer) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(consumer);
        for (C c : collection) {
            consumer.accept(c);
        }
    }

    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> void remove(final Collection<T> source, final Collection<T> matches) {
        consumeRemaining(matches, new Consumer<T>() {
            @Override
            public void accept(T t) {
                source.remove(t);
            }
        });
    }
}
