package com.ruoyi.common.mapper;

import com.ruoyi.common.mapper.provider.InsertOrUpdateProvider;
import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * Create Time : 2019/12/31 14:40
 *
 * @author chenmingwen
 */
@RegisterMapper
public interface InsertOrUpdateMapper<T> {
    /**
     * 新增或者修改
     */
    @InsertProvider(type = InsertOrUpdateProvider.class, method = "dynamicSQL")
    int insertOrUpdate(T record);
}
