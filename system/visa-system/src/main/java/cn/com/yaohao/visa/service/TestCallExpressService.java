package cn.com.yaohao.visa.service;

import com.sf.csim.express.service.CallExpressServiceTools;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.InputStream;


public class TestCallExpressService {
    @SuppressWarnings("static-access")
    public static void main(String[] args) {
        String reqXml = "";
        try {
            @SuppressWarnings("resource")
            InputStream is = new FileInputStream("HYTXGJLX");//下订单
            //     InputStream is = new FileInputStream("2.order_query.txt");//查订单
            //     InputStream is = new FileInputStream("3.order_confirm.txt");//订单取消
            //     InputStream is = new FileInputStream("4.order_filter.txt");//订单筛选
            //     InputStream is = new FileInputStream("5.route_queryByMailNo.txt");//     路由查询-通过运单号
            //     InputStream is = new FileInputStream("5.route_queryByOrderNo.txt");// 路由查询-通过订单号
            //     InputStream is = new FileInputStream("7.orderZD.txt");  //子单号申请
            byte[] bs = new byte[is.available()];
            is.read(bs);
            reqXml = new String(bs);
        } catch (Exception e) {
        }
        //丰桥平台公共测试账号
        //SLKJ2019
        //FBIqMkZjzxbsZgo7jTpeq7PD8CVzLT4Q
        String reqURL = "https://bsp-oisp.sf-express.com/bsp-oisp/sfexpressService";
        String clientCode = "HYTXGJLX";//此处替换为您在丰桥平台获取的顾客编码
        String checkword = "x5ZF2i6EZ7JG8vASyjyuTCt7MA6mmcH8";//此处替换为您在丰桥平台获取的校验码
        CallExpressServiceTools client = CallExpressServiceTools.getInstance();
        String myReqXML = reqXml.replace("SLKJ2019", clientCode);
        System.out.println("请求报文:" + myReqXML);
        String respXml = client.callSfExpressServiceByCSIM(reqURL, myReqXML, clientCode, checkword);
        if (respXml != null) {
            System.out.println("--------------------------------------");
            System.out.println("返回报文: " + respXml);
            System.out.println("--------------------------------------");
        }
    }
}
