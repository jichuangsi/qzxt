package cn.com.yaohao.visa.constant;

public class ResultCode {
	public static final String SUCESS = "0010";//成功
	public static final String PARAM_MISS = "0020";//缺少参数
	public static final String PARAM_ERR = "0021";//参数不正确
	public static final String TOKEN_MISS = "0030";//缺少token
	public static final String TOKEN_CHECK_ERR = "0031";//token校验异常
	public static final String SYS_ERROR = "0050";//系统内部异常
	public static final String SYS_BUSY = "0051";//熔断
	
	public static final String SUCESS_MSG = "成功";
	public static final String PARAM_MISS_MSG = "缺少参数";
	public static final String PARAM_ERR_MSG = "参数不正确";
	public static final String TOKEN_MISS_MSG = "缺少token";
	public static final String TOKEN_CHECK_ERR_MSG = "token校验异常";
	public static final String SYS_ERROR_MSG = "系统繁忙";
	public static final String SYS_BUSY_MSG = "系统繁忙";
	public static final String NO_ACCESS = "无权访问";

	public final static String ACCOUNT_ISEXIST_MSG = "账号已存在";
	public final static String ACCOUNT_NOTEXIST_MSG = "账号不存在，或者账户密码错误";
	public final static String PWD_NOT_MSG = "两次密码不一致";
	public final static String USER_NOROLE_MSG = "该账户没有分配角色，请先让管理员分配角色";
	public final static String EXPREES_NOEXIST_MSG="该快件不存在";
	public final static String PASSPORT_NOEXIST_MSG="该护照不存在";
	public static final String EXCEL_IMPORT_MSG = "excel导入失败";
}
