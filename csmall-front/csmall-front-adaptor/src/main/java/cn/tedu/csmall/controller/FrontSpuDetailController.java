package cn.tedu.csmall.controller;

import cn.tedu.mall.common.restful.JsonResult;
import cn.tedu.mall.front.service.IFrontSpuDetailService;
import cn.tedu.mall.pojo.product.vo.SpuDetailStandardVO;
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
@RequestMapping("/front/spu/detail")
public class FrontSpuDetailController {
    @Autowired(required = false)
    private IFrontSpuDetailService spuDetailService;

    @GetMapping("/{spuId}")
    @ApiOperation(value = "前台根据SPUID查询SPU DETAILS详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "spuId", value = "spu id", paramType = "path", required = true, dataType = "long")
    })
    public JsonResult<SpuDetailStandardVO> getSpuDetail(@PathVariable("spuId") Long spuId) {
        SpuDetailStandardVO spuDetailStandardVO = spuDetailService.getSpuDetail(spuId);
        return JsonResult.ok(spuDetailStandardVO);
    }
}
