package com.ruoyi.common.mapper;

import com.ruoyi.common.mapper.provider.SelectExampleProvider;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface SelectSingleOneMapper<T> {
    /**
     * 查询条件内的一条记录，倒序
     *
     * @param var1
     * @return
     */
    @SelectProvider(
            type = SelectExampleProvider.class,
            method = "dynamicSQL"
    )
    T selectSingleOneByExample(Object var1);
}