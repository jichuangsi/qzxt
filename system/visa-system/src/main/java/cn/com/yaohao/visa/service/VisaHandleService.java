package cn.com.yaohao.visa.service;

import cn.com.yaohao.visa.constant.ResultCode;
import cn.com.yaohao.visa.dao.mapper.IVisaMapper;
import cn.com.yaohao.visa.entity.*;
import cn.com.yaohao.visa.entity.IntermediateTable.*;
import cn.com.yaohao.visa.exception.PassportException;
import cn.com.yaohao.visa.model.*;
import cn.com.yaohao.visa.repository.*;
import cn.com.yaohao.visa.repository.IntermediateRepasitory.IPassPortSupplementRelationRepository;
import cn.com.yaohao.visa.repository.IntermediateRepasitory.VisaPassportRelationRepository;
import cn.com.yaohao.visa.repository.IntermediateRepasitory.VisaRemarkRelationRepository;
import cn.com.yaohao.visa.repository.IntermediateRepasitory.PassportInformationRepository;
import cn.com.yaohao.visa.util.MappingEntityModelCoverter;
import cn.com.yaohao.visa.util.TimeUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
    private IPassPortImgRepository passPortImgRepository;
    @Resource
    private IPassPortSupplementRelationRepository passPortSupplementRelationRepository;
    @Resource
    private IBackUserInfoRepository backUserInfoRepository;
    @Resource
    private IVisaOperationRecordRepository visaOperationRecordRepository;
    @Resource
    private ILogisticsRepository logisticsRepository;
    @Resource
    private ShunFengService shunFengService;
    @Resource
    private IVisaMapper visaMapper;
    @Value("${file.uploadFolder}")
    private String uploadPath;
    @Value("${file.imagePath}")
    private String imagePath;
    @Value("${file.uri}")
    private String uri;

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
        },PageRequest.of(model.getPageNum()-1,model.getPageSize()));
        return expresses;
    }

    /**
     * 修改护照
     * @param userInfo
     * @param model
     * @throws PassportException
     */
    public void updatePassPort(UserInfoForToken userInfo,PassportInformation model)throws PassportException{
        if(StringUtils.isEmpty(model.getId())||StringUtils.isEmpty(model.getName())
                ||StringUtils.isEmpty(model.getSex())||StringUtils.isEmpty(model.getPassportEncoding())
                ||StringUtils.isEmpty(model.getTelephoneNumber())||StringUtils.isEmpty(model.getExpiryDate())){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        PassportInformation information=passportInformationRepository.findByIdIs(model.getId());
        if(information==null){
            throw new PassportException(ResultCode.PASSPORT_NOEXIST_MSG);
        }
        information.setHabitation(model.getHabitation());
        information.setReturnAddress(model.getReturnAddress());
        information.setBirthDay(model.getBirthDay());
        information.setBirthPlace(model.getBirthPlace());
        information.setTelephoneNumber(model.getTelephoneNumber());
        information.setName(model.getName());
        information.setSex(model.getSex());
        information.setPassportEncoding(model.getPassportEncoding());
        information.setLuggage(model.getLuggage());
        passportInformationRepository.save(information);
    }

    /**
     * 录入护照
     * @param userInfo
     * @param model
     * @return
     * @throws PassportException
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean passportEntry( UserInfoForToken userInfo,PassportModel model)throws PassportException {
       if (StringUtils.isEmpty(model)||StringUtils.isEmpty(model.getVisaId())
                ||StringUtils.isEmpty(model.getInformation())||StringUtils.isEmpty(model.getEssential())){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        //基本信息
       model.getEssential().setId(UUID.randomUUID().toString().replaceAll("-",""));
       ExpressReceipt expressReceipt=expressReceiptRepository.findByid(model.getVisaId());
       if(expressReceipt.getCount()==visaPassportRelationRepository.countByVid(expressReceipt.getId())){
            throw new PassportException("护照数量已满！");
       }
       PassportEssential essential= passportEssentialRepository.save(model.getEssential());
       //护照信息
        if(StringUtils.isEmpty(model.getPassportId())){//有图
            PassportInformation information=passportInformationRepository.findByIdIs(model.getPassportId());
            model.getInformation().setId(information.getId());
            //model.getInformation().setPassprot(information.getPassprot());
            model.getInformation().setCreateTime(System.currentTimeMillis());
        }
        model.getInformation().setOrderId(expressReceipt.getOrderNumber());//订单号
        model.getInformation().setStatus("W");//待审核
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

    /**
     * 批量录入护照
     * @param userInfo
     * @param model
     * @throws PassportException
     */
    public void addPassPortByBatch(UserInfoForToken userInfo,BatchPassPortModel model)throws PassportException{
        if (StringUtils.isEmpty(model)||StringUtils.isEmpty(model.getOrderId())
                ||StringUtils.isEmpty(model.getInformations())){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        ExpressReceipt expressReceipt=expressReceiptRepository.findByid(model.getOrderId());//查找快件
        List<String> code=new ArrayList<>();
        model.getInformations().forEach(information -> {
            code.add(information.getPassportEncoding());
        });
        List<PassportInformation> passportInformations=passportInformationRepository.findByPassportEncodingInAndStatusNot(code,"P");//查询录入时已存在的护照
        if(passportInformations!=null&&passportInformations.size()!=0){
            List<String> pids=new ArrayList<>();
            passportInformations.forEach(pa->{
                pids.add(pa.getId());
            });
            List<VisaPassportRelation> vpr2=visaPassportRelationRepository.findByPassIdIn(pids);
            if(vpr2!=null&&vpr2.size()!=0){
                visaPassportRelationRepository.deleteInBatch(vpr2);
            }
            passportInformationRepository.deleteInBatch(passportInformations);
        }
        List<PassportInformation> pa=passportInformationRepository.findByPassportEncodingInAndStatus(code,"P");//
        List<String> iii=new ArrayList<>();
        pa.forEach(p->{
            iii.add(p.getPassportEncoding());
        });
        List<PassportInformation> ppp=new ArrayList<>();
        for (PassportInformation im:model.getInformations()) {
            if (!iii.contains(im.getPassportEncoding())){
                ppp.add(im);
            }
        }
        List<PassportInformation> informationList=passportInformationRepository.saveAll(ppp);
        for (PassportInformation im:informationList) {
            VisaPassportRelation visaPassportRelation=new VisaPassportRelation();
            visaPassportRelation.setPassId(im.getId());
            visaPassportRelation.setVid(expressReceipt.getId());
            visaPassportRelationRepository.save(visaPassportRelation);
        }

        /*
        List<String> passNum=new ArrayList<>();//录入的护照编号
        model.getInformations().forEach(passportInformation -> {
            passNum.add(passportInformation.getPassportEncoding());
        });
        int count=passportInformationRepository.countByPassportEncodingInAndOrderId(passNum,expressReceipt.getOrderNumber());
        if (count>0){
            throw new PassportException("护照已存在！");
        }
        */
        /*
        int total=expressReceipt.getCount();//总本数
        int ok=visaPassportRelationRepository.countByVid(expressReceipt.getId());//已经录入
        int wait=model.getInformations().size();//等待录入
        if(total==ok){
            throw new PassportException("护照数量已满！");
        }else if(wait>(total-ok)){
            throw new PassportException("护照数量超额，还可以录入"+(total-ok)+"本！");
        }else if(wait<=(total-ok)){//数量相当
            List<String> passNum=new ArrayList<>();//录入的护照编号
            model.getInformations().forEach(passportInformation -> {
                passNum.add(passportInformation.getPassportEncoding());
            });
            int count=passportInformationRepository.countByPassportEncodingInAndOrderId(passNum,expressReceipt.getOrderNumber());
            if (count>0){
                throw new PassportException("护照已存在！");
            }
            List<PassportInformation> informationList=passportInformationRepository.saveAll(model.getInformations());
            for (PassportInformation im:informationList) {
                VisaPassportRelation visaPassportRelation=new VisaPassportRelation();
                visaPassportRelation.setPassId(im.getId());
                visaPassportRelation.setVid(expressReceipt.getId());
                visaPassportRelationRepository.save(visaPassportRelation);
            }
        }
        */

    }

    /**
     * 本地上传护照照片
     * @param userInfoForToken
     * @param file
     * @return
     * @throws IOException
     */
    public void localUploadPic(UserInfoForToken userInfoForToken, MultipartFile file) throws IOException,PassportException {
        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        double fileSize = (double) file.getSize()/1024/1024;//MB
        if(fileSize>2){
            throw new PassportException("图片过大！");
        }
        PassportInformation information=passportInformationRepository.findByPassportEncoding(fileName);
        PassPortImg imagePath=new PassPortImg();
        imagePath.setPassPortCode(fileName);//护照编码
        fileName =UUID.randomUUID()+suffixName;//重新生成文件名
        information.setPicPath(uri+fileName);
        File file1=new File(uploadPath+imagePath+fileName);
        if (!file1.exists()){
            //创建文件夹
            file1.getParentFile().mkdir();
        }
        file.transferTo(file1);
        imagePath.setPicPath(uri+fileName);
        passPortImgRepository.save(imagePath);
        passportInformationRepository.save(information);
    }

    /**
     * 批量上传护照图片
     * @param userInfoForToken
     * @param files
     * @param orderId
     * @throws IOException
     * @throws PassportException
     */
    public void localUploadPics(UserInfoForToken userInfoForToken, MultipartFile[] files,String orderId) throws IOException,PassportException {
        if (StringUtils.isEmpty(orderId)||StringUtils.isEmpty(files)||files.length==0){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        ExpressReceipt expressReceipt=expressReceiptRepository.findByid(orderId);
        if(expressReceipt==null){
            throw new PassportException(ResultCode.EXPREES_NOEXIST_MSG);
        }
        List<String> code=new ArrayList<>();
        for (MultipartFile file:files) {
            String fileName = file.getOriginalFilename();
            String passportEncoding=fileName.substring(0,fileName.indexOf("_"));
            code.add(passportEncoding);
        }
        if (code!=null && code.size()!=0){
            List<PassPortImg> passPortImgs=passPortImgRepository.findByOrderNumberAndPassPortCodeIn(expressReceipt.getOrderNumber(),code);
            if (passPortImgs!=null && passPortImgs.size()!=0) {
                passPortImgs.forEach(passPortImg -> {
                    File file=new File(uploadPath+passPortImg.getPicPath());
                    if(file.exists()){
                        //删除文件
                        file.delete();
                    }
                });
                passPortImgRepository.deleteInBatch(passPortImgs);
            }
        }
        for (MultipartFile file:files) {
            //获取文件名
            String fileName = file.getOriginalFilename();
            //获取文件后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            //重新生成文件名
            String newFileName =UUID.randomUUID()+suffixName;
            //护照编码
            String passportEncoding=fileName.substring(0,fileName.indexOf("_"));
            double fileSize = (double) file.getSize()/1024/1024;//MB
            if(fileSize>2){
                throw new PassportException("图片过大！");
            }

            //PassportInformation information=passportInformationRepository.findByPassportEncodingAndOrderId(passportEncoding,expressReceipt.getOrderNumber());//根据护照编码和订单号
            PassPortImg passPortImg=new PassPortImg();
            passPortImg.setPassPortCode(passportEncoding);//护照编码
            passPortImg.setOrderNumber(expressReceipt.getOrderNumber());
            //fileName =UUID.randomUUID()+suffixName;//重新生成文件名
            //information.setPicPath(uri+fileName);
            File file1=new File(uploadPath+imagePath+fileName);
            if (!file1.exists()){
                //创建文件夹
                file1.getParentFile().mkdir();
            }
            file.transferTo(file1);
            passPortImg.setPicPath(uri+fileName);
            passPortImg.setFileName(fileName);
            passPortImgRepository.save(passPortImg);
            //passportInformationRepository.save(information);
        }
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

    /**
     * 根据签证查看护照
     * @param userInfo
     * @param vId
     * @return
     * @throws PassportException
     */
    public PageInfo<PassportModel> getPassportById(UserInfoForToken userInfo,String vId,int pageNum,int pageSize)throws PassportException{
        if (StringUtils.isEmpty(vId)){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        ExpressReceipt expressReceipt=expressReceiptRepository.findByIdIs(vId);
        if (expressReceipt==null){
            throw new PassportException(ResultCode.EXPREES_NOEXIST_MSG);
        }
        List<VisaPassportRelation> visaPassportRelation=visaPassportRelationRepository.findByVid(vId);
        if(visaPassportRelation==null && visaPassportRelation.size()==0){
            throw new PassportException("该快件下暂无护照");
        }
        List<String> passId=new ArrayList<String>();
        for (VisaPassportRelation v:visaPassportRelation){
            passId.add(v.getPassId());
        }
        List<PassportInformation> pass=passportInformationRepository.findByIdIn(passId,(pageNum-1)*pageSize,pageSize);
        List<PassportModel> passportModels=new ArrayList<>();
        for (PassportInformation pi:pass) {
            PassportModel model=new PassportModel();
            model.setInformation(pi);
            model.setExpressReceipt(expressReceipt);
            passportModels.add(model);
        }
        PageInfo page=new PageInfo();
        page.setPageSize(pageSize);
        page.setPageNum(pageNum);
        page.setList(passportModels);
        page.setTotal(passportInformationRepository.countByIdIn(passId));
        return page;
    }

    //查看审核护照
    public PassportModel getCheckPassportById(UserInfoForToken userInfo,String passprtId)throws PassportException{
        if (StringUtils.isEmpty(passprtId)){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
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
   public PageInfo<ValidationModel> getProblemPassPort(UserInfoForToken userInfo, ValidationModel model){
       if (StringUtils.isEmpty(model.getName())){
            model.setName(null);
       }
       if (StringUtils.isEmpty(model.getOrderNumber())){
           model.setOrderNumber(null);
       }
       if (StringUtils.isEmpty(model.getPassportEncoding())){
           model.setPassportEncoding(null);
       }
       if (StringUtils.isEmpty(model.getTelephoneNumber())){
           model.setTelephoneNumber(null);
       }

        //有问题护照
       List<ValidationModel> models=visaMapper.findProblePassWord(model.getOrderNumber(),model.getName(),model.getPassportEncoding(),model.getTelephoneNumber(),(model.getPageNum()-1)*model.getPageSize(),model.getPageSize());
       PageInfo page=new PageInfo();
       page.setPageSize(model.getPageSize());
       page.setPageNum(model.getPageNum());
       page.setTotal(visaMapper.countProblePassWord(model.getOrderNumber(),model.getName(),model.getPassportEncoding(),model.getTelephoneNumber()));
       page.setList(models);
       return page;
    }

    /**
     * 审核未通过护照
     * @param userInfo
     * @param model
     * @return
     */
    public PageInfo<ValidationModel> getPassPortByStatus(UserInfoForToken userInfo,ValidationModel model){
        if (StringUtils.isEmpty(model.getName())){
            model.setName(null);
        }
        if (StringUtils.isEmpty(model.getOrderNumber())){
            model.setOrderNumber(null);
        }
        if (StringUtils.isEmpty(model.getPassportEncoding())){
            model.setPassportEncoding(null);
        }
        if (StringUtils.isEmpty(model.getTelephoneNumber())){
            model.setTelephoneNumber(null);
        }
        List<ValidationModel> models=visaMapper.findPassPortByStatus(model.getOrderNumber(),model.getPassportEncoding(),model.getName(),model.getTelephoneNumber(),(model.getPageNum()-1)*model.getPageSize(),model.getPageSize());
        PageInfo page=new PageInfo();
        page.setPageSize(model.getPageSize());
        page.setPageNum(model.getPageNum());
        page.setTotal(visaMapper.countPassPortByStatus(model.getOrderNumber(),model.getPassportEncoding(),model.getName(),model.getTelephoneNumber()));
        page.setList(models);
        return page;
    }

    /**
     * 根据护照id获得护照信息（重审）
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
        PassPortImg passPortImg=passPortImgRepository.findByOrderNumberAndPassPortCode(expressReceipt.getOrderNumber(),passportInformation.getPassportEncoding());
        if(passPortImg!=null){
            passportInformation.setPicPath(passPortImg.getPicPath());
        }
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
        pe.setStatus(expressReceipt.getStatus());
        model.setExpressReceipt(expressReceipt);
        model.setEssential(pe);
        model.setInformation(passportInformation);
        return model;
    }

    /**
     * 审核护照（审核页）
     * @param userInfo
     * @param model
     * @throws PassportException
     */
    @Transactional(rollbackFor = Exception.class)
    public void examineAndVerifyPassPort(UserInfoForToken userInfo, VerifyPassPortModel model)throws PassportException{
        if(StringUtils.isEmpty(userInfo.getUserId())|| StringUtils.isEmpty(model.getPid())||StringUtils.isEmpty(model.getStatus())){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        String userId=userInfo.getUserId();
        BackUserInfo userInfo1=backUserInfoRepository.findByid(userId);
        synchronized (userId.intern()){
            //护照
            PassportInformation passportInformation=passportInformationRepository.findByIdIs(model.getPid());
            if (passportInformation==null){
                throw new PassportException(ResultCode.PASSPORT_NOEXIST_MSG);
            }
            //快递
            ExpressReceipt expressReceipt=expressReceiptRepository.findByOrderNumber(passportInformation.getOrderId());
            VisaOperationRecord visaOperationRecord=new VisaOperationRecord();//操作记录
            visaOperationRecord.setOperaterId(userInfo1.getId());
            visaOperationRecord.setOperaterName(userInfo1.getUserName());
            visaOperationRecord.setOrderNumber(passportInformation.getOrderId());
            visaOperationRecord.setPhone(expressReceipt.getTelephoneNumber());
            visaOperationRecord.setApplicantName(passportInformation.getName());
            if ("P".equalsIgnoreCase(model.getStatus()) && "W".equalsIgnoreCase(passportInformation.getStatus())){//未审核-审核通过
                passportInformation.setStatus("P");
                passportInformation.setIsExport("N");//是否导出
                passPortSupplementRelationRepository.deleteByPassId(model.getPid());//删除问题
                /*passportInformation.setCheckTime(new Date().getTime());
                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR)+Integer.valueOf(expressReceipt.getSchedule()));
                passportInformation.setExpireTime(calendar.getTimeInMillis());*/
                //SimpleDateFormat form1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                visaOperationRecord.setOperationName("审核：通过");
                passportInformationRepository.checkPassPort(Integer.valueOf(expressReceipt.getSchedule()),passportInformation.getId());
            }else if ("F".equalsIgnoreCase(model.getStatus())&&"W".equalsIgnoreCase(passportInformation.getStatus())){//未审核-审核不通过
                if (StringUtils.isEmpty(model.getProblem())){
                    throw new PassportException("缺少补充问题");
                }
                passportInformation.setStatus("F");
                PassportSupplementRelation relation=new PassportSupplementRelation();
                relation.setPassId(model.getPid());
                relation.setContentId(model.getProblem());
                relation.setIllustrated(model.getProblem());
                passPortSupplementRelationRepository.save(relation);
                visaOperationRecord.setOperationName("审核：不通过");
                passportInformationRepository.save(passportInformation);//修改状态
            }else {
                throw new PassportException("审核出错！");
            }
            visaOperationRecordRepository.save(visaOperationRecord);//保存操作记录
        }
    }

    /**
     * 发回重审
     * @param userInfo
     * @param model
     * @throws PassportException
     */
    @Transactional(rollbackFor = Exception.class)
    public void aRetrialPassPort(UserInfoForToken userInfo, VerifyPassPortModel model)throws PassportException{
        if(StringUtils.isEmpty(userInfo.getUserId())|| StringUtils.isEmpty(model.getPid())){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        String userId=userInfo.getUserId();
        BackUserInfo userInfo1=backUserInfoRepository.findByid(userId);
        synchronized (userId.intern()){
            //护照
            PassportInformation passportInformation=passportInformationRepository.findByIdIs(model.getPid());
            if (passportInformation==null){
                throw new PassportException(ResultCode.PASSPORT_NOEXIST_MSG);
            }
            //快递
            ExpressReceipt expressReceipt=expressReceiptRepository.findByOrderNumber(passportInformation.getOrderId());
            if(expressReceipt==null){
                throw new PassportException(ResultCode.EXPREES_NOEXIST_MSG);

            }
            VisaOperationRecord visaOperationRecord=new VisaOperationRecord();//操作记录
            visaOperationRecord.setOperaterId(userInfo1.getId());
            visaOperationRecord.setOperaterName(userInfo1.getUserName());
            visaOperationRecord.setOrderNumber(passportInformation.getOrderId());
            visaOperationRecord.setPhone(expressReceipt.getTelephoneNumber());
            visaOperationRecord.setApplicantName(passportInformation.getName());
            if(passportInformation.getStatus().equalsIgnoreCase("F")){//审核未通过
                passportInformation.setStatus("W");
                visaOperationRecord.setOperationName("审核：发回重审");
                passPortSupplementRelationRepository.deleteByPassId(model.getPid());//删除问题
            }else{
                throw new PassportException("发回审核出错！");
            }
            passportInformationRepository.save(passportInformation);//修改状态
            visaOperationRecordRepository.save(visaOperationRecord);//保存操作记录
        }
    }

    /**
     * 签证进度表
     * @param userInfo
     * @param model
     * @return
     */
    public PageInfo<ValidationModel> getPassPortByPass(UserInfoForToken userInfo,ValidationModel model)throws PassportException{
        if (StringUtils.isEmpty(model.getName())){
            model.setName(null);
        }
        if (StringUtils.isEmpty(model.getOrderNumber())){
            model.setOrderNumber(null);
        }
        if (StringUtils.isEmpty(model.getPassportEncoding())){
            model.setPassportEncoding(null);
        }
        if (StringUtils.isEmpty(model.getTelephoneNumber())){
            model.setTelephoneNumber(null);
        }
        if (StringUtils.isEmpty(model.getSendStatus())){
            model.setSendStatus(null);
        }
        //List<ValidationModel> models=visaMapper.findPassPortByPass(model.getOrderNumber(),model.getName(),model.getPassportEncoding(),model.getTelephoneNumber(),model.getSendStatus(),(model.getPageNum()-1)*model.getPageSize(),model.getPageSize());
        List<ValidationModel> models=visaMapper.findPassPortByPass2(model.getOrderNumber(),model.getName(),model.getPassportEncoding(),model.getTelephoneNumber(),model.getSendStatus(),(model.getPageNum()-1)*model.getPageSize(),model.getPageSize());
        PageInfo page=new PageInfo();
        page.setPageSize(model.getPageSize());
        page.setPageNum(model.getPageNum());
        page.setTotal(visaMapper.countPassPortByPass(model.getOrderNumber(),model.getName(),model.getPassportEncoding(),model.getTelephoneNumber(),model.getSendStatus()));
        page.setList(models);
        return page;
    }

    /**
     * 送签操作
     * @param userInfo
     * @param pid
     */
    @Transactional(rollbackFor = Exception.class)
    public void sendVisa(UserInfoForToken userInfo,String pid)throws PassportException{
        if (StringUtils.isEmpty(pid)) {
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        String userId=userInfo.getUserId();
        BackUserInfo userInfo1=backUserInfoRepository.findByid(userId);
        synchronized (userId.intern()){
            PassportInformation passportInformation=passportInformationRepository.findByIdIs(pid);
            if(passportInformation==null){
                throw new PassportException(ResultCode.PASSPORT_NOEXIST_MSG);
            }
            //快递
            ExpressReceipt expressReceipt=expressReceiptRepository.findByOrderNumber(passportInformation.getOrderId());
            if(expressReceipt==null){
                throw new PassportException(ResultCode.EXPREES_NOEXIST_MSG);
            }
            if(passportInformation.getStatus().equalsIgnoreCase("P")&&passportInformation.getSendStatus().equalsIgnoreCase("N")){//待送签
                VisaOperationRecord visaOperationRecord=new VisaOperationRecord();//操作记录
                visaOperationRecord.setOperaterId(userInfo1.getId());
                visaOperationRecord.setOperaterName(userInfo1.getUserName());
                visaOperationRecord.setOrderNumber(passportInformation.getOrderId());
                visaOperationRecord.setPhone(expressReceipt.getTelephoneNumber());
                visaOperationRecord.setApplicantName(passportInformation.getName());
                passportInformation.setSendStatus("S");//送签
                passportInformationRepository.save(passportInformation);
                visaOperationRecord.setOperationName("操作：送签");
                visaOperationRecordRepository.save(visaOperationRecord);//保存操作记录
            }else {
                throw new PassportException("送签失败");
            }
        }
    }

    /**
     * 出签拒签
     * @param userInfo
     * @param pid
     * @param status
     * @throws PassportException
     */
    @Transactional(rollbackFor = Exception.class)
    public void outAndRefuseVisa(UserInfoForToken userInfo,String pid,String status)throws PassportException{
        if (StringUtils.isEmpty(pid)||StringUtils.isEmpty(status)) {
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        String userId=userInfo.getUserId();
        BackUserInfo userInfo1=backUserInfoRepository.findByid(userId);
        synchronized (userId.intern()){
            PassportInformation passportInformation=passportInformationRepository.findByIdIs(pid);
            if(passportInformation==null){
                throw new PassportException(ResultCode.PASSPORT_NOEXIST_MSG);
            }
            //快递
            ExpressReceipt expressReceipt=expressReceiptRepository.findByOrderNumber(passportInformation.getOrderId());
            if(expressReceipt==null){
                throw new PassportException(ResultCode.EXPREES_NOEXIST_MSG);
            }
            if(passportInformation.getStatus().equalsIgnoreCase("P")&&passportInformation.getSendStatus().equalsIgnoreCase("S")){//已送签
                VisaOperationRecord visaOperationRecord=new VisaOperationRecord();//操作记录
                visaOperationRecord.setOperaterId(userInfo1.getId());
                visaOperationRecord.setOperaterName(userInfo1.getUserName());
                visaOperationRecord.setOrderNumber(passportInformation.getOrderId());
                visaOperationRecord.setPhone(expressReceipt.getTelephoneNumber());
                visaOperationRecord.setApplicantName(passportInformation.getName());
                if (status.equalsIgnoreCase("O")){
                    passportInformation.setSendStatus("O");//出签
                    passportInformation.setOutSignTime(new Date().getTime());
                    visaOperationRecord.setOperationName("操作：出签");
                }else if(status.equalsIgnoreCase("R")){//拒签
                    passportInformation.setSendStatus("R");
                    visaOperationRecord.setOperationName("操作：拒签");
                }else {
                    throw new PassportException("无效操作");
                }
                passportInformation.setIsSendBack("N");//未寄回
                passportInformationRepository.save(passportInformation);
                visaOperationRecordRepository.save(visaOperationRecord);//保存操作记录
            }else {
                throw new PassportException("送签失败");
            }
        }
    }

    /**
     * 寄回
     * @param userInfo
     * @param model
     * @throws PassportException
     */
    @Transactional(rollbackFor = Exception.class)
    public LogisticsModel sendBackVisa(UserInfoForToken userInfo,LogisticsModel model)throws PassportException{
        if (StringUtils.isEmpty(model.getPassportId()) || StringUtils.isEmpty(model.getGet())
                ||StringUtils.isEmpty(model.getGetAddress()) || StringUtils.isEmpty(model.getGetPhone())
                ||StringUtils.isEmpty(model.getSendAddress()) || StringUtils.isEmpty(model.getSender())
                ||StringUtils.isEmpty(model.getSenderPhone())) {
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        String userId=userInfo.getUserId();
        BackUserInfo userInfo1=backUserInfoRepository.findByid(userId);
        synchronized (userId.intern()){
            PassportInformation passportInformation=passportInformationRepository.findByIdIs(model.getPassportId());
            if(passportInformation==null){
                throw new PassportException(ResultCode.PASSPORT_NOEXIST_MSG);
            }
            ExpressReceipt expressReceipt=expressReceiptRepository.findByOrderNumber(passportInformation.getOrderId());
            if(expressReceipt==null){
                throw new PassportException(ResultCode.EXPREES_NOEXIST_MSG);
            }
            if(passportInformation.getStatus().equalsIgnoreCase("P")&&passportInformation.getSendStatus().equalsIgnoreCase("O")||passportInformation.getSendStatus().equalsIgnoreCase("R")){
                passportInformation.setIsSendBack("SB");
                //寄回地址
                if(!StringUtils.isEmpty(model.getGetAddress())){
                    passportInformation.setReturnAddress(model.getGetAddress());
                }
                VisaOperationRecord visaOperationRecord=new VisaOperationRecord();//操作记录
                visaOperationRecord.setOperaterId(userInfo1.getId());
                visaOperationRecord.setOperaterName(userInfo1.getUserName());
                visaOperationRecord.setOrderNumber(passportInformation.getOrderId());
                visaOperationRecord.setPhone(expressReceipt.getTelephoneNumber());
                visaOperationRecord.setApplicantName(passportInformation.getName());
                visaOperationRecord.setOperationName("操作：寄回");
                passportInformationRepository.saveAndFlush(passportInformation);
                visaOperationRecordRepository.save(visaOperationRecord);//保存操作记录
                /*if(1==1){
                    throw new PassportException("测试事物！");
                }*/
                Logistics logistics=MappingEntityModelCoverter.CONVERTERFROMLOGISTICSMODELTOLOGISTIC(model);
                logistics=shunFengService.addOrder(logistics);
                if(!StringUtils.isEmpty(logistics.getSfExpressId())){
                    model.setSfExpress(logistics.getSfExpressId());
                    model.setOrderId(logistics.getOrderId());
                    passportInformation.setSfExpressId(logistics.getSfExpressId());
                    passportInformationRepository.save(passportInformation);
                }else {
                    throw new PassportException("顺丰寄件失败！");
                }
            }else {
                throw new PassportException("寄回失败");
            }
            model.setSendBackNum(1);
            return model;
        }
    }

    /**
     * 一同寄回
     * @param userInfo
     * @param model
     * @throws PassportException
     */
    @Transactional(rollbackFor = Exception.class)
    public LogisticsModel sendBackTogether(UserInfoForToken userInfo,LogisticsModel model)throws PassportException{
        if (StringUtils.isEmpty(model.getPassportId()) || StringUtils.isEmpty(model.getGet())
                ||StringUtils.isEmpty(model.getGetAddress()) || StringUtils.isEmpty(model.getGetPhone())
                ||StringUtils.isEmpty(model.getSendAddress()) || StringUtils.isEmpty(model.getSender())
                ||StringUtils.isEmpty(model.getSenderPhone())) {
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        String userId=userInfo.getUserId();
        BackUserInfo userInfo1=backUserInfoRepository.findByid(userId);
        synchronized (userId.intern()){
            PassportInformation passportInformation=passportInformationRepository.findByIdIs(model.getPassportId());
            if(passportInformation==null){
                throw new PassportException(ResultCode.PASSPORT_NOEXIST_MSG);
            }
            ExpressReceipt expressReceipt=expressReceiptRepository.findByOrderNumber(passportInformation.getOrderId());
            if(expressReceipt==null){
                throw new PassportException(ResultCode.EXPREES_NOEXIST_MSG);
            }
            //查询快件下护照
            List<VisaPassportRelation> relations=visaPassportRelationRepository.findByVid(expressReceipt.getId());
            List<String> passIds=new ArrayList<>();
            relations.forEach(visaPassportRelation -> {
                passIds.add(visaPassportRelation.getPassId());
            });
            //查询已出签和拒签的护照
            List<PassportInformation> passportInformations=passportInformationRepository.findByIdInAndSendStatusOrSendStatus(passIds,"O","R");
            List<PassportInformation> passPIs=new ArrayList<>();
            if (passportInformations!=null && passportInformations.size()!=0){
                for (PassportInformation p:passportInformations) {
                    if(!p.getIsSendBack().equalsIgnoreCase("SB")){
                        if(p.getStatus().equalsIgnoreCase("P")){
                            if(p.getSendStatus().equalsIgnoreCase("O")||p.getSendStatus().equalsIgnoreCase("R")){
                                //p.setSendStatus("SB");
                                p.setIsSendBack("SB");
                                //寄回地址
                                if(!StringUtils.isEmpty(model.getGetAddress())){
                                    p.setReturnAddress(model.getGetAddress());
                                }
                                passPIs.add(p);
                            }else {
                                throw new PassportException("一同寄回失败");
                            }
                        }
                    }
                }

            }else{
                throw new PassportException("暂无可一同寄回护照！");
            }
            passportInformationRepository.saveAll(passPIs);
            Logistics logistics=MappingEntityModelCoverter.CONVERTERFROMLOGISTICSMODELTOLOGISTIC(model);
            logistics=shunFengService.addOrder(logistics);
            if(!StringUtils.isEmpty(logistics.getSfExpressId())){
                for (PassportInformation p:passPIs) {
                    p.setSfExpressId(logistics.getSfExpressId());
                }
            }
            VisaOperationRecord visaOperationRecord=new VisaOperationRecord();//操作记录
            visaOperationRecord.setOperaterId(userInfo1.getId());
            visaOperationRecord.setOperaterName(userInfo1.getUserName());
            visaOperationRecord.setOrderNumber(expressReceipt.getOrderNumber());
            visaOperationRecord.setPhone(expressReceipt.getTelephoneNumber());
            visaOperationRecord.setApplicantName(expressReceipt.getSignatory());
            visaOperationRecord.setOperationName("操作：一同寄回");
            passportInformationRepository.saveAll(passPIs);
            visaOperationRecordRepository.save(visaOperationRecord);//保存操作记录
            model.setSfExpress(logistics.getSfExpressId());
            model.setSendBackNum(passPIs.size());
            model.setOrderId(logistics.getOrderId());
            return model;
        }
    }

    /**
     *  取消订单
     * @param userInfo
     * @param passportId
     * @return
     * @throws PassportException
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelSFOrder(UserInfoForToken userInfo,String passportId)throws PassportException{
        if (StringUtils.isEmpty(passportId)) {
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        PassportInformation passportInformation=passportInformationRepository.findByIdIs(passportId);
        if(passportInformation!=null){
            Logistics logistics=logisticsRepository.findBySfExpressId(passportInformation.getSfExpressId());//根据顺丰快件id查找快件
            if(logistics != null){
                int num=shunFengService.exitOrder(logistics.getOrderId());
                if (num==1){//成功
                    List<PassportInformation> passportInformations=passportInformationRepository.findBySfExpressId(logistics.getSfExpressId());
                    for (PassportInformation pi:passportInformations) {
                        pi.setIsSendBack("N");
                        pi.setSfExpressId(null);
                    }
                    passportInformationRepository.saveAll(passportInformations);
                    return true;
                }
            }else {
                throw new PassportException("查询不到该订单！");
            }
        }
        return false;
    }

    /**
     * excel导入护照
     * @param file
     * @param userInfo
     * @param expressReceiptId
     * @return
     * @throws PassportException
     */
    public ReadExcelModel addPassPortByExcel(MultipartFile file, UserInfoForToken userInfo, String expressReceiptId) throws PassportException {
        if (StringUtils.isEmpty(expressReceiptId)) {
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        ExpressReceipt expressReceipt=expressReceiptRepository.findByIdIs(expressReceiptId);
        if(expressReceipt==null){
            throw new PassportException(ResultCode.EXPREES_NOEXIST_MSG);
        }
        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(".xls")) {
            System.out.println("文件不是.xls类型");
        }
        try {
            // 得到这个excel表格对象
            HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            List<String> errorRowNum = new ArrayList<>();
            List<PassportInformation> informations = new ArrayList<>();
            // 得到这个excel表格的sheet数量
            int numberOfSheets = workbook.getNumberOfSheets();
            HSSFSheet sheet=workbook.getSheet("护照");//读取名为护照的sheet
            //得到sheet里的总行数
            int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
            for (int j = 1; j < physicalNumberOfRows; j++) {
                HSSFRow row = sheet.getRow(j);
                PassportInformation passportInformation;
                try {
                    passportInformation = saveRowPassPort(row, expressReceipt);
                } catch (PassportException e) {
                    errorRowNum.add((j + 1) + " ");
                    continue;
                }
                informations.add(passportInformation);
            }
            /*for (int i = 0; i < numberOfSheets; i++) {
                //得到sheet
                HSSFSheet sheet = workbook.getSheetAt(i);
                //得到sheet里的总行数
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                for (int j = 1; j < physicalNumberOfRows; j++) {
                    HSSFRow row = sheet.getRow(j);
                    PassportInformation passportInformation;
                    try {
                        passportInformation = saveRowPassPort(row, expressReceipt);
                    } catch (PassportException e) {
                        errorRowNum.add((j + 1) + " ");
                        continue;
                    }
                    informations.add(passportInformation);
                }
            }*/
            ReadExcelModel model=new ReadExcelModel();
            model.setInformationList(informations);
            model.setErrorRow(JSONObject.toJSONString(errorRowNum.toArray()));
            return model;
        } catch (IOException e) {
            throw new PassportException(ResultCode.EXCEL_IMPORT_MSG);
        }
    }

    private PassportInformation saveRowPassPort(HSSFRow row, ExpressReceipt expressReceipt) throws PassportException {
        PassportInformation passportInformation=new PassportInformation();
        String travel="";//旅行社
        String visaType="";//签证种类
        String remarks="";//备注
        String name="";
        String engishName="";
        String sex="";
        long birthDay=0;//出生日期
        String habitation="";//居住地
        String birthPlace="";//出生地
        String passportEncoding="";//护照编码
        long signDate=0;//签发日期
        long expiryDate=0;//有效期
        String signAddress="";//签发地
        int physicalNumberOfCells = row.getPhysicalNumberOfCells();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < physicalNumberOfCells; i++) {
            switch (i) {
                case 0:
                    travel = getCellValue(row.getCell(i));
                    break;
                case 1:
                    name = getCellValue(row.getCell(i));
                    break;
                case 2:
                    engishName = getCellValue(row.getCell(i));
                    break;
                case 3:
                    /*sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date=getCellDate(row.getCell(i));
                    birthDay = TimeUtils.startTime(sdf.format(date));*/
                    sex=getCellValue(row.getCell(i));
                    break;
                case 4:
                    passportEncoding = getCellString(row.getCell(i));
                    break;
                case 5:
                    birthDay = TimeUtils.startTime(getCellValue(row.getCell(i)));
                    //birthDay=Long.valueOf(getCellValue(row.getCell(i)));
                    break;
                case 6:
                    visaType= getCellValue(row.getCell(i));
                    break;
                case 7:
                    remarks= getCellValue(row.getCell(i));
                    break;
                case 8:
                    birthPlace= getCellValue(row.getCell(i));
                    break;
                case 9:
                    signAddress= getCellValue(row.getCell(i));
                    break;
                case 10:
                    signDate = TimeUtils.startTime(getCellValue(row.getCell(i)));
                    //signDate= Long.valueOf(getCellValue(row.getCell(i)));
                    break;
                case 11:
                    expiryDate = TimeUtils.startTime(getCellValue(row.getCell(i)));
                    //expiryDate= Long.valueOf(getCellValue(row.getCell(i)));
                    break;
                default:
                    break;
            }
        }
        passportInformation.setStatus("W");
        passportInformation.setOrderId(expressReceipt.getOrderNumber());
        passportInformation.setBirthDay(birthDay);
        passportInformation.setCreateTime(new Date().getTime());
        passportInformation.setBirthPlace(birthPlace);
        passportInformation.setExpiryDate(expiryDate);
        passportInformation.setHabitation(habitation);
        passportInformation.setName(name);
        passportInformation.setEnglishName(engishName);
        passportInformation.setSex(sex);
        passportInformation.setTelephoneNumber(expressReceipt.getTelephoneNumber());
        passportInformation.setPassportEncoding(passportEncoding);
        passportInformation.setReturnAddress(expressReceipt.getReturnAddress());
        passportInformation.setSignAddress(signAddress);
        passportInformation.setVisaType(visaType);
        passportInformation.setTravel(travel);
        passportInformation.setRemarks(remarks);
        passportInformation.setSignDate(signDate);
        return passportInformation;
    }

    private String getCellString(HSSFCell cell) throws PassportException {
        if (null == cell) {
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        return cell.getStringCellValue();
    }

    private Date getCellDate(HSSFCell cell) throws PassportException {
        if (null == cell) {
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        return cell.getDateCellValue();
    }

    private String getCellNumber(HSSFCell cell) throws PassportException {
        if (null == cell) {
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        return NumberToTextConverter.toText(cell.getNumericCellValue());
    }

    /**
     * 判断单元格类型（日期，字符串，数字）
     * @param cell
     * @return
     * @throws PassportException
     */
    private String getCellValue(HSSFCell cell) throws PassportException {
        if (null == cell) {
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        switch (cell.getCellType()){
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if(HSSFDateUtil.isCellDateFormatted(cell)){
                    SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
                    Date date= cell.getDateCellValue();
                    return String.valueOf(TimeUtils.startTime(sdf.format(date)));
                }else {
                    return NumberToTextConverter.toText(cell.getNumericCellValue());
                }
        }
        return "";
    }

    /**
     * 导出护照列表
     * @param userInfo
     * @return
     */
    public PageInfo<StatisticsModel> getExpressReceiptStatistic(UserInfoForToken userInfo,RequireVisaModel requireVisaModel)throws PassportException{
        if(StringUtils.isEmpty(requireVisaModel.getTimeStart()) || StringUtils.isEmpty(requireVisaModel.getTimeEnd()) ){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        if (StringUtils.isEmpty(requireVisaModel.getOrderNumber())){
            requireVisaModel.setOrderNumber(null);
        }
        if (StringUtils.isEmpty(requireVisaModel.getSignatory())){
            requireVisaModel.setSignatory(null);
        }
        if (StringUtils.isEmpty(requireVisaModel.getTelephoneNumber())){
            requireVisaModel.setTelephoneNumber(null);
        }
        if (StringUtils.isEmpty(requireVisaModel.getSchedule())){
            requireVisaModel.setSchedule(null);
        }
        //List<ExpressReceipt> expressReceipts=expressReceiptRepository.findByIsError("0");//查询无异常快件
        List<ExpressReceipt> expressReceipts=visaMapper.findByIsErrorAndCondition(requireVisaModel.getOrderNumber(),requireVisaModel.getSignatory(),requireVisaModel.getTelephoneNumber(),requireVisaModel.getSchedule(),TimeUtils.startTime(requireVisaModel.getTimeStart()),TimeUtils.endTime(requireVisaModel.getTimeEnd()),(requireVisaModel.getPageNum()-1)*requireVisaModel.getPageSize(),requireVisaModel.getPageSize());
        List<StatisticsModel> models=new ArrayList<>();
        if(expressReceipts!=null && expressReceipts.size()!=0){
            for (ExpressReceipt ex:expressReceipts) {
                Integer passportNum=0;//护照本数
                Integer egisNum=0;//审核通过数
                Integer waitNum=0;//待审核数
                Integer failNum=0;//审核不通过
                Integer sendNum=0;//已送签数
                Integer refuseNum=0;//拒签数
                Integer outNum=0;//出签数
                Integer sendBackNum=0;//寄回数
                Integer exprotNum=0;//导出数
                List<PassportInformation> passportInformations=passportInformationRepository.findByOrderId(ex.getOrderNumber());
                if(passportInformations!=null && passportInformations.size()!=0){
                    passportNum=passportInformations.size();
                    for (PassportInformation pi:passportInformations){
                        if((!StringUtils.isEmpty(pi.getStatus())) && pi.getStatus().equalsIgnoreCase("P")){
                            egisNum+=1;
                        }
                        if((!StringUtils.isEmpty(pi.getStatus())) && pi.getStatus().equalsIgnoreCase("W")){
                            waitNum+=1;
                        }
                        if((!StringUtils.isEmpty(pi.getStatus())) && pi.getStatus().equalsIgnoreCase("F")){
                            failNum+=1;
                        }
                        if((!StringUtils.isEmpty(pi.getSendStatus())) && pi.getSendStatus().equalsIgnoreCase("S")){
                            sendNum+=1;
                        }
                        if((!StringUtils.isEmpty(pi.getSendStatus())) && pi.getSendStatus().equalsIgnoreCase("R")){
                            refuseNum+=1;
                        }
                        if((!StringUtils.isEmpty(pi.getSendStatus())) && pi.getSendStatus().equalsIgnoreCase("O")){
                            outNum+=1;
                        }
                        if((!StringUtils.isEmpty(pi.getIsSendBack())) && pi.getIsSendBack().equalsIgnoreCase("SB")){
                            sendBackNum+=1;
                        }
                        if((!StringUtils.isEmpty(pi.getIsExport())) && pi.getIsExport().equalsIgnoreCase("Y")){
                            exprotNum+=1;
                        }
                    }
                }
                StatisticsModel model=new StatisticsModel();
                model.setEgisNum(egisNum);
                model.setExprotNum(exprotNum);
                model.setFailNum(failNum);
                model.setId(ex.getId());
                model.setName(ex.getSignatory());
                model.setOrderNumber(ex.getOrderNumber());
                model.setOutNum(outNum);
                model.setPassportNum(passportNum);
                model.setRefuseNum(refuseNum);
                model.setSchedule(ex.getSchedule());
                model.setSendBackNum(sendBackNum);
                model.setSendNum(sendNum);
                model.setSignTime(ex.getCreateTime());
                models.add(model);
            }
        }
        PageInfo page=new PageInfo();
        page.setPageSize(requireVisaModel.getPageSize());
        page.setPageNum(requireVisaModel.getPageNum());
        page.setTotal(visaMapper.countByIsErrorAndCondition(requireVisaModel.getOrderNumber(),requireVisaModel.getSignatory(),requireVisaModel.getTelephoneNumber(),requireVisaModel.getSchedule(),TimeUtils.startTime(requireVisaModel.getTimeStart()),TimeUtils.endTime(requireVisaModel.getTimeEnd())));
        page.setList(models);
        return page;
    }

    /**
     * 根据状态查询订单号（未导出过）
     * @param userInfo
     * @param orderIds
     * @return
     * @throws PassportException
     */
    @Transactional(rollbackFor = Exception.class)
    public List<PassportInformation> getAllPassPortInformationByOrderId(UserInfoForToken userInfo,List<String> orderIds)throws PassportException{
        if(orderIds==null && orderIds.size()==0){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        List<PassportInformation> passportInformations=passportInformationRepository.findByStatusAndOrderIdInAndIsExportOrderByOrderId("P",orderIds,"N");//查询审核通过并且未导出的护照
        for (PassportInformation pi:passportInformations){
            pi.setIsExport("Y");
        }
        passportInformationRepository.saveAll(passportInformations);
        return passportInformations;
    }

    /**
     * 根据状态查询订单号（全部）
     * @param userInfo
     * @param orderIds
     * @return
     * @throws PassportException
     */
    public List<PassportInformation> getAllPassPortInformationByOrderId2(UserInfoForToken userInfo,List<String> orderIds)throws PassportException{
        if(orderIds==null && orderIds.size()==0){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        List<PassportInformation> passportInformations=passportInformationRepository.findByStatusAndOrderIdIn("P",orderIds);//查询审核通过的护照
        return passportInformations;
    }

    /**
     * 根据护照查询寄回信息
     * @param userInfo
     * @param passportId
     * @return
     * @throws PassportException
     */
    public LogisticsPassPortModel getsendBackMessageByPassPortId(UserInfoForToken userInfo,String passportId)throws PassportException{
        if(StringUtils.isEmpty(passportId)){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        PassportInformation passportInformations=passportInformationRepository.findByIdIs(passportId);//查询审核通过的护照
        if (passportInformations!=null){
            if(passportInformations.getSendStatus().equalsIgnoreCase("O")&&passportInformations.getIsSendBack().equalsIgnoreCase("SB")
                    ||passportInformations.getSendStatus().equalsIgnoreCase("R")&&passportInformations.getIsSendBack().equalsIgnoreCase("SB")){
                Logistics logistics=logisticsRepository.findBySfExpressId(passportInformations.getSfExpressId());
                VisaPassportRelation visaPassportRelation=visaPassportRelationRepository.findByPassId(passportInformations.getId());
                ExpressReceipt expressReceipt=expressReceiptRepository.findByIdIs(visaPassportRelation.getVid());
                LogisticsPassPortModel model=new LogisticsPassPortModel();
                model.setExpressReceipt(expressReceipt);
                model.setLogistics(logistics);
                model.setPassportInformation(passportInformations);
                if(logistics==null&&expressReceipt==null){
                    model=null;
                    return model;
                }
                return model;
            }else {
                throw new PassportException("该护照尚未出签或寄回！");
            }
        }else {
            throw new PassportException(ResultCode.PASSPORT_NOEXIST_MSG);
        }
    }
}
