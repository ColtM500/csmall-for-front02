package cn.tedu.csmall.service.impl;

import cn.tedu.csmall.mapper.FrontCategoryMapper;
import cn.tedu.mall.front.service.IFrontCategoryService;
import cn.tedu.mall.pojo.front.entity.FrontCategoryEntity;
import cn.tedu.mall.pojo.front.vo.FrontCategoryTreeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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
    public static final String FRONT_CAT_TREE_KEY="front:cat:tree";
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public FrontCategoryTreeVO categoryTree() {
        //不会频繁变动,而且没有必要引入超时
        //1 准备一个 三级分类树的key值
        //2 查询缓存,判断命中
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object o = valueOperations.get(FRONT_CAT_TREE_KEY);
        if (o == null){
            log.debug("缓存没命中");
            //这个方法,就通过映射文件,xml来解决属性和字段不一致的问题 手动映射Map标签
            List<FrontCategoryEntity> frontCategoryEntityList
                    =frontCategoryMapper.selectAll();//select * from pms_category
            //初始化解析
            FrontCategoryTreeVO frontCategoryTreeVO
                    =initTree(frontCategoryEntityList);
            //存放缓存
            valueOperations.set(FRONT_CAT_TREE_KEY,frontCategoryTreeVO);
            return frontCategoryTreeVO;
        }
        log.debug("缓存命中");
        return (FrontCategoryTreeVO) o;
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
        //当前map中 保存了所有同一父级分类的子集集合 但是没有办法区分2级分类 3级分类
        //一级分类的key=0， 根据一级分类集合遍历找到二级分类 嵌套遍历二级分类找到三级分类
        //准备返回对象
        FrontCategoryTreeVO frontCategoryTreeVO = new FrontCategoryTreeVO();
        //拿到一级分类
        List<FrontCategoryEntity> firstLevelCategories = categoriesMap.get(0L);
        frontCategoryTreeVO.setCategories(firstLevelCategories);
        //遍历一级分类 分类二级分类的list集合
        for (FrontCategoryEntity firstLevelCategory: firstLevelCategories) {
            //拿到一个一级分类的id
            Long firstLevelId = firstLevelCategory.getId();
            //到map取出这个id对应的二级分类集合
            List<FrontCategoryEntity> secondLevelCategories = categoriesMap.get(firstLevelId);
            //set给每一个一级分类的对象
            firstLevelCategory.setChildrens(secondLevelCategories);
            //循环遍历二级分类，放入三级分类的list集合
            for (FrontCategoryEntity secondLevelCategory : secondLevelCategories) {
                //拿到每一个二级分类的id
                Long secondLevelCategoryId = secondLevelCategory.getId();
                //到map取出这个二级id对应三级集合
                List<FrontCategoryEntity> thirdLevelCategories = categoriesMap.get(secondLevelCategoryId);
                secondLevelCategory.setChildrens(thirdLevelCategories);
            }
        }
        return frontCategoryTreeVO;
    }

}
