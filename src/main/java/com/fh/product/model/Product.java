package com.fh.product.model;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_product")
public class Product {

    private Integer id;
    private String name;
    private Integer  brandId;
    private BigDecimal price;
    private Integer  status;
    private String  filePath;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date date;
    private Integer  isHot;
    private Integer stock;//库存
    private Integer  category1;
    private Integer  category2;
    private Integer  category3;




}
