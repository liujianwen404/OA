package com.ruoyi.system.redisService.repository;

import com.ruoyi.system.domain.SysPost;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface SysPostRedisRepository extends QueryByExampleExecutor<SysPost> ,CrudRepository<SysPost,Long> {
}
