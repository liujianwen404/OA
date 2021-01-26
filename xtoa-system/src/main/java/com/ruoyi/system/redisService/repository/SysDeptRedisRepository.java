package com.ruoyi.system.redisService.repository;

import com.ruoyi.system.domain.SysDept;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface SysDeptRedisRepository extends QueryByExampleExecutor<SysDept> ,CrudRepository<SysDept,Long> {
}
