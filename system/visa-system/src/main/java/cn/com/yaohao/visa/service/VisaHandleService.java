package cn.com.yaohao.visa.service;

import cn.com.yaohao.visa.constant.ResultCode;
import cn.com.yaohao.visa.dao.mapper.IVisaMapper;
import cn.com.yaohao.visa.entity.ExpressReceipt;
import cn.com.yaohao.visa.entity.IntermediateTable.PassportRelation;
import cn.com.yaohao.visa.entity.IntermediateTable.PassportSupplementRelation;
import cn.com.yaohao.visa.entity.IntermediateTable.VisaPassportRelation;
import cn.com.yaohao.visa.entity.IntermediateTable.VisaRemarkRelation;
import cn.com.yaohao.visa.entity.PassportEssential;
import cn.com.yaohao.visa.entity.PassportInformation;
import cn.com.yaohao.visa.entity.RemarksInformation;
import cn.com.yaohao.visa.exception.PassportException;
import cn.com.yaohao.visa.model.PassportModel;
import cn.com.yaohao.visa.model.RequireVisaModel;
import cn.com.yaohao.visa.model.UserInfoForToken;
import cn.com.yaohao.visa.model.ValidationModel;
import cn.com.yaohao.visa.repository.*;
import cn.com.yaohao.visa.repository.IntermediateRepasitory.IPassPortSupplementRelationRepository;
import cn.com.yaohao.visa.repository.IntermediateRepasitory.VisaPassportRelationRepository;
import cn.com.yaohao.visa.repository.IntermediateRepasitory.VisaRemarkRelationRepository;
import cn.com.yaohao.visa.repository.IntermediateRepasitory.PassportInformationRepository;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.io.IOException;
import java.util.*;

@Service
public class VisaHandleService {

    
    @Resource
    private PassportInformationRepository passportInformationRepository;
    @Resource
    private PassportEssentialRepository passportEssentialRepository;
    @Resource
    private PassportRelationRepository passportRelationRepository;
    @Resource
    private VisaPassportRelationRepository visaPassportRelationRepository;
    @Resource
    private RemarksInformationRepository remarksInformationRepository;
    @Resource
    private VisaRemarkRelationRepository visaRemarkRelationRepository;
    @Resource
    private ExpressReceiptRepository expressReceiptRepository;
    @Resource
    private IPassPortSupplementRelationRepository passPortSupplementRelationRepository;
    @Resource
    private IVisaMapper visaMapper;
        //查询全部
   /* public PageInfo<TestExpress> getAllVisa(UserInfoForToken userInfo, int pageNum, int pageSize){
         *//*Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "createTime"));
        Pageable pageable = new PageRequest((pageNum - 1)*pageSize,pageSize, sort);*//*

     *//*   Page<TestExpress> course = testExpressRepository.findAll((Root<TestExpress> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        }, pageable);
        return course;*//*
        List<TestExpress> expresses=testExpressRepository.getOrderByCreateTimeASEC((pageNum-1)*pageSize,pageSize);
        List<TestExpress> count2=testExpressRepository.findAll();
        PageInfo<TestExpress> page=new PageInfo<TestExpress>();
        int count=count2.size();
        page.setTotal(count);
        page.setList(expresses);
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        page.setPages((count + pageSize - 1)/pageSize);
        return page;
    }*/

