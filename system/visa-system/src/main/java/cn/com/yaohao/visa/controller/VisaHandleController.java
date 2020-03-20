package cn.com.yaohao.visa.controller;

import cn.com.yaohao.visa.entity.ExpressReceipt;
import cn.com.yaohao.visa.entity.RemarksInformation;
import cn.com.yaohao.visa.exception.PassportException;
import cn.com.yaohao.visa.model.*;
import cn.com.yaohao.visa.model.backuser.RoleAddModel;
import cn.com.yaohao.visa.service.VisaHandleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
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
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })

    @PostMapping("/passportEntry")
    public ResponseModel<Boolean> passportEntry(@ModelAttribute UserInfoForToken userInfo,@RequestBody PassportModel model)throws PassportException{

        return ResponseModel.sucess("",visaHandleService.passportEntry(userInfo, model));
    }

    @ApiOperation(value = "护照照片录入", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/passportEntryPic")
    public ResponseModel passportEntryPic(@ModelAttribute UserInfoForToken userInfo,@RequestParam MultipartFile file){
        try {
            visaHandleService.localUploadPic(userInfo,file);
            return ResponseModel.sucessWithEmptyData("");
        }catch (Exception e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "护照修改", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })

    @PostMapping("/updatePassport")
    public ResponseModel updatePassport(@ModelAttribute UserInfoForToken userInfo,@RequestBody PassportModel model)throws PassportException{
        try {
            visaHandleService.updatePassPort(userInfo, model.getInformation());
            return ResponseModel.sucessWithEmptyData("");
        }catch (PassportException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }


    @ApiOperation(value = "查看备注", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getVisaRemark")
    public ResponseModel<List<RemarksInformation>> getVisaRemark(@ModelAttribute UserInfoForToken userInfo,
                                                                 @RequestParam String passprtId){
        List<RemarksInformation> remark=new ArrayList<>();
        return ResponseModel.sucess("",visaHandleService.getVisaRemark(userInfo,passprtId));
    }

    @ApiOperation(value = "添加备注", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addVisaRemark")
    public ResponseModel<Boolean> addVisaRemark(@ModelAttribute UserInfoForToken userInfo,
                                                @RequestBody RemarksInformation model,@RequestParam String passprtId)throws PassportException{

        return ResponseModel.sucess("",visaHandleService.addVisaRemark(userInfo, model, passprtId));
    }

    @ApiOperation(value = "根据快件id查看护照", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getPassportByExpressReceiptId")
    public ResponseModel getPassportByExpressReceiptId(@ModelAttribute UserInfoForToken userInfo, @RequestBody RequireVisaModel model)throws PassportException{
        try {
            return ResponseModel.sucess("",visaHandleService.getPassportById(userInfo, model.getExpressReceiptId(),model.getPageNum(),model.getPageSize()));
        }catch (PassportException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据id,查看资料审核", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getCheckPassportById")
    public ResponseModel<PassportModel> getCheckPassportById(@ModelAttribute UserInfoForToken userInfo, @RequestParam String passprtId){
        PassportModel model=new PassportModel();
        return ResponseModel.sucess("",model);
    }

    @ApiOperation(value = "根据签证id,资料审核", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/checkPassportById")
    public ResponseModel<Boolean> checkPassportById(@ModelAttribute UserInfoForToken userInfo,@RequestBody VerifyPassPortModel model){
        try {
            visaHandleService.examineAndVerifyPassPort(userInfo,model);
        }catch (PassportException e){
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "根据签证id发回审核", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/aRetrialPassPort")
    public ResponseModel<Boolean> aRetrialPassPort(@ModelAttribute UserInfoForToken userInfo,@RequestBody VerifyPassPortModel model){
        try {
            visaHandleService.aRetrialPassPort(userInfo,model);
        }catch (PassportException e){
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "查看待补充签证", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getProblemPassport")
    public ResponseModel getProblemPassport(@ModelAttribute UserInfoForToken userInfo, @RequestBody ValidationModel model)throws PassportException{
        return ResponseModel.sucess("",visaHandleService.getProblemPassPort(userInfo,model ));
    }

    @ApiOperation(value = "根据护照id查看护照信息（重审）", notes = "")
   @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getPassportByPassId")
    public ResponseModel<PassportModel> getPassportByPassId(@ModelAttribute UserInfoForToken userInfo, @RequestParam String id)throws PassportException{
        return ResponseModel.sucess("",visaHandleService.getPassportById2(userInfo,id ));
    }

    @ApiOperation(value = "查询签证信息审核", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getPassPortByStatus")
    public ResponseModel getPassPortByStatus(@ModelAttribute UserInfoForToken userInfo, @RequestBody ValidationModel model)throws PassportException{
        return ResponseModel.sucess("",visaHandleService.getPassPortByStatus(userInfo,model ));
    }

    @ApiOperation(value = "查看签证进度", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getPassPortByPass")
    public ResponseModel getPassPortByPass(@ModelAttribute UserInfoForToken userInfo, @RequestBody ValidationModel model){
        try {
            return ResponseModel.sucess("",visaHandleService.getPassPortByPass(userInfo,model ));
        }catch (Exception e){
            return ResponseModel.fail("",e.getMessage());
        }

    }

    @ApiOperation(value = "送签操作", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/sendVisa/{passPortid}")
    public ResponseModel sendVisa(@ModelAttribute UserInfoForToken userInfo, @PathVariable String passPortid){
        try {
            visaHandleService.sendVisa(userInfo,passPortid);
            return ResponseModel.sucessWithEmptyData("");
        }catch (Exception e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "出签拒签操作", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/outAndRefuseVisa/{passPortid}/{status}")
    public ResponseModel outAndRefuseVisa(@ModelAttribute UserInfoForToken userInfo, @PathVariable String passPortid, @PathVariable String status){
        try {
            visaHandleService.outAndRefuseVisa(userInfo,passPortid,status);
            return ResponseModel.sucessWithEmptyData("");
        }catch (Exception e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "寄回操作", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/sendBackVisa")
    public ResponseModel sendBackVisa(@ModelAttribute UserInfoForToken userInfo, @RequestBody LogisticsModel model){
        try {
            return ResponseModel.sucess("",visaHandleService.sendBackVisa(userInfo,model));
        }catch (Exception e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "一同寄回操作", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/sendBackVisaTogether")
    public ResponseModel sendBackVisaTogether(@ModelAttribute UserInfoForToken userInfo, @RequestBody LogisticsModel model){
        try {
            return ResponseModel.sucess("",visaHandleService.sendBackTogether(userInfo,model));
        }catch (Exception e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "excel导入护照", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/savePassPortByExcel/{expressReceiptId}")
    public ResponseModel savePassPortByExcel(@ModelAttribute UserInfoForToken userInfo,@RequestParam MultipartFile file, @PathVariable String expressReceiptId){
        try {
            return ResponseModel.sucess("", visaHandleService.addPassPortByExcel(file,userInfo,expressReceiptId));
        }catch (PassportException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "批量插入护照", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addPassPortByBatch")
    public ResponseModel addPassPortByBatch(@ModelAttribute UserInfoForToken userInfo,@RequestBody BatchPassPortModel model){
        try {
            visaHandleService.addPassPortByBatch(userInfo,model);
            return ResponseModel.sucessWithEmptyData("");
        }catch (PassportException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "批量插入照片", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/localUploadPics/{orderId}")
    public ResponseModel localUploadPics(@ModelAttribute UserInfoForToken userInfo,@RequestParam MultipartFile[] file,@PathVariable String orderId){
        try {
            visaHandleService.localUploadPics(userInfo,file,orderId);
            return ResponseModel.sucessWithEmptyData("");
        }catch (Exception e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "导出护照列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getStatisticsModel")
    public ResponseModel getStatisticsModel(@ModelAttribute UserInfoForToken userInfo, @RequestBody RequireVisaModel model){
        try {
            return ResponseModel.sucess("",visaHandleService.getExpressReceiptStatistic(userInfo,model));
        }catch (Exception e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据订单查询护照（导出）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getPassportByOrderIds")
    public ResponseModel getPassportByOrderIds(@ModelAttribute UserInfoForToken userInfo, @RequestBody RoleAddModel model){
        try {
            return ResponseModel.sucess("",visaHandleService.getAllPassPortInformationByOrderId(userInfo,model.getOrderIds()));
        }catch (Exception e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据订单查询护照（全部导出）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getAllPassportByOrderIds")
    public ResponseModel getAllPassportByOrderIds(@ModelAttribute UserInfoForToken userInfo, @RequestBody RoleAddModel model){
        try {
            return ResponseModel.sucess("",visaHandleService.getAllPassPortInformationByOrderId2(userInfo,model.getOrderIds()));
        }catch (Exception e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据护照查询基本信息（打印）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getsendBackMessageByPassPortId")
    public ResponseModel getsendBackMessageByPassPortId(@ModelAttribute UserInfoForToken userInfo, @RequestParam String passportId){
        try {
            return ResponseModel.sucess("",visaHandleService.getsendBackMessageByPassPortId(userInfo,passportId));
        }catch (Exception e){
            return ResponseModel.fail("",e.getMessage());
        }
    }
}
