package com.rsmaxwell.utilities.basic;

import java.util.List;

/**
 *
 */
public class ListUtils {

    /**
     * @param list
     * @return int array
     */
    public static int[] toIntArray(List<Integer> list) {
        int[] ret = new int[list.size()];
        int i = 0;
        for (Integer e : list)
            ret[i++] = e.intValue();
        return ret;
    }
}
