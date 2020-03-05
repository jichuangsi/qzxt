package cn.com.yaohao.visa.controller.advice;

import cn.com.yaohao.visa.entity.ExpressReceipt;
import cn.com.yaohao.visa.entity.PassportInformation;
import cn.com.yaohao.visa.entity.RemarksInformation;
import cn.com.yaohao.visa.exception.PassportException;
import cn.com.yaohao.visa.model.*;
import cn.com.yaohao.visa.service.VisaHandleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SessionAttributes
@RestController
@RequestMapping("/visaHandle")
@Api("签证处理")
@CrossOrigin
public class VisaHandleController {

    @Resource
    private VisaHandleService visaHandleService;

    @ApiOperation(value = "查询全部签证信息", notes = "")
    /*@ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })*/
    @GetMapping("/getAllVisa")
    public ResponseModel<Page<ExpressReceipt>> getAllVisa(@ModelAttribute UserInfoForToken userInfo, @RequestParam int pageNum, @RequestParam int pageSize) {

        return ResponseModel.sucess("",visaHandleService.getAllVisa(userInfo, pageNum, pageSize));
    }

    @ApiOperation(value = "条件查询签证信息", notes = "")
    /*@ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })*/
    @PostMapping("/getRequirementsVisa")
    public ResponseModel<Page<ExpressReceipt>> getRequirementsVisa(@ModelAttribute UserInfoForToken userInfo, @RequestBody RequireVisaModel model) {

        return ResponseModel.sucess("",visaHandleService.getRequirementsVisa(userInfo, model));
    }

    @ApiOperation(value = "护照录入", notes = "")
   /* @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })*/
    @PostMapping("/passportEntry")
    public ResponseModel<Boolean> passportEntry(@ModelAttribute UserInfoForToken userInfo,@RequestBody PassportModel model)throws PassportException{

        return ResponseModel.sucess("",visaHandleService.passportEntry(userInfo, model));
    }
    @ApiOperation(value = "护照照片录入", notes = "")
   /* @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })*/
    @PostMapping("/passportEntryPic")
    public ResponseModel<String> passportEntryPic(@ModelAttribute UserInfoForToken userInfo,@RequestParam MultipartFile file)throws PassportException,IOException {

        return ResponseModel.sucess("",visaHandleService.passportEntryPic(userInfo,file));
    }
    @ApiOperation(value = "查看备注", notes = "")
   /* @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })*/
    @PostMapping("/getVisaRemark")
    public ResponseModel<List<RemarksInformation>> getVisaRemark(@ModelAttribute UserInfoForToken userInfo,
                                                                 @RequestParam String passprtId){
        List<RemarksInformation> remark=new ArrayList<>();
        return ResponseModel.sucess("",visaHandleService.getVisaRemark(userInfo,passprtId));
    }

    @ApiOperation(value = "添加备注", notes = "")
   /* @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })*/
    @PostMapping("/addVisaRemark")
    public ResponseModel<Boolean> addVisaRemark(@ModelAttribute UserInfoForToken userInfo,
                                                @RequestBody RemarksInformation model,@RequestParam String passprtId)throws PassportException{

        return ResponseModel.sucess("",visaHandleService.addVisaRemark(userInfo, model, passprtId));
    }

    @ApiOperation(value = "根据签证id查看护照", notes = "")
   /* @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })*/
    @PostMapping("/getPassportById")
    public ResponseModel<List<PassportInformation>> getPassportById(
            @ModelAttribute UserInfoForToken userInfo, @RequestParam String passprtId)throws PassportException{
        return ResponseModel.sucess("",visaHandleService.getPassportById(userInfo, passprtId));
    }

   /* @ApiOperation(value = "签证消息验证", notes = "")
   *//* @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })*//*
    @PostMapping("/getVerification")
    public ResponseModel<Page<ValidationModel>> getVerification(@ModelAttribute UserInfoForToken userInfo,@RequestBody ValidationModel model){

        return ResponseModel.sucess("",pass);
    }*/


    @ApiOperation(value = "根据id,查看资料审核", notes = "")
   /* @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })*/
    @PostMapping("/getCheckPassportById")
    public ResponseModel<PassportModel> getCheckPassportById(@ModelAttribute UserInfoForToken userInfo, @RequestParam String passprtId){
        PassportModel model=new PassportModel();
        return ResponseModel.sucess("",model);
    }

    @ApiOperation(value = "根据签证id,资料审核", notes = "")
   /* @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })*/
    @PostMapping("/checkPassportById")
    public ResponseModel<Boolean> checkPassportById(@ModelAttribute UserInfoForToken userInfo,@RequestBody PassportCheckModel model){

        return ResponseModel.sucess("",true);
    }

    @ApiOperation(value = "待补充签证", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getProblemPassport")
    public ResponseModel<List<ValidationModel>> getProblemPassport(@ModelAttribute UserInfoForToken userInfo, @RequestBody ValidationModel model)throws PassportException{
        return ResponseModel.sucess("",visaHandleService.getProblemPassPort(userInfo,model ));
    }

    @ApiOperation(value = "查看护照信息（重审）", notes = "")
   @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getPassportByPassId")
    public ResponseModel<PassportModel> getPassportByPassId(@ModelAttribute UserInfoForToken userInfo, @RequestParam String id)throws PassportException{
        return ResponseModel.sucess("",visaHandleService.getPassportById2(userInfo,id ));
    }
}
