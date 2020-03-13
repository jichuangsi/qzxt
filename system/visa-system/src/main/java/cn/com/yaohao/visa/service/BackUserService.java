package cn.com.yaohao.visa.service;

import cn.com.yaohao.visa.config.Md5Util;
import cn.com.yaohao.visa.constant.ResultCode;
import cn.com.yaohao.visa.dao.mapper.IVisaMapper;
import cn.com.yaohao.visa.entity.*;
import cn.com.yaohao.visa.entity.IntermediateTable.UserRoleRelation;
import cn.com.yaohao.visa.exception.PassportException;
import cn.com.yaohao.visa.model.backuser.*;
import cn.com.yaohao.visa.model.UserInfoForToken;
import cn.com.yaohao.visa.repository.IBackUserInfoRepository;
import cn.com.yaohao.visa.repository.IParentNodeRepository;
import cn.com.yaohao.visa.repository.IRoleRepository;
import cn.com.yaohao.visa.repository.IVisaOperationRecordRepository;
import cn.com.yaohao.visa.repository.IntermediateRepasitory.IUserRoleRelationRepository;
import cn.com.yaohao.visa.util.MappingEntityModelCoverter;
import cn.com.yaohao.visa.util.TimeUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class BackUserService {

    @Resource
    private IBackUserInfoRepository backUserRepository;
    @Resource
    private BackTokenService backTokenService;
    @Resource
    private IRoleRepository roleRepository;
    @Resource
    private IUserRoleRelationRepository userRoleRelationRepository;
    @Resource
    private IParentNodeRepository parentNodeRepository;
    @Resource
    private IVisaMapper visaMapper;
    @Resource
    private IVisaOperationRecordRepository visaOperationRecordRepository;

    private Logger logger=LoggerFactory.getLogger(getClass());

    /**
     * 用户注册
     * @param backUser
     * @throws PassportException
     */
    public void registBackUser(BackUserInfo backUser)throws PassportException {
        if (StringUtils.isEmpty(backUser.getAccount()) || StringUtils.isEmpty(backUser.getPwd())
                || StringUtils.isEmpty(backUser.getUserName())){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        if (backUserRepository.countByaccount(backUser.getAccount())>0){
            throw new PassportException(ResultCode.ACCOUNT_ISEXIST_MSG);
        }
        backUser.setStatus("A");
        backUser.setPwd(Md5Util.encodeByMd5(backUser.getPwd()));
        backUser.setRoleId("else");
        backUser.setRoleName("else");
        backUserRepository.save(backUser);
    }

    /**
     * 用户登录
     * @param model
     * @return
     * @throws PassportException
     */
    public String loginBackUser(BackUserLoginModel model)throws PassportException{
        if (StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getPwd())){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        BackUserInfo backUser=backUserRepository.findByAccountAndPwd(model.getAccount(),Md5Util.encodeByMd5(model.getPwd()));
        if (backUser==null){
            throw new PassportException(ResultCode.ACCOUNT_NOTEXIST_MSG);
        }
        List<UserRoleRelation> userRoleRelations=userRoleRelationRepository.findByUid(backUser.getId());
        if(userRoleRelations==null&&userRoleRelations.size()==0){
            throw new PassportException(ResultCode.USER_NOROLE_MSG);
        }
        //BackUserInfo backUser2=backUserRepository.findByAccountAndPwdAndStatus(model.getAccount(),Md5Util.encodeByMd5(model.getPwd()),"A");

        String user=JSONObject.toJSONString(MappingEntityModelCoverter.CONVERTERFROMBACKUSERINFO(backUser));
        try {
            return backTokenService.createdToken(user);
        }catch (UnsupportedEncodingException e){
            throw new PassportException(e.getMessage());
        }
    }

    /**
     * 添加管理员
     * @throws PassportException
     */
    public void insertSuperMan() throws PassportException {
        if (backUserRepository.countByaccount("admin") > 0){
            throw new PassportException(ResultCode.ACCOUNT_ISEXIST_MSG);
        }
        BackUserInfo userInfo = new BackUserInfo();
        userInfo.setStatus("A");
        userInfo.setAccount("admin");
        userInfo.setUserName("admin");
        userInfo.setRoleId("123456");
        userInfo.setRoleName("M");
        /*userInfo.setDeptId("123456");
        userInfo.setDeptName("S");*/
        userInfo.setPwd(Md5Util.encodeByMd5("admin"));
        userInfo.setCreatedTime(new Date().getTime());
        backUserRepository.save(userInfo);
    }

    /**
     * 获取单个用户信息
     * @param userInfoForToken
     * @return
     * @throws PassportException
     */
    public BackUserModel getBackUserById(UserInfoForToken userInfoForToken)throws PassportException{
        BackUserInfo user=backUserRepository.findByid(userInfoForToken.getUserId());
        BackUserModel model =MappingEntityModelCoverter.CONVERTERFROMBACKUSERINFOTOMODEL(user);
        if(user==null){
            throw new PassportException(ResultCode.ACCOUNT_NOTEXIST_MSG);
        }
        if (user.getRoleId().equalsIgnoreCase("123456")&&user.getRoleName().equalsIgnoreCase("123456")){//管理员
            List<Role> roles=new ArrayList<>();
            Role role=new Role();
            role.setId(Integer.valueOf(user.getRoleId()));
            role.setRolename(user.getRoleName());
            roles.add(role);
            model.setRoles(roles);
        }else {
            List<Role> roles=visaMapper.findRoleByUid(user.getId());
            if (roles!=null && roles.size()!=0){
                model.setRoles(roles);
            }else {
                model.setRoles(null);
            }
        }
        return model;
    }

    /**
     * 用户修改密码
     * @param userInfoForToken
     * @param model
     * @throws PassportException
     */
    public void updateBackUserPwd(UserInfoForToken userInfoForToken, UpdatePwdModel model)throws PassportException {
        if(StringUtils.isEmpty(model.getFirstPwd()) || StringUtils.isEmpty(model.getSecondPwd())){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        if(!(model.getFirstPwd().equals(model.getSecondPwd()))){
            throw new PassportException(ResultCode.PWD_NOT_MSG);
        }
        BackUserInfo user=backUserRepository.findByid(userInfoForToken.getUserId());
        if(null==user){
            throw new PassportException(ResultCode.ACCOUNT_NOTEXIST_MSG);
        }
        user.setPwd(Md5Util.encodeByMd5(model.getSecondPwd()));
        backUserRepository.save(user);
    }

    //修改员工密码
    public void updateBackUserPwd2(UserInfoForToken userInfoForToken,String id,String pwd)throws PassportException {
        if(StringUtils.isEmpty(pwd) || StringUtils.isEmpty(id)){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        BackUserInfo user=backUserRepository.findByid(id);
        if (user==null){
            throw new PassportException(ResultCode.ACCOUNT_NOTEXIST_MSG);
        }
        user.setPwd(Md5Util.encodeByMd5(pwd));
        backUserRepository.save(user);
    }

    /**
     * 查询全部员工
     * @param userInfo
     * @return
     */
    public PageInfo<BackUserModel> getAllBackUser(UserInfoForToken userInfo, Integer role, int pageNum, int pageSize){
        List<BackUserModel> models=new ArrayList<>();
        int total=0;
        if(StringUtils.isEmpty(role)){
           List<BackUserInfo> backUserInfos=backUserRepository.findAllByPage((pageNum-1)*pageSize,pageSize);
           total=backUserRepository.countByRoleIdNotContains(123456);
            for (BackUserInfo user:backUserInfos
                 ) {
                BackUserModel model=MappingEntityModelCoverter.CONVERTERFROMBACKUSERINFOTOMODEL(user);
                List<Role> roles=visaMapper.findRoleByUid(user.getId());
                if (roles!=null && roles.size()!=0){
                    model.setRoles(roles);
                }else {
                    model.setRoles(null);
                }
                models.add(model);
            }
            PageInfo page=new PageInfo();
            page.setPageSize(pageSize);
            page.setPageNum(pageNum);
            page.setTotal(total);
            page.setList(models);
            return page;
        }
        List<BackUserInfo> backUserInfos=backUserRepository.findAllByPage(role,(pageNum-1)*pageSize,pageSize);
        total=backUserRepository.countByRoleIdAndRoleIdNotContains(role,123456);
        for (BackUserInfo user:backUserInfos
                ) {
            BackUserModel model=MappingEntityModelCoverter.CONVERTERFROMBACKUSERINFOTOMODEL(user);
            model.setRoles(visaMapper.findRoleByUid(user.getId()));
            models.add(model);
        }
        PageInfo page=new PageInfo();
        page.setPageSize(pageSize);
        page.setPageNum(pageNum);
        page.setTotal(total);
        page.setList(models);
        return page;
    }

    /**
     * 查询全部角色
     * @param userInfo
     * @return
     */
    public List<Role> getAllRole(UserInfoForToken userInfo){
        return roleRepository.findAll();
    }

    /**
     * 批量添加用户角色
     * @param userInfo
     * @param relations
     */
    public void saveUserRole(UserInfoForToken userInfo, List<UserRoleRelation> relations)throws PassportException{
        for (UserRoleRelation u:relations
             ) {
            if (StringUtils.isEmpty(u.getRid())){
                throw  new PassportException(ResultCode.PARAM_ERR_MSG);
            }
            //u.setUid(userInfo.getUserId());
        }
        userRoleRelationRepository.saveAll(relations);
    }

    /**
     * 批删除用户角色
     * @param userInfo
     * @param relations
     */
    public void deleteUserRole(UserInfoForToken userInfo, List<UserRoleRelation> relations)throws PassportException{
        for (UserRoleRelation u:relations
                ) {
            if (StringUtils.isEmpty(u.getRid())){
                throw  new PassportException(ResultCode.PARAM_ERR_MSG);
            }
            //u.setUid(userInfo.getUserId());
        }
        userRoleRelationRepository.deleteAll(relations);
    }

    /**
     * 查询全部父节点
     * @param userInfo
     * @return
     */
    public List<ParentNode> getAllParentNode(UserInfoForToken userInfo){
        return parentNodeRepository.findAll();
    }

    /**
     * 获取角色可访问页面
     * @param userInfo
     * @return
     */
    public List<UrlMapping> getUrlByRole(UserInfoForToken userInfo)throws PassportException{
        if (userInfo.getRoleId().equalsIgnoreCase("123456")){
            return visaMapper.findAllByRole();
        }
        List<UserRoleRelation> relations=userRoleRelationRepository.findByUid(userInfo.getUserId());
        List<Integer> roleIds=new ArrayList<>();
        relations.forEach(userRoleRelation -> {
            roleIds.add(userRoleRelation.getRid());
        });
        if (roleIds.size()==0){
            throw new PassportException(ResultCode.USER_NOROLE_MSG);
        }
        return visaMapper.findAllByRoleIn(roleIds);
    }

    /**
     * 获取角色可访问页面方法
     * @param userInfo
     * @return
     * @throws PassportException
     */
    public boolean getUrlMethodByRole(UserInfoForToken userInfo,String url)throws PassportException{
        if (userInfo.getRoleId().equalsIgnoreCase("123456")){
            return true;
        }
        List<UserRoleRelation> relations=userRoleRelationRepository.findByUid(userInfo.getUserId());
        List<Integer> roleIds=new ArrayList<>();
        relations.forEach(userRoleRelation -> {
            roleIds.add(userRoleRelation.getRid());
        });
        if (roleIds.size()==0){
            throw new PassportException(ResultCode.USER_NOROLE_MSG);
        }
        List<UrlMapping> urlMappings= visaMapper.findMethodByRoleIn(roleIds);
        Map<String,String> urlList=null;
        if (null!=urlMappings){
            urlList=new HashMap<String,String>();
            for (UrlMapping item:urlMappings) {
                urlList.put(item.getId(),item.getRoleUrl());
            }
            //通用方法
            urlList.put("获取用户信息","/backRoleConsole/getBackuserById");
            urlList.put("查询角色","/backRoleConsole/getAllRole");
            urlList.put("查询父节点","/backRoleConsole/getAllParentNode");
            urlList.put("查询角色对应url列表","/backRoleConsole/getUrlByUserRole");
        }
        if(null!=urlList && urlList.size()!=0){
            for (Map.Entry<String,String> entry: urlList.entrySet()) {
                if (url.equals(entry.getValue())|| url.startsWith(entry.getValue())){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 查看操作记录
     * @param userInfo
     * @param model
     * @return
     */
    public Page<VisaOperationRecord> getVisaRecords(UserInfoForToken userInfo, VisaRecordModel model){
        //Sort sort=new Sort(Sort.Direction.DESC,"createdTime");
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "createTime").nullsLast();
        Page<VisaOperationRecord> records=visaOperationRecordRepository.findAll((Root<VisaOperationRecord> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
            if (!StringUtils.isEmpty(model.getOrderNumber())){
                predicateList.add(criteriaBuilder.equal(root.get("orderNumber").as(String.class),model.getOrderNumber()));
            }else if (!StringUtils.isEmpty(model.getApplicantName())){
                predicateList.add(criteriaBuilder.like(root.get("applicantName").as(String.class),"%"+model.getApplicantName()+"%"));
            }else if (!StringUtils.isEmpty(model.getOperaterId())){
                predicateList.add(criteriaBuilder.equal(root.get("operaterId").as(String.class),model.getOperaterId()));
            }else if (!StringUtils.isEmpty(model.getOperaterName())){
                predicateList.add(criteriaBuilder.like(root.get("operaterName").as(String.class),"%"+model.getOperaterName()+"%"));
            }else if (!StringUtils.isEmpty(model.getPhone())){
                predicateList.add(criteriaBuilder.like(root.get("phone").as(String.class),"%"+model.getPhone()+"%"));
            }else if (!StringUtils.isEmpty(model.getCreateTime())){//&& model.getSigningTime()!=0
                long startTime=TimeUtils.startTime(model.getCreateTime());
                long endTime=TimeUtils.endTime(model.getCreateTime());
                predicateList.add(criteriaBuilder.between(root.get("createTime"),startTime,endTime));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },PageRequest.of(model.getPageNum()-1,model.getPageSize(),Sort.by(order)));
        return records;
    }
}
