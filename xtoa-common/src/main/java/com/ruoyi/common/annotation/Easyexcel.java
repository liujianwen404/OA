package com.ruoyi.common.annotation;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.WriteHandler;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Easyexcel {
    String name() default "";
    ExcelTypeEnum suffix() default ExcelTypeEnum.XLSX;
    String password() default "";
    String[] sheet() default {};
    boolean inMemory() default false;
    String template() default "";
    String[] include() default {};
    String[] exclude() default {};
    Class<? extends WriteHandler>[] writeHandler() default {};
    Class<? extends Converter>[] converter() default {};

}
