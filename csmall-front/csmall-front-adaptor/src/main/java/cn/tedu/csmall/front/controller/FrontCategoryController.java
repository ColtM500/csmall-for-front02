package cn.tedu.csmall.front.controller;

import cn.tedu.mall.common.restful.JsonResult;
import cn.tedu.mall.front.service.IFrontCategoryService;
import cn.tedu.mall.pojo.front.entity.FrontCategoryEntity;
import cn.tedu.mall.pojo.front.vo.FrontCategoryTreeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/front/category")
public class FrontCategoryController {

    /**
     * 读取当前数据库中所有分类数据
     * 并且整理成树状
     * @return
     */
    @Autowired
    private IFrontCategoryService frontCategoryService;

    @GetMapping("/all")
    public JsonResult<FrontCategoryTreeVO<FrontCategoryEntity>> categoryTree(){
        FrontCategoryTreeVO frontCategoryTreeVO = frontCategoryService.categoryTree();
        JsonResult jsonResult = new JsonResult();
        jsonResult.setState(200);
        jsonResult.setMessage("ok");
        jsonResult.setData(frontCategoryTreeVO);
        return JsonResult.ok(frontCategoryTreeVO);
    }

}
