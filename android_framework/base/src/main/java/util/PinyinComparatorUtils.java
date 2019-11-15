package util;

public class PinyinComparatorUtils {
    private static final PinyinComparator c = new PinyinComparator();

    public static int compare(String o1, String o2) {
        return c.compare(o1, o2);
    }
}
