package cn.com.yaohao.visa.service;

import cn.com.yaohao.visa.constant.ResultCode;
import cn.com.yaohao.visa.dao.mapper.IVisaMapper;
import cn.com.yaohao.visa.entity.Logistics;
import cn.com.yaohao.visa.exception.PassportException;
import cn.com.yaohao.visa.model.LogisticsModel;
import cn.com.yaohao.visa.repository.ILogisticsRepository;
import com.alibaba.fastjson.JSONObject;
import com.sf.csim.express.service.CallExpressServiceTools;
import net.sf.json.xml.XMLSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class ShunFengService {

    @Value("${fengqiao.reqURL}")
    private String reqURL;        // 请求路径固定的，文档中有写
    @Value("${fengqiao.clientCode}")
    private String clientCode;      //商户号
    @Value("${fengqiao.checkWord}")
    private String checkWord;       //验证码

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private IVisaMapper visaMapper;
    @Resource
    private ILogisticsRepository logisticsRepository;

    /**
     * 下订单
     * @param logisticsModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Logistics addOrder(Logistics logisticsModel) throws PassportException {
        // 生成顺丰下单订单号
        //logistics.setOrderId(UUID.randomUUID().toString().replaceAll("-", ""));
        //logisticsModel.setOrderId("SFKD-20200317000001");
        Logistics numbers=createSerialNumber();
        logisticsModel.setOrderId(numbers.getOrderId());
        logisticsModel.setCode(numbers.getCode());
        StringBuffer buffer = new StringBuffer();
        buffer.append("<Request service='OrderService' lang='zh-CN' >");
        buffer.append("<Head>");
        buffer.append(clientCode);
        buffer.append("</Head>");
        buffer.append("<Body>");
        buffer.append("<Order ");
        buffer.append("orderid='").append(logisticsModel.getOrderId()).append("' ");
        //寄件方信息
        buffer.append("j_contact='").append(logisticsModel.getSender()).append("' ");//寄件方联系人
        buffer.append("j_tel='").append(logisticsModel.getSenderPhone()).append("' ");//寄件方联系电话
        buffer.append("j_address='").append(logisticsModel.getSendAddress()).append("' ");//	寄件方详细地址
        //收件方信息
        buffer.append("d_contact='").append(logisticsModel.getGetP()).append("' ");//到件方联系人
        buffer.append("d_tel='").append(logisticsModel.getGetPhone()).append("' ");//到件方联系电话
        buffer.append("d_address='").append(logisticsModel.getGetAddress()).append("' ");//	到件方详细地址
        //buffer.append("sendstarttime='").append(DateUtils.dateTimeToDateString(logistics.getMakeAnAppointment())).append("' ");
        buffer.append("parcel_quantity='1' ");//包裹数
        buffer.append("is_docall ='1' ");   //特别重要(是否要求通过手持终端通知顺丰
        buffer.append("pay_method='2' >");//付款方式:1:寄方付2:收方付3:第三方付
        //buffer.append("custid ='").append(custId).append("' >");
        buffer.append("</Order>");
        buffer.append("</Body>");
        buffer.append("</Request>");
        String send = send(buffer.toString());
        JSONObject resp = JSONObject.parseObject(send(buffer.toString()));
        JSONObject resp1 = JSONObject.parseObject(send);
        logger.info("---------顺丰返回数据-----");
        logger.info(send);
        String code = resp1.getString("Head");
        // 接口请求成功
        if (code.equals("OK")) {
            JSONObject orderResponse = resp1.getJSONObject("Body").getJSONObject("OrderResponse");
            // 下单返回结果  1：人工确认  2：可收派  3：不可以收派
            String filter_result = orderResponse.getString("@filter_result");
            // 顺丰运单号
            String mailno = orderResponse.getString("@mailno");
            logger.info("----------顺丰请求成功-------，打印订单号："+mailno);
            if (filter_result.equals("2")) {
                logisticsModel.setLogisticsNumber(filter_result);
                logisticsModel.setSfExpressId(mailno);
            }
            logisticsModel.setMessage("快递下单成功");
            logisticsModel.setCreateTime(new Date().getTime());
            logisticsRepository.save(logisticsModel);
        }else {
            logger.info("----------顺丰请求失败-----");
            String error = resp1.getString("ERROR");
            logger.info(error);
            logisticsModel.setMessage(error);
            throw new PassportException(error);//抛出异常
        }
        return logisticsModel;
    }

    @SuppressWarnings("static-access")
    private String send(String reqXml){
        CallExpressServiceTools client = CallExpressServiceTools.getInstance();
        String respXml = client.callSfExpressServiceByCSIM(reqURL, reqXml, clientCode, checkWord);
        if (reqXml == null) {
            logger.error("顺丰下单接口异常，调用失败");
            return null;
        }
        XMLSerializer xmlSerializer = new XMLSerializer();
        return xmlSerializer.read(respXml).toString();
    }

    /**
     * 取消订单
     * @param logisticsModel
     * @return
     */
    public int exitOrder(Logistics logisticsModel) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<Request service='OrderConfirmService' lang='zh-CN' >");
        buffer.append("<Head>");
        buffer.append(clientCode);
        buffer.append("</Head>");
        buffer.append("<Body>");
        buffer.append("<OrderConfirm ");
        buffer.append("orderid='").append(logisticsModel.getOrderId()).append("' ");
        buffer.append("dealtype='2' >");
        buffer.append("</OrderConfirm>");
        buffer.append("</Body>");
        buffer.append("</Request>");

        JSONObject resp = JSONObject.parseObject(send(buffer.toString()));

        String code = resp.getString("Head");
        // 接口请求成功
        if (code.equals("OK")) {
            JSONObject orderConfirmResponse = resp.getJSONObject("Body").getJSONObject("OrderConfirmResponse");
            // 下单返回结果  1：客户订单号与顺丰运单不匹配    2 ：操作成功
            String res_status = orderConfirmResponse.getString("@res_status");
            if (res_status.equals("2")) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * 查询物流信息
     * @param logisticsModel
     * @return
     */
    public String getOrderStatus(Logistics logisticsModel) {

        StringBuffer buffer = new StringBuffer();

        buffer.append("<Request service='RouteService' lang='zh-CN' >");
        buffer.append("<Head>");
        buffer.append(clientCode);
        buffer.append("</Head>");
        buffer.append("<Body>");
        buffer.append("<RouteRequest tracking_type='1' method_type='1' tracking_number='");
        buffer.append(logisticsModel.getLogisticsNumber()).append("' /></Body></Request>");
        String send = send(buffer.toString());
        System.out.println(send);
        JSONObject resp = JSONObject.parseObject(send(buffer.toString()));

        String code = resp.getString("Head");
        // 接口请求成功
	  /*  if (code.equals("OK")) {
	    	return resp.getJSONObject("Body").getJSONArray("RouteResponse").toString();
	    }*/
        return code;
    }

    /**
     * 查询订单号
     * @param model
     * @return
     */
    public String getOrderSearchServiceRequestXml(LogisticsModel model) {
        String orderNo = model.getOrderId();
        StringBuffer buffer = new StringBuffer();
        buffer.append("<Request service='OrderSearchService' lang='zh-CN'>");
        buffer.append("<Head>" + clientCode + "</Head>");
        buffer.append("<Body>");
        buffer.append("<OrderSearch").append(" ");
        buffer.append("orderid='" + orderNo + "'").append(" /> ");
        buffer.append("</Body>");
        buffer.append("</Request>");
        String send = send(buffer.toString());
        JSONObject resp = JSONObject.parseObject(send(buffer.toString()));
        JSONObject resp1 = JSONObject.parseObject(send);
        System.out.println(send);
        String code = resp1.getString("Head");
        return send;
    }

    /**
     * 生成流水号
     * @return
     */
    private Logistics createSerialNumber(){
        String pre="SFKD";
        SimpleDateFormat dmfot = new SimpleDateFormat("yyyyMMdd");
        //截取当前时间作为流水号
        String preCode = dmfot.format(new Date());
        DecimalFormat num=new DecimalFormat("00000");
        //获取最大编号
        String maxCode = visaMapper.getMaxCodeByDate(getCurrentDay());
        Logistics logistics=new Logistics();
        if(maxCode != null && !"null".equals(maxCode)){
            //对流水号截取后三位
            String code = maxCode.substring(maxCode.length()-5,maxCode.length());
            //例如后三位为002，通过以下步骤，将会变为003
            int count = Integer.valueOf(code)+1;
            String number = num.format(count);
            logistics.setOrderId( pre+ "-" + preCode + number);
            logistics.setCode( preCode + number);
            return logistics;
        }else {
            logistics.setOrderId( pre+ "-" + preCode + "00001");
            logistics.setCode( preCode +  "00001");
            return logistics;
        }
    }

    /**
     * 返回当前时间零点
     * @return
     */
    public static String getCurrentDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date date = c.getTime();
        String currentDay = format.format(date);
        return currentDay;
    }
}
