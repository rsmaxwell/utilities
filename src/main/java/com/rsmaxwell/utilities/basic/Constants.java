package com.rsmaxwell.utilities.basic;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 *
 */
public class Constants {

    /**
     * @param c
     * @param value
     * @return
     */
    public static String lookup(Class<?> c, int value) {

        String found = null;

        for (Field f : c.getDeclaredFields()) {
            int mod = f.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isPublic(mod) && Modifier.isFinal(mod)) {
                if (f.getType() == Integer.TYPE) {
                    try {
                        int fieldValue = (Integer) f.get(null);

                        if (value == fieldValue) {
                            found = f.getName();
                        }
                    } catch (IllegalAccessException e) {
                        // skip
                    }
                }
            }
        }

        if (found == null) {
            found = Integer.toString(value);
        }

        return found;
    }
}