    //查询全部
    public Page<ExpressReceipt> getAllVisa(UserInfoForToken userInfo, int pageNum, int pageSize){
        Page<ExpressReceipt> expresses=expressReceiptRepository.findAll((Root<ExpressReceipt> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(criteriaBuilder.equal(root.get("problem").as(String.class),"否"));
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },PageRequest.of((pageNum-1)*pageSize,pageSize));
        return expresses;
    }
    //条件查询
    public Page<ExpressReceipt> getRequirementsVisa(UserInfoForToken userInfo, RequireVisaModel model){
     Page<ExpressReceipt> expresses=expressReceiptRepository.findAll((Root<ExpressReceipt> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
            if (!StringUtils.isEmpty(model.getOrderNumber())){
                predicateList.add(criteriaBuilder.equal(root.get("orderNumber").as(String.class),model.getOrderNumber()));
            }else if (!StringUtils.isEmpty(model.getExpressCompany())){
                predicateList.add(criteriaBuilder.equal(root.get("courierName").as(String.class),model.getExpressCompany()));
            }else if (!StringUtils.isEmpty(model.getCourierNumber())){
                predicateList.add(criteriaBuilder.equal(root.get("courierNumber").as(String.class),model.getCourierNumber()));
            }else if (!StringUtils.isEmpty(model.getSignatory())){
                predicateList.add(criteriaBuilder.equal(root.get("signatory").as(String.class),model.getSignatory()));
            }else if (!StringUtils.isEmpty(model.getTelephoneNumber())){
                predicateList.add(criteriaBuilder.equal(root.get("telephoneNumber").as(String.class),model.getTelephoneNumber()));
            }else if (!StringUtils.isEmpty(model.getSigningTime())){//&& model.getSigningTime()!=0
                predicateList.add(criteriaBuilder.equal(root.get("createTime").as(long.class),model.getSigningTime()));
            }else if (!StringUtils.isEmpty(model.getStatus())||model.getStatus()!=0){
                predicateList.add(criteriaBuilder.equal(root.get("status").as(Integer.class),model.getStatus()));
            }
            predicateList.add(criteriaBuilder.equal(root.get("problem").as(String.class),"否"));
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },PageRequest.of((model.getPageNum()-1)*model.getPageSize(),model.getPageSize()));
        return expresses;
    }
    //录入护照
    @Transactional(rollbackFor = Exception.class)
    public boolean passportEntry( UserInfoForToken userInfo,PassportModel model)throws PassportException {
       /* if (StringUtils.isEmpty(userInfo)||StringUtils.isEmpty(model)
                ||StringUtils.isEmpty(model.getInformation())||StringUtils.isEmpty(model.getEssential())){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }*/
        //基本信息
       model.getEssential().setId(UUID.randomUUID().toString().replaceAll("-",""));
       ExpressReceipt expressReceipt=expressReceiptRepository.findByid(model.getVisaId());
       if(expressReceipt.getCount()==visaPassportRelationRepository.countByVid(expressReceipt.getId())){
            throw new PassportException("护照数量已满！");
       }
       PassportEssential essential= passportEssentialRepository.save(model.getEssential());
       //护照信息
       /* PassportInformation information=passportInformationRepository.findByIdIs(model.getPassportId());
       model.getInformation().setId(information.getId());
       model.getInformation().setPassprot(information.getPassprot());
       model.getInformation().setCreateTime(System.currentTimeMillis());*/
       model.getInformation().setOrderId(expressReceipt.getOrderNumber());//订单号
        model.getInformation().setStatus("P");
       PassportInformation passportInformation=passportInformationRepository.save(model.getInformation());
       //护照信息和基本信息
        PassportRelation passportRelation=new PassportRelation();
        passportRelation.setId(UUID.randomUUID().toString().replaceAll("-",""));
        passportRelation.setEssentialId(essential.getId());
        passportRelation.setPassportId(model.getPassportId());
        passportRelationRepository.save(passportRelation);
        //护照和快件
        VisaPassportRelation visaPassportRelation=new VisaPassportRelation();
        visaPassportRelation.setId(UUID.randomUUID().toString().replaceAll("-",""));
        visaPassportRelation.setPassId(passportInformation.getId());
        visaPassportRelation.setVid(model.getVisaId());
        visaPassportRelationRepository.save(visaPassportRelation);
        return true;
    }

