package com.fh.category.service;

import com.fh.category.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;



    public List<Map<String, Object>> queryList() {
        List<Map<String, Object>> allList = categoryMapper.queryList();
        List<Map<String, Object>>  parentList =new ArrayList<>();
        for (Map map : allList) {
          if (map.get("pid").equals(0)){
              parentList.add(map);
          }
        }
        selectChildren(parentList,allList);
        return parentList;
    }





    private void selectChildren(List<Map<String, Object>> parentList, List<Map<String, Object>> allList) {
        for (Map<String, Object> pmap: parentList) {
            List<Map<String, Object>> childrenList =new ArrayList<>();
            for (Map<String, Object> amap: allList) {
                if (pmap.get("id").equals(amap.get("pid"))) {
                     childrenList.add(amap);
                }
            }
            if (childrenList != null && childrenList.size()>0) {
                 pmap.put("children",childrenList);
                 selectChildren(childrenList,allList);
            }

        }
    }
}
