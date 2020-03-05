package cn.com.yaohao.visa.controller.advice;

import cn.com.yaohao.visa.entity.BackUserInfo;
import cn.com.yaohao.visa.entity.ParentNode;
import cn.com.yaohao.visa.entity.Role;
import cn.com.yaohao.visa.exception.PassportException;
import cn.com.yaohao.visa.model.backuser.*;
import cn.com.yaohao.visa.model.ResponseModel;
import cn.com.yaohao.visa.model.UserInfoForToken;
import cn.com.yaohao.visa.service.BackUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api("后台角色相关")
@CrossOrigin
@RestController
@RequestMapping("/backRoleConsole")
public class BackUserRoleConsoleController {
    @Resource
    private BackUserService backUserService;

    @ApiOperation("后台用户注册")
    @ApiImplicitParams({})
    @PostMapping("/registBackUser")
    public ResponseModel registBackUser(@RequestBody BackUserInfo backUser){
        try {
            backUserService.registBackUser(backUser);
        }catch (PassportException e){
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation("后台用户登录")
    @ApiImplicitParams({})
    @PostMapping("/loginBackUser")
    public ResponseModel loginBackUser(@RequestBody BackUserLoginModel model){
        try {
            return ResponseModel.sucess("",backUserService.loginBackUser(model));
        }catch (PassportException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation("后台修改密码")
    @ApiImplicitParams({})
    @PostMapping("/updateBackUserPwd")
    public ResponseModel updateBackUserPwd(@ModelAttribute UserInfoForToken userInfoForToken, @RequestBody UpdatePwdModel model){
        try {
            backUserService.updateBackUserPwd(userInfoForToken,model);
            return ResponseModel.sucessWithEmptyData("");
        }catch (PassportException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation("后台修改员工密码")
    @ApiImplicitParams({})
    @PostMapping("/updateBackUserPwd2")
    public ResponseModel updateBackUserPwd2(@ModelAttribute UserInfoForToken userInfoForToken, @RequestBody UpdatePwdModel model){
        try {
            backUserService.updateBackUserPwd2(userInfoForToken,model.getId(),model.getSecondPwd());
            return ResponseModel.sucessWithEmptyData("");
        }catch (PassportException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation("根据id查询详细信息")
    @ApiImplicitParams({})
    @GetMapping("/getBackuserById")
    public ResponseModel getBackuserById(@ModelAttribute UserInfoForToken userInfo){
        try {
            return ResponseModel.sucess("",backUserService.getBackUserById(userInfo));
        }catch (PassportException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation("查询全部后台人员")
    @ApiImplicitParams({})
    @GetMapping("/getAllBackuserByCondition")
    public ResponseModel<List<BackUserModel>> getAllBackuserByCondition(@ModelAttribute UserInfoForToken userInfo, @RequestParam(required = false) Integer role, @RequestParam int pageNum, @RequestParam int pageSize){
        return ResponseModel.sucess("",backUserService.getAllBackUser(userInfo,role,pageNum,pageSize));
    }

    @ApiOperation("查询全部角色")
    @ApiImplicitParams({})
    @GetMapping("/getAllRole")
    public ResponseModel<List<Role>> getAllRole(@ModelAttribute UserInfoForToken userInfo){
        return ResponseModel.sucess("",backUserService.getAllRole(userInfo));
    }

    @ApiOperation("查询父节点")
    @ApiImplicitParams({})
    @GetMapping("/getAllParentNode")
    public ResponseModel<List<ParentNode>> getAllParentNode(@ModelAttribute UserInfoForToken userInfo){
        return ResponseModel.sucess("",backUserService.getAllParentNode(userInfo));
    }

    @ApiOperation("批量给用户添加角色")
    @ApiImplicitParams({})
    @PostMapping("/saveUserRole")
    public ResponseModel saveUserRole(@ModelAttribute UserInfoForToken userInfo, @RequestBody RoleAddModel model){
        try {
            backUserService.saveUserRole(userInfo,model.getUserRoleRelations());
            return ResponseModel.sucessWithEmptyData("");
        }catch (PassportException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation("批量给用户移除角色")
    @ApiImplicitParams({})
    @PostMapping("/removeUserRole")
    public ResponseModel removeUserRole(@ModelAttribute UserInfoForToken userInfo, @RequestBody RoleAddModel model){
        try {
            backUserService.deleteUserRole(userInfo,model.getUserRoleRelations());
            return ResponseModel.sucessWithEmptyData("");
        }catch (PassportException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation("查询角色可访问页面")
    @ApiImplicitParams({})
    @GetMapping("/getUrlByUserRole")
    public ResponseModel<List<UrlMapping>> getUrlByUserRole(@ModelAttribute UserInfoForToken userInfo){
        try {
            return ResponseModel.sucess("",backUserService.getUrlByRole(userInfo));
        }catch (PassportException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }
}
