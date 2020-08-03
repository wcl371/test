package com.fh.category.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;
import java.util.Map;

@Mapper
public interface CategoryMapper extends BaseMapper<CategoryMapper> {
    List<Map<String, Object>> queryList();


}
