package cn.tedu.csmall.service.impl;

import cn.tedu.csmall.mapper.FrontCategoryMapper;
import cn.tedu.mall.front.service.IFrontCategoryService;
import cn.tedu.mall.pojo.front.entity.FrontCategoryEntity;
import cn.tedu.mall.pojo.front.vo.FrontCategoryTreeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
                FrontCategoryMapper.selectAll();
        //初始化解析
        FrontCategoryTreeVO frontCategoryTreeVO = initTree(frontCategoryEntities);
        //返回
        return frontCategoryTreeVO;
    }

    private FrontCategoryTreeVO initTree(List<FrontCategoryEntity> frontCategoryEntities){
        /**
         * 参数是包含所有分类的数组集合
         * 使用一个Map对象 key值是分类的id value值是一个list集合
         * 如果这个id对应的分类是父级分类
         * value值就是子集集合
         */
    }

}