    //录入护照
    public String passportEntryPic(UserInfoForToken userInfo,MultipartFile file)throws PassportException,IOException {
       /* if (StringUtils.isEmpty(userInfo)||StringUtils.isEmpty(file)){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }*/
        double fileSize = (double) file.getSize() / 1024 / 1024;//MB
        if (fileSize > 3) {
            throw new IOException("图片过大！");
        }
        PassportInformation information=new PassportInformation();
        information.setId(UUID.randomUUID().toString().replaceAll("-",""));
        information.setPassprot(file.getBytes());
        PassportInformation in= passportInformationRepository.save(information);
        return in.getId();
    }
    //查看备注
    public List<RemarksInformation> getVisaRemark(UserInfoForToken userInfo,String vId){
        List<VisaRemarkRelation> visaRemarkRelation=visaRemarkRelationRepository.findByVisaId(vId);
        List<String> id=new ArrayList<String>();
        for (VisaRemarkRelation v:visaRemarkRelation){
            id.add(v.getRemarkId());
        }
        List<RemarksInformation> list=remarksInformationRepository.findByIdIn(id);
        return list;
    }
    //添加备注
    public boolean addVisaRemark(UserInfoForToken userInfo,RemarksInformation model,String passprtId)throws PassportException{
        /*if (StringUtils.isEmpty(userInfo)||StringUtils.isEmpty(model)
                ||StringUtils.isEmpty(passprtId)){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }*/
        //PassportInformation passportInformation=passportInformationRepository.findByIdIs(passprtId);
        ExpressReceipt testExpress=expressReceiptRepository.findByIdIs(passprtId);
        if (StringUtils.isEmpty(testExpress)){
            throw new PassportException(ResultCode.PARAM_ERR_MSG);
        }
        RemarksInformation information=remarksInformationRepository.save(model);
        //签证和备注
        VisaRemarkRelation visaRemarkRelation=new VisaRemarkRelation();
        visaRemarkRelation.setId(UUID.randomUUID().toString().replaceAll("-",""));
        visaRemarkRelation.setVisaId(passprtId);
        visaRemarkRelation.setRemarkId(information.getId());
        visaRemarkRelationRepository.save(visaRemarkRelation);
        return true;
    }

    //根据签证查看护照
    public List<PassportInformation> getPassportById(UserInfoForToken userInfo,String vId)throws PassportException{
       /* if (StringUtils.isEmpty(userInfo)||StringUtils.isEmpty(vId)){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }*/
        List<VisaPassportRelation> visaPassportRelation=visaPassportRelationRepository.findByVid(vId);
        List<String> passId=new ArrayList<String>();
        for (VisaPassportRelation v:visaPassportRelation){
            passId.add(v.getPassId());
        }
        List<PassportInformation> pass=passportInformationRepository.findByIdIn(passId);
        return pass;
    }
    //消息验证
  /* public Page<ValidationModel> getVerification(UserInfoForToken userInfo,ValidationModel model)throws PassportException{


       Page<TestExpress> expresses=testExpressRepository.findAll((Root<TestExpress> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
           List<Predicate> predicateList = new ArrayList<>();
           if (!StringUtils.isEmpty(model.getOrderNumber())){
               predicateList.add(criteriaBuilder.equal(root.get("orderNumber").as(String.class),model.getOrderNumber()));
           }else if (!StringUtils.isEmpty(model.getExpressCompany())){
               predicateList.add(criteriaBuilder.equal(root.get("courierName").as(String.class),model.getExpressCompany()));
           }else if (!StringUtils.isEmpty(model.getCourierNumber())){
               predicateList.add(criteriaBuilder.equal(root.get("courierNumber").as(String.class),model.getCourierNumber()));
           }else if (!StringUtils.isEmpty(model.getSignatory())){
               predicateList.add(criteriaBuilder.equal(root.get("signatory").as(String.class),model.getSignatory()));
           }else if (!StringUtils.isEmpty(model.getTelephoneNumber())){
               predicateList.add(criteriaBuilder.equal(root.get("telephoneNumber").as(String.class),model.getTelephoneNumber()));
           }else if (!StringUtils.isEmpty(model.getSigningTime())&& model.getSigningTime()!=0){
               predicateList.add(criteriaBuilder.equal(root.get("createTime").as(long.class),model.getSigningTime()));
           }else if (!StringUtils.isEmpty(model.getStatus())||model.getStatus()!=0){
               predicateList.add(criteriaBuilder.equal(root.get("status").as(Integer.class),model.getStatus()));
           }
           predicateList.add(criteriaBuilder.equal(root.get("problem").as(String.class),"否"));
           return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
       },PageRequest.of((model.getPageNum()-1)*model.getPageSize(),model.getPageSize()));
       return expresses;
   }
*/
    //查看审核护照
    public PassportModel getCheckPassportById(UserInfoForToken userInfo,String passprtId)throws PassportException{

        /*if (StringUtils.isEmpty(userInfo)||StringUtils.isEmpty(passprtId)){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }*/
        //基本信息
        PassportRelation passportRelation=passportRelationRepository.findByPassportId(passprtId);
        PassportEssential passportEssential=passportEssentialRepository.findByIdIs(passportRelation.getEssentialId());
        PassportInformation passportInformation=passportInformationRepository.findByIdIs(passportRelation.getPassportId());
        PassportModel model=new PassportModel();
        model.setEssential(passportEssential);
        model.setInformation(passportInformation);
        model.setPassportId(passportRelation.getPassportId());
        model.setVisaId(passprtId);
        return model;
    }

