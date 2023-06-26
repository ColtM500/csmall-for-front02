package cn.tedu.csmall.controller;

import cn.tedu.mall.common.restful.JsonResult;
import cn.tedu.mall.front.service.IFrontSkuService;
import cn.tedu.mall.pojo.product.vo.SkuStandardVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/front/sku")
public class FrontSkuController {

    @Autowired(required = false)
    private IFrontSkuService frontSkuService;

    @GetMapping("{/spuId}")
    public JsonResult<List<SkuStandardVO>> getSkuListBySpuId
            (@PathVariable("spuId") Long spuId) {
        List<SkuStandardVO> skus = frontSkuService.getSkus(spuId);
        return JsonResult.ok(skus);
    }
}
