package cn.com.yaohao.visa.common;

import cn.com.yaohao.visa.constant.ResultCode;
import cn.com.yaohao.visa.exception.PassportException;
import cn.com.yaohao.visa.model.ResponseModel;
import cn.com.yaohao.visa.model.UserInfoForToken;
import cn.com.yaohao.visa.service.BackUserService;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

@Component
public class TokenCheckInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${custom.token.ingoreTokenUrls}")
    private String[] ingoreTokenUrls;
    @Value("${custom.token.cache.expireAfterAccessWithMinutes}")
    private long expireAfterAccessWithMinutes;
    @Value("${custom.token.userClaim}")
    private String userClaim;
    @Autowired
    private Algorithm tokenAlgorithm;
    @Resource
    private BackUserService backUserService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String url=request.getRequestURI();//地址
        if(null!=ingoreTokenUrls && ingoreTokenUrls.length>0){
            for (String ingoreUrl : ingoreTokenUrls) {
                if (ingoreUrl.equals(url) || url.startsWith(ingoreUrl)) {// 对免检查token的url放行
                    return true;
                }
            }
        }
        try {
            String accessToken=request.getHeader("accessToken");
            Enumeration<String> headers=request.getHeaderNames();
            String m =request.getMethod();
            if (request.getMethod().equals("OPTIONS")) {
                return true;
            }
            if(!StringUtils.isEmpty(accessToken)){
                final JWTVerifier verifier=JWT.require(tokenAlgorithm).build();
                verifier.verify(accessToken);
                DecodedJWT jwt=JWT.decode(accessToken);
                String userId=jwt.getClaim(userClaim).asString();
                UserInfoForToken userInfo=JSONObject.parseObject(userId,UserInfoForToken.class);
                if (userInfo.getRoleId().equalsIgnoreCase("123456")){
                    return true;
                }
                if (!backUserService.getUrlMethodByRole(userInfo,url)){
                    returnJson(response,request,ResultCode.TOKEN_CHECK_ERR,ResultCode.NO_ACCESS);
                }else {
                    return true;
                }
            }else {
                returnJson(response,request,ResultCode.TOKEN_MISS,ResultCode.TOKEN_MISS_MSG);
            }
        }catch (JWTVerificationException e){
            logger.error("token检验不通过：" + e.getMessage());
            returnJson(response,request,ResultCode.TOKEN_CHECK_ERR,ResultCode.TOKEN_CHECK_ERR_MSG);
            //return false;
        }catch (PassportException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            returnJson(response,request,ResultCode.SYS_ERROR,e.getMessage());
            //return false;
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            returnJson(response,request,ResultCode.SYS_ERROR,ResultCode.SYS_ERROR_MSG);
            //return false;
        }
        return false;
    }

    private void returnJson(HttpServletResponse response, HttpServletRequest request, String code, String msg){
        PrintWriter writer=null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", "*");//设置允许哪些域名应用进行ajax访问
        response.setHeader("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Expose-Headers", "Content-Type,Content-Disposition");
        try {
            writer=response.getWriter();
            writer.print(JSONObject.toJSONString(new ResponseModel(code, msg)));
        }catch (IOException e){
            logger.error("拦截器输出流异常",e.getMessage());
        }finally {
            if(writer != null){
                writer.close();
            }
        }
    }
}
