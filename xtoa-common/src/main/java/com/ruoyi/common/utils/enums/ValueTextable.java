package com.ruoyi.common.utils.enums;

public interface ValueTextable<T> {

    T getValue();

    String getText();

    String getDescription();

    boolean isSelected();
}
