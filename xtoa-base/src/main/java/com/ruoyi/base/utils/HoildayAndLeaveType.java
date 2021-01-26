package com.ruoyi.base.utils;

import tk.mybatis.mapper.util.StringUtil;

public class HoildayAndLeaveType {

    public static String getHoildayAndLeaveType(String typeStr) {
        if("调休".equals(typeStr) || "休息（调休）".equals(typeStr)){
            typeStr = "1";
        }
        if("事假".equals(typeStr)){
            typeStr = "2";
        }
        if("病假".equals(typeStr)){
            typeStr = "3";
        }
        if("年假".equals(typeStr)){
            typeStr = "4";
        }
        if("丧假".equals(typeStr)){
            typeStr = "5";
        }
        if("婚假".equals(typeStr)){
            typeStr = "6";
        }
        if("产假".equals(typeStr)){
            typeStr = "7";
        }
        if("陪产假".equals(typeStr)){
            typeStr = "8";
        }
        if("哺乳假".equals(typeStr)){
            typeStr = "9";
        }
        if("例假".equals(typeStr)){
            typeStr = "10";
        }

        if (StringUtil.isEmpty(typeStr)){
            throw  new RuntimeException("参数有误");
        }

        return typeStr;
    }


    public static String getHoildayAndLeaveStr(String type) {
        String typeStr = "";
        switch (type){
            case "1":
                typeStr = "调休";
                break;
            case "2":
                typeStr = "事假";
                break;
            case "3":
                typeStr = "病假";
                break;
            case "4":
                typeStr = "年假";
                break;
            case "5":
                typeStr = "丧假";
                break;
            case "6":
                typeStr = "婚假";
                break;
            case "7":
                typeStr = "产假";
                break;
            case "8":
                typeStr = "陪产假";
                break;
            case "9":
                typeStr = "哺乳假";
                break;
            case "10":
                typeStr = "例假";
                break;
        }
        return typeStr;
    }

}
