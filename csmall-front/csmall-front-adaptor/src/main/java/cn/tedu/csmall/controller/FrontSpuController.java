package cn.tedu.csmall.controller;

import cn.tedu.mall.common.restful.JsonPage;
import cn.tedu.mall.common.restful.JsonResult;
import cn.tedu.mall.front.service.IFrontSpuService;
import cn.tedu.mall.pojo.product.vo.SpuListItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/front/spu")
public class FrontSpuController {

    @Autowired(required = false)
    private IFrontSpuService spuService;

    /**
     * 根据categoryId查询spu列表
     */
    @GetMapping("/list/{categoryId}")
    public JsonResult<JsonPage<SpuListItemVO>> CateSpuList(
            @PathVariable("categoryId") Long categoryId,
            Integer page, Integer pageSize) {
        JsonPage<SpuListItemVO> spuListItemVOJsonPage = spuService.listSpuByCategoryId(categoryId, page, pageSize);
        return JsonResult.ok(spuListItemVOJsonPage);
    }
}
