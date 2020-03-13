package cn.com.yaohao.visa.service;

import cn.com.yaohao.visa.constant.ResultCode;
import cn.com.yaohao.visa.entity.ExpressReceipt;
import cn.com.yaohao.visa.entity.IntermediateTable.ExpressReceiptRemarkRelation;
import cn.com.yaohao.visa.entity.RemarksInformation;
import cn.com.yaohao.visa.exception.PassportException;
import cn.com.yaohao.visa.model.RequireVisaModel;
import cn.com.yaohao.visa.model.UserInfoForToken;
import cn.com.yaohao.visa.repository.ExpressReceiptRepository;
import cn.com.yaohao.visa.repository.IntermediateRepasitory.ExpressReceiptRemarkRelationRepository;
import cn.com.yaohao.visa.repository.RemarksInformationRepository;
import cn.com.yaohao.visa.util.TimeUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpressReceiptService {
    @Resource
    private ExpressReceiptRepository expressReceiptRepository;
    @Resource
    private RemarksInformationRepository remarksInformationRepository;
    @Resource
    private ExpressReceiptRemarkRelationRepository expressReceiptRemarkRelationRepository;

    /**
     * 快件签收
     * @param userInfo
     * @param expressReceipt
     * @throws PassportException
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveExpressReceipt(UserInfoForToken userInfo, ExpressReceipt expressReceipt)throws PassportException {
        if (StringUtils.isEmpty(expressReceipt.getCount()) || StringUtils.isEmpty(expressReceipt.getCourierName())
                || StringUtils.isEmpty(expressReceipt.getCourierNumber()) || StringUtils.isEmpty(expressReceipt.getOrderNumber())
                || StringUtils.isEmpty(expressReceipt.getProblem()) || StringUtils.isEmpty(expressReceipt.getTelephoneNumber())
                || StringUtils.isEmpty(expressReceipt.getSignatory())|| StringUtils.isEmpty(expressReceipt.getOrderPath())
                || StringUtils.isEmpty(expressReceipt.getAddress())|| StringUtils.isEmpty(expressReceipt.getReturnAddress())){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        //待修改
        expressReceipt.setStatus("未匹配");
        expressReceipt.setIsError("0");
        expressReceiptRepository.save(expressReceipt);
    }

    /**
     * 更正快件
     * @param userInfo
     * @param expressReceipt
     * @throws PassportException
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateExpressReceipt(UserInfoForToken userInfo, ExpressReceipt expressReceipt)throws PassportException {
        if (StringUtils.isEmpty(expressReceipt.getCount()) || StringUtils.isEmpty(expressReceipt.getCourierName())
                || StringUtils.isEmpty(expressReceipt.getCourierNumber()) || StringUtils.isEmpty(expressReceipt.getOrderNumber())
                || StringUtils.isEmpty(expressReceipt.getProblem()) || StringUtils.isEmpty(expressReceipt.getTelephoneNumber())
                || StringUtils.isEmpty(expressReceipt.getSignatory())|| StringUtils.isEmpty(expressReceipt.getOrderPath())
                || StringUtils.isEmpty(expressReceipt.getAddress())|| StringUtils.isEmpty(expressReceipt.getReturnAddress())
                || StringUtils.isEmpty(expressReceipt.getId())){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        ExpressReceipt expressReceipt1=expressReceiptRepository.findByIdIs(expressReceipt.getId());
        if (expressReceipt1==null){
            throw new PassportException(ResultCode.EXPREES_NOEXIST_MSG);
        }
        if (expressReceipt1.getIsError().equalsIgnoreCase("1")){//异常件
            //待修改
            expressReceipt1.setStatus("未匹配");
            expressReceipt1.setIsError("0");
        }else if (expressReceipt1.getIsError().equalsIgnoreCase("0")){//不是异常件
            //待修改
            expressReceipt1.setStatus("未匹配");
        }else {
            throw new PassportException("更正失败！");
        }
        expressReceipt1.setAddress(expressReceipt.getAddress());
        expressReceipt1.setCount(expressReceipt.getCount());
        expressReceipt1.setCourierName(expressReceipt.getCourierName());
        expressReceipt1.setCourierNumber(expressReceipt.getCourierNumber());
        expressReceipt1.setOrderPath(expressReceipt.getOrderPath());
        expressReceipt1.setSignatory(expressReceipt.getSignatory());
        expressReceipt1.setProblem(expressReceipt.getProblem());
        expressReceipt1.setTelephoneNumber(expressReceipt.getTelephoneNumber());
        expressReceiptRepository.save(expressReceipt1);
    }

    /**
     * 快件管理
     * @param userInfo
     * @param model
     * @return
     */
    public Page<ExpressReceipt> getRequirementsVisa(UserInfoForToken userInfo, RequireVisaModel model){
        Page<ExpressReceipt> expresses=expressReceiptRepository.findAll((Root<ExpressReceipt> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
            if (!StringUtils.isEmpty(model.getOrderNumber())){
                predicateList.add(criteriaBuilder.equal(root.get("orderNumber").as(String.class),model.getOrderNumber()));
            }else if (!StringUtils.isEmpty(model.getExpressCompany())){
                predicateList.add(criteriaBuilder.like(root.get("courierName").as(String.class),"%"+model.getExpressCompany()+"%"));
            }else if (!StringUtils.isEmpty(model.getCourierNumber())){
                predicateList.add(criteriaBuilder.equal(root.get("courierNumber").as(String.class),model.getCourierNumber()));
            }else if (!StringUtils.isEmpty(model.getSignatory())){
                predicateList.add(criteriaBuilder.like(root.get("signatory").as(String.class),"%"+model.getSignatory()+"%"));
            }else if (!StringUtils.isEmpty(model.getSigningTime())){//&& model.getSigningTime()!=0
                long startTime=TimeUtils.startTime(model.getSigningTime());
                long endTime=TimeUtils.endTime(model.getSigningTime());
                predicateList.add(criteriaBuilder.between(root.get("createTime"),startTime,endTime));
            }else if (!StringUtils.isEmpty(model.getTelephoneNumber())){
                predicateList.add(criteriaBuilder.like(root.get("telephoneNumber").as(String.class),"%"+model.getTelephoneNumber()+"%"));
            }else if (!StringUtils.isEmpty(model.getStatus())||model.getStatus()!=0){
                predicateList.add(criteriaBuilder.equal(root.get("status").as(Integer.class),model.getStatus()));
            }
            predicateList.add(criteriaBuilder.equal(root.get("isError").as(String.class),"0"));
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },PageRequest.of(model.getPageNum()-1,model.getPageSize(),Sort.Direction.DESC,"createTime"));
        return expresses;
    }

    /**
     * 异常快件
     * @param userInfo
     * @param model
     * @return
     */
    public Page<ExpressReceipt> getUnusualRequirementsVisa(UserInfoForToken userInfo, RequireVisaModel model){
        Page<ExpressReceipt> expresses=expressReceiptRepository.findAll((Root<ExpressReceipt> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
            if (!StringUtils.isEmpty(model.getOrderNumber())){
                predicateList.add(criteriaBuilder.equal(root.get("orderNumber").as(String.class),model.getOrderNumber()));
            }else if (!StringUtils.isEmpty(model.getExpressCompany())){
                predicateList.add(criteriaBuilder.like(root.get("courierName").as(String.class),"%"+model.getExpressCompany()+"%"));
            }else if (!StringUtils.isEmpty(model.getCourierNumber())){
                predicateList.add(criteriaBuilder.equal(root.get("courierNumber").as(String.class),model.getCourierNumber()));
            }else if (!StringUtils.isEmpty(model.getSignatory())){
                predicateList.add(criteriaBuilder.like(root.get("signatory").as(String.class),"%"+model.getSignatory()+"%"));
            }else if (!StringUtils.isEmpty(model.getTelephoneNumber())){
                predicateList.add(criteriaBuilder.like(root.get("telephoneNumber").as(String.class),"%"+model.getTelephoneNumber()+"%"));
            }else if (!StringUtils.isEmpty(model.getSigningTime())){
                long startTime=TimeUtils.startTime(model.getSigningTime());
                long endTime=TimeUtils.endTime(model.getSigningTime());
                predicateList.add(criteriaBuilder.between(root.get("createTime"),startTime,endTime));
            }else if (!StringUtils.isEmpty(model.getStatus())||model.getStatus()!=0){
                predicateList.add(criteriaBuilder.equal(root.get("status").as(Integer.class),model.getStatus()));
            }
            predicateList.add(criteriaBuilder.equal(root.get("isError").as(String.class),"1"));
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },PageRequest.of(model.getPageNum()-1,model.getPageSize(),Sort.Direction.DESC,"createTime"));
        return expresses;
    }

    /**
     * 添加异常
     * @param userInfo
     * @param expressId
     * @throws PassportException
     */
    @Transactional(rollbackFor = Exception.class)
    public void addUnusualToExpressReceipt(UserInfoForToken userInfo,String expressId)throws PassportException{
        if(StringUtils.isEmpty(expressId)){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        ExpressReceipt expressReceipt=expressReceiptRepository.findByid(expressId);
        if(expressReceipt==null){
            throw new PassportException(ResultCode.EXPREES_NOEXIST_MSG);
        }
        if (expressReceipt.getIsError().equalsIgnoreCase("0")){
            expressReceipt.setIsError("1");
            expressReceiptRepository.save(expressReceipt);
        }else {
            throw new PassportException("添加失败！");
        }
    }

    /**
     * 添加备注
     * @param userInfo
     * @param model
     * @param expressId
     * @throws PassportException
     */
    @Transactional(rollbackFor = Exception.class)
    public void addExpressRemark(UserInfoForToken userInfo, RemarksInformation model,String expressId)throws PassportException{
        if(StringUtils.isEmpty(expressId)){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        ExpressReceipt expressReceipt=expressReceiptRepository.findByid(expressId);
        if(expressReceipt==null){
            throw new PassportException(ResultCode.EXPREES_NOEXIST_MSG);
        }
        model.setOperator(userInfo.getUserName());
        model.setOperatorId(userInfo.getUserId());
        RemarksInformation information=remarksInformationRepository.save(model);
        //快件备注
        ExpressReceiptRemarkRelation err=new ExpressReceiptRemarkRelation();
        err.setRemarkId(information.getId());
        err.setExpressId(expressId);
        expressReceiptRemarkRelationRepository.save(err);
    }

    /**
     * 查看备注
     * @param userInfo
     * @param eId
     * @return
     */
    public List<RemarksInformation> getExpressRemark(UserInfoForToken userInfo,String eId,int pageNum,int pageSize) throws PassportException{
        if(StringUtils.isEmpty(eId)){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        List<ExpressReceiptRemarkRelation> em=expressReceiptRemarkRelationRepository.findByExpressId(eId);
        List<String> id=new ArrayList<String>();
        for (ExpressReceiptRemarkRelation e:em){
            id.add(e.getRemarkId());
        }
        List<RemarksInformation> list=remarksInformationRepository.findByIdInOrderByOperationTimeDesc(id,(pageNum-1)*pageSize,pageSize);
        return list;
    }
}
