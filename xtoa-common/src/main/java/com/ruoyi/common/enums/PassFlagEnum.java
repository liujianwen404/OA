package com.ruoyi.common.enums;

import lombok.Getter;

/**
 *  枚举
 *
 * @author chenfei
 */
@Getter
public enum PassFlagEnum
{
    PASS_FLAG_YES("是",0),
    PASS_FLAG_NO("否",1);

    private String name;
    private Integer type;

    PassFlagEnum(String name, Integer type) {
        this.name=name;
        this.type=type;
    }

    public static String getValue(Integer type) {
        PassFlagEnum[] examFlagEnums = values();
        for (PassFlagEnum examFlagEnum : examFlagEnums) {
            if (examFlagEnum.getType().equals(type)) {
                return examFlagEnum.getName();
            }
        }
        return null;
    }

    public static Integer getByName(String name) {
        PassFlagEnum[] examFlagEnums = values();
        for (PassFlagEnum examFlagEnum : examFlagEnums) {
            if (examFlagEnum.getName().equals(name)) {
                return examFlagEnum.getType();
            }
        }
        return null;
    }


}
