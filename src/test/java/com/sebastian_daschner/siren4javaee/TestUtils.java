package com.sebastian_daschner.siren4javaee;

import javax.ws.rs.ext.RuntimeDelegate;
import java.lang.reflect.Field;

public final class TestUtils {

    private TestUtils() {
        throw new UnsupportedOperationException();
    }

    public static void injectRuntimeDelegate(final RuntimeDelegate runtimeDelegate) {
        try {
            final Field delegateField = RuntimeDelegate.class.getDeclaredField("cachedDelegate");
            delegateField.setAccessible(true);
            delegateField.set(null, runtimeDelegate);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
