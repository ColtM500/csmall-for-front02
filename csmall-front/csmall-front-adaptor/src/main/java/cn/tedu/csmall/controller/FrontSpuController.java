package cn.tedu.csmall.controller;

import cn.tedu.mall.common.restful.JsonPage;
import cn.tedu.mall.common.restful.JsonResult;
import cn.tedu.mall.front.service.IFrontSpuService;
import cn.tedu.mall.pojo.product.vo.SpuListItemVO;
import cn.tedu.mall.pojo.product.vo.SpuStandardVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/front/spu")
public class FrontSpuController {

    @Autowired
    private IFrontSpuService spuService;

    /**
     * 根据categoryId查询spu列表
     */
    @GetMapping("/list/{categoryId}")
    @ApiOperation(value = "根据categoryId查询spu列表")
    public JsonResult<JsonPage<SpuListItemVO>> CateSpuList(
            @PathVariable("categoryId") Long categoryId,
            Integer page, Integer pageSize) {
        JsonPage<SpuListItemVO> spuListItemVOJsonPage = spuService.listSpuByCategoryId(categoryId, page, pageSize);
        return JsonResult.ok(spuListItemVOJsonPage);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "前台根据SpuID查询SPU信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "spu id", paramType = "path", required = true, dataType = "long")
    })
    public JsonResult<SpuStandardVO> getFrontSpuById(@PathVariable(value = "id") Long id) {
        SpuStandardVO spuStandardVO = spuService.getFrontSpuById(id);
        return JsonResult.ok(spuStandardVO);
    }
}
