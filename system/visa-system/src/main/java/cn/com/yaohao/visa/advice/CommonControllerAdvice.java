/**
 * 拦截token将用户信息放入model，及总异常拦截
 */
package cn.com.yaohao.visa.advice;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import cn.com.yaohao.visa.model.ResponseModel;
import cn.com.yaohao.visa.model.UserInfoForToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author huangjiajun
 *
 */
@RestControllerAdvice
public class CommonControllerAdvice {

	@Value("${custom.token.userClaim}")
	private String userClaim;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@ModelAttribute
	public UserInfoForToken translateHeader(@RequestHeader @Nullable String accessToken, Model model)
			throws UnsupportedEncodingException {
		if (!StringUtils.isEmpty(accessToken)) {
			DecodedJWT jwt = JWT.decode(accessToken);
			String user = jwt.getClaim(userClaim).asString();
			UserInfoForToken userInfo = JSONObject.parseObject(user, UserInfoForToken.class);
			return userInfo;
		}
		return null;
	}

	@ExceptionHandler
	public ResponseModel<Object> handler(Exception e) {
		logger.error(e.getCause() + ":" + e.getMessage());
		return ResponseModel.fail("", e.getMessage());

	}

	/**
	 * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
	 *
	 * @param binder
	 */
	/*
	 * @InitBinder public void initBinder(WebDataBinder binder) { //保存用户信息
	 * HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
	 * .getRequestAttributes()).getRequest(); HttpSession session =
	 * request.getSession(); long endTime = (System.currentTimeMillis()/1000);
	 * String userId=(String)session.getAttribute("userId"); if
	 * (session.getAttribute("userId") != null) { //login(session); if
	 * (session.getAttribute("time") != null) { long saertTime = (Long)
	 * session.getAttribute("time"); if (endTime-saertTime>=1800) {//半小时
	 * teacherInfoService.getUserLogin(userId);
	 * 
	 * session.removeAttribute("userId"); session.removeAttribute("time"); } } } }
	 */

}
