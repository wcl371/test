package com.fh.category.controller;

import com.fh.category.service.CategoryService;
import com.fh.common.Ignore;
import com.fh.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("category")
@Api(value = "分类接口",tags ={"分类接口aaa"})
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    private String name;
    private Integer age;

    @RequestMapping("queryList")
    @Ignore
    @ApiOperation(value = "查询分类")
    public ServerResponse queryList(){
        List<Map<String ,Object>> list=categoryService.queryList();
        return ServerResponse.success(list);
    }


    @RequestMapping(name="add",method = RequestMethod.POST)
    @Ignore
    @ApiOperation(value = "增加分类",notes = "add")
    public ServerResponse add(@ApiParam(name = "name", value = "用户名称")@RequestBody String name,
                              @ApiParam(name = "age", value = "用户年龄",required = true) Integer age){
        this.name = name;
        this.age = age;
        System.out.println("name"+name+"age"+age);
        return ServerResponse.success();
    }


}
