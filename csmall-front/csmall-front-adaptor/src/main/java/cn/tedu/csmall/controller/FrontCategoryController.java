package cn.tedu.csmall.controller;

import cn.tedu.mall.common.restful.JsonResult;
import cn.tedu.mall.front.service.IFrontCategoryService;
import cn.tedu.mall.pojo.front.entity.FrontCategoryEntity;
import cn.tedu.mall.pojo.front.vo.FrontCategoryTreeVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/front/category")
public class FrontCategoryController {

    @Autowired
    private IFrontCategoryService frontCategoryService;

    /**3700
     * 封装一个前台分类树 Used
     * 需要父子关系
     */
    @GetMapping(value = "/all")
    @ApiOperation(value = "查询三级分类树")
    public JsonResult<FrontCategoryTreeVO<FrontCategoryEntity>> categoryTree() {
        //查询分类树
        FrontCategoryTreeVO<FrontCategoryEntity> frontCategoryTreeVO = frontCategoryService.categoryTree();
        return JsonResult.ok(frontCategoryTreeVO);
    }

}
