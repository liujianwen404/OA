package com.ruoyi.common.enums;

import lombok.Getter;

/**
 *  培训方式枚举
 *
 * @author chenfei
 */
@Getter
public enum TeachingTypeEnum
{
    FACE_TO_FACE("面授",0),
    ONLINE_TEACHING("网授",1);

    private String name;
    private Integer type;

    TeachingTypeEnum(String name, Integer type) {
        this.name=name;
        this.type=type;
    }

    public static String getValue(Integer type) {
        TeachingTypeEnum[] examFlagEnums = values();
        for (TeachingTypeEnum examFlagEnum : examFlagEnums) {
            if (examFlagEnum.getType().equals(type)) {
                return examFlagEnum.getName();
            }
        }
        return null;
    }

    public static Integer getByName(String name) {
        TeachingTypeEnum[] examFlagEnums = values();
        for (TeachingTypeEnum examFlagEnum : examFlagEnums) {
            if (examFlagEnum.getName().equals(name)) {
                return examFlagEnum.getType();
            }
        }
        return null;
    }


}
