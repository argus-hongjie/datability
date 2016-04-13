package datability;

import java.sql.Connection;

public class Assert {

    private Assert() {
    }

    public static <T> T notNull(T ref, String errorMsg) {
        if (ref == null) {
            throw new IllegalArgumentException(errorMsg);
        }

        return ref;
    }
}
