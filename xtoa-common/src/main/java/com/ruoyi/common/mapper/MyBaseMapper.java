package com.ruoyi.common.mapper;

import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author chenmingwen
 */
@RegisterMapper
public interface MyBaseMapper<T> extends Mapper<T>, MySqlMapper<T>, SelectSingleOneMapper<T>, InsertOrUpdateMapper<T> {
}