    /**
     * 待补充签证
     * @param userInfo
     * @param model
     * @return
     */
   public List<ValidationModel> getProblemPassPort(UserInfoForToken userInfo,ValidationModel model){
        //有问题护照
       /* List<PassportSupplementRelation> psr=passPortSupplementRelationRepository.findAll();
        List<String> ppIds=new ArrayList<>();
        psr.forEach(passportSupplementRelation -> {
            ppIds.add(passportSupplementRelation.getPassId());
        });*/
       List<ValidationModel> models=visaMapper.findProblePassWord(model.getOrderNumber(),model.getName(),model.getPassportEncoding(),model.getTelephoneNumber(),(model.getPageNum()-1)*model.getPageSize(),model.getPageSize());
       return models;
    }

    /**
     * 获得护照信息（重审）
     * @param userInfo
     * @param id
     * @return
     */
    public PassportModel getPassportById2(UserInfoForToken userInfo,String id)throws PassportException{
        VisaPassportRelation visaPassportRelation=visaPassportRelationRepository.findByPassId(id);//关系
        if (visaPassportRelation==null){
            throw new PassportException(ResultCode.EXPREES_NOEXIST_MSG);
        }
        ExpressReceipt expressReceipt=expressReceiptRepository.findByid(visaPassportRelation.getVid());//快递
        if (expressReceipt==null){
            throw new PassportException(ResultCode.EXPREES_NOEXIST_MSG);
        }
        PassportInformation passportInformation=passportInformationRepository.findByIdIs(id);//护照
        if (passportInformation==null){
            throw new PassportException(ResultCode.PASSPORT_NOEXIST_MSG);
        }
        PassportModel model=new PassportModel();
        model.setVisaId(expressReceipt.getId());
        model.setPassportId(id);
        PassportEssential pe=new PassportEssential();
        pe.setCourierNumber(expressReceipt.getCourierNumber());
        pe.setExpressAddress(expressReceipt.getAddress());
        pe.setOrderNumber(expressReceipt.getOrderNumber());
        pe.setName(passportInformation.getName());
        model.setEssential(pe);
        model.setInformation(passportInformation);
        return model;
    }

    /*public List<PassportModel> getVisaHandleList(UserInfoForToken userInfo){
        List<PassportInformation> passportInformations=passportInformationRepository.findByStatus("A");//审核通过的
        List<String> passIds=new ArrayList<>();
        passportInformations.forEach(passportInformation -> {
            passIds.add(passportInformation.getId());
        });
        List<VisaPassportRelation> visaPassportRelations=visaPassportRelationRepository.findByPassIdIn(passIds);//查询订单信息
        List<String> visaIds=new ArrayList<>();
        visaPassportRelations.forEach(vv->{
            visaIds.add(vv.getVid());
        });
        //去重
        Set set=new HashSet();
        List newIds=new ArrayList();
        set.addAll(visaIds);
        newIds.addAll(set);
        //订单
        List<ExpressReceipt> expressReceipts=expressReceiptRepository.findByidIn(newIds);
        for (ExpressReceipt er:expressReceipts) {
            
        }

    }*/
}
