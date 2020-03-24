package cn.com.yaohao.visa.controller;

import cn.com.yaohao.visa.entity.ExpressReceipt;
import cn.com.yaohao.visa.entity.RemarksInformation;
import cn.com.yaohao.visa.exception.PassportException;
import cn.com.yaohao.visa.model.RequireVisaModel;
import cn.com.yaohao.visa.model.ResponseModel;
import cn.com.yaohao.visa.model.TBOrderModel;
import cn.com.yaohao.visa.model.UserInfoForToken;
import cn.com.yaohao.visa.service.ExpressReceiptService;
import cn.com.yaohao.visa.service.TBOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api("后台快件相关")
@CrossOrigin
@RestController
@RequestMapping("/backExpressReceipt")
public class ExpressReceiptController {
    @Resource
    private ExpressReceiptService expressReceiptService;
    @Resource
    private TBOrderService tbOrderService;

    @ApiOperation("快件签收")
    @ApiImplicitParams({})
    @PostMapping("/expressReceipt")
    public ResponseModel expressReceipt(@ModelAttribute UserInfoForToken userInfo,@RequestBody ExpressReceipt expressReceipt){
        try {
            expressReceiptService.saveExpressReceipt(userInfo,expressReceipt);
        }catch (PassportException e){
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation("更正快件")
    @ApiImplicitParams({})
    @PostMapping("/updateExpressReceipt")
    public ResponseModel updateExpressReceipt(@ModelAttribute UserInfoForToken userInfo,@RequestBody ExpressReceipt expressReceipt){
        try {
            expressReceiptService.updateExpressReceipt(userInfo,expressReceipt);
        }catch (PassportException e){
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation("添加异常")
    @ApiImplicitParams({})
    @PostMapping("/addUnusual")
    public ResponseModel addUnusual(@ModelAttribute UserInfoForToken userInfo,@RequestParam String expressId){
        try {
            expressReceiptService.addUnusualToExpressReceipt(userInfo,expressId);
        }catch (PassportException e){
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation("收件管理")
    @ApiImplicitParams({})
    @PostMapping("/getExpressReceiptByCondition")
    public ResponseModel<Page<ExpressReceipt>> getExpressReceiptByCondition(@ModelAttribute UserInfoForToken userInfo, @RequestBody RequireVisaModel model){
        return ResponseModel.sucess("",expressReceiptService.getRequirementsVisa(userInfo,model));
    }

    @ApiOperation("异常快件")
    @ApiImplicitParams({})
    @PostMapping("/getUnusualExpressReceiptByCondition")
    public ResponseModel<Page<ExpressReceipt>> getUnusualExpressReceiptByCondition(@ModelAttribute UserInfoForToken userInfo, @RequestBody RequireVisaModel model){
        return ResponseModel.sucess("",expressReceiptService.getUnusualRequirementsVisa(userInfo,model));
    }

    @ApiOperation("订单备注信息")
    @ApiImplicitParams({})
    @PostMapping("/expressReceiptRemark")
    public ResponseModel expressReceiptRemark(@ModelAttribute UserInfoForToken userInfo,@RequestBody RemarksInformation model,@RequestParam String expressId){
        try {
            expressReceiptService.addExpressRemark(userInfo,model,expressId);
        }catch (PassportException e){
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation("查看订单备注")
    @ApiImplicitParams({})
    @GetMapping("/getExpressReceiptRemark")
    public ResponseModel getExpressReceiptRemark(@ModelAttribute UserInfoForToken userInfo,@RequestParam String expressId,@RequestParam int pageNum,@RequestParam int pageSize){
        try {
            return ResponseModel.sucess("",expressReceiptService.getExpressRemark(userInfo,expressId,pageNum,pageSize));
        }catch (PassportException e){
            return ResponseModel.fail("",e.getMessage());
        }

    }

    @ApiOperation("根据手机匹配订单")
    @ApiImplicitParams({})
    @GetMapping("/getOrderByPhone")
    public ResponseModel getOrderByPhone(@ModelAttribute UserInfoForToken userInfo,@RequestParam(required = false) String phone,@RequestParam(required = false) String orderId){
        try {
            return ResponseModel.sucess("",tbOrderService.getTbOrder(phone,orderId));
        }catch (PassportException e){
            return ResponseModel.fail("",e.getMessage());
        }

    }
}
