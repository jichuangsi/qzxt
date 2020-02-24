/**
 * 作为jwt token的用户信息封装（因为在token中可能不包含用户所有的信息）
 */
package cn.com.yaohao.visa.model;

/**
 * @author huangjiajun
 *
 */
public class UserInfoForToken {
	private String userId;
	private String userName;
	private String roleId;
	private long loginTime;

	public long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}
