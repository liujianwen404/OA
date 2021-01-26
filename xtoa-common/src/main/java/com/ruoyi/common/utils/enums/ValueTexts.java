package com.ruoyi.common.utils.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ValueTexts {

    public static <T> Map<T, String> asMap(Iterable<? extends ValueTextable<T>> vts) {
        Map<T, String> result = new LinkedHashMap<>();
        for (ValueTextable<T> vt : vts) {
            result.put(vt.getValue(), vt.getText());
        }
        return result;
    }
}
