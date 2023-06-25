package cn.tedu.csmall.service.impl;

import cn.tedu.csmall.mapper.FrontCategoryMapper;
import cn.tedu.mall.front.service.IFrontCategoryService;
import cn.tedu.mall.pojo.front.entity.FrontCategoryEntity;
import cn.tedu.mall.pojo.front.vo.FrontCategoryTreeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FrontCategoryServiceImpl implements IFrontCategoryService {

    //直接注入FrontCategoryMapper接口
    @Autowired
    private FrontCategoryMapper frontCategoryMapper;

    /**
     * 第一步：查询FrontCategoryMapper 所有分类对象 FrontCategoryEntity
     * 第二步：
     * 第三步：
     *
     * @return
     */
    @Override
    public FrontCategoryTreeVO categoryTree() {
        //这个方法 就通过映射文件 xml来解决属性和字段不一致的问题 手动映射Map标签
        List<FrontCategoryEntity> frontCategoryEntities =
                frontCategoryMapper.selectAll();
        //初始化解析
        FrontCategoryTreeVO frontCategoryTreeVO = initTree(frontCategoryEntities);
        //返回
        return frontCategoryTreeVO;
    }

    private FrontCategoryTreeVO initTree(List<FrontCategoryEntity> frontCategoryEntityList) {
        /**
         * 参数是包含所有分类的数组集合
         * 使用一个Map对象 key值是分类的id value值是一个list集合
         * 如果这个id对应的分类是父级分类
         * value值就是子集集合
         */
        //准备一个map对象
        Map<Long, List<FrontCategoryEntity>> categoriesMap = new HashMap<>();
        log.debug("当前拿到分类个数:{}", frontCategoryEntityList.size());
        //对全量分类 做遍历 按照key值是id, value是这个id的下级分类集合
        for (FrontCategoryEntity frontCategoryEntity : frontCategoryEntityList) {
            Long parentId = frontCategoryEntity.getParentId();
            //判断map是否有这个key值 有 直接将元素添加进value中
            //如果没有 就构建初始化数据
            if (!categoriesMap.containsKey(parentId)) {
                List<FrontCategoryEntity> nextLevelList = new ArrayList<>();
                nextLevelList.add(frontCategoryEntity);
                categoriesMap.put(parentId, nextLevelList);
            } else {
                List<FrontCategoryEntity> nextLevelList = categoriesMap.get(parentId);
                nextLevelList.add(frontCategoryEntity);
            }
        }
        return null;
    }

}
