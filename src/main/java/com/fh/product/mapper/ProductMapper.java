package com.fh.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.product.model.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    @Select("select * from t_product order by id desc limit #{start},#{pageSize}")
    List<Product> queryList(@Param("start") long start, @Param("pageSize") long pageSize);

    @Select("select count(*) from t_product")
    long queryTotalCount();


    Long updateStock(@Param("count")int count, @Param("id")Integer id);
}
