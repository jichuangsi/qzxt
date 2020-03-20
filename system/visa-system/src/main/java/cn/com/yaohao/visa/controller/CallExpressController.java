package cn.com.yaohao.visa.controller;

import cn.com.yaohao.visa.entity.ExpressReceipt;
import cn.com.yaohao.visa.entity.Logistics;
import cn.com.yaohao.visa.exception.PassportException;
import cn.com.yaohao.visa.model.LogisticsModel;
import cn.com.yaohao.visa.model.ResponseModel;
import cn.com.yaohao.visa.model.UserInfoForToken;
import cn.com.yaohao.visa.service.ShunFengService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/callExpress")
@Api("顺丰Api")
@CrossOrigin
public class CallExpressController {
    @Resource
    private ShunFengService shunFengService;

    @ApiOperation(value = "下单", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/sfAddOrder")
    public ResponseModel sfAddOrder(@ModelAttribute UserInfoForToken userInfo, @RequestBody Logistics model) throws PassportException {
        return ResponseModel.sucess("",shunFengService.addOrder(model));
    }

    @ApiOperation(value = "取消订单", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/sfExitOrder")
    public ResponseModel sfExitOrder(@ModelAttribute UserInfoForToken userInfo, @RequestBody Logistics model) {
        return ResponseModel.sucess("",shunFengService.exitOrder(model));
    }

    @ApiOperation(value = "订单查询", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/sfgetOrder")
    public ResponseModel sfgetOrder(@ModelAttribute UserInfoForToken userInfo, @RequestBody LogisticsModel model) {
        return ResponseModel.sucess("",shunFengService.getOrderSearchServiceRequestXml(model));
    }
}
