package com.xiaojihua.web.action;

import com.xiaojihua.domain.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xiaojihua.utils.SysConstant;
import com.xiaojihua.utils.UtilFuns;

/**
 * @Description: 登录和退出类
 * @Author:		传智播客 java学院	传智.宋江
 * @Company:	http://java.itcast.cn
 * @CreateDate:	2014年10月31日
 * 
 * 继承BaseAction的作用
 * 1.可以与struts2的API解藕合
 * 2.还可以在BaseAction中提供公有的通用方法
 */
@Namespace("/")
@Results({
	@Result(name="login",location="/WEB-INF/pages/sysadmin/login/login.jsp"),
	@Result(name="success",location="/WEB-INF/pages/home/fmain.jsp"),
	@Result(name="logout",location="/index.jsp")})
public class LoginAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;



	//SSH传统登录方式
	@Action("loginAction_login")
	public String login() throws Exception {
		
//		if(true){
//			String msg = "登录错误，请重新填写用户名密码!";
//			this.addActionError(msg);
//			throw new Exception(msg);
//		}
//		User user = new User(username, password);
//		User login = userService.login(user);
//		if (login != null) {
//			ActionContext.getContext().getValueStack().push(user);
//			session.put(SysConstant.CURRENT_USER_INFO, login);	//记录session
//			return SUCCESS;
//		}
//		return "login";


		if(UtilFuns.isEmpty(username)){
			return "login";
		}

		Subject subject = SecurityUtils.getSubject();

		// 组装token的时候，密码进行md5处理
		// 这样就不用了设置密码比较器了，实际上是走的shior内部的默认密码比较器，只是进行简单的equales比较
		// 另外还需要将配置文件中的密码比较器配置去掉
		Md5Hash hash = new Md5Hash(password, username, 2);

		UsernamePasswordToken token = new UsernamePasswordToken(username, hash.toString());

		try {
			subject.login(token);
			User user = (User) subject.getPrincipal();
			session.put(SysConstant.CURRENT_USER_INFO, user);
			return SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			super.put("errorInfo", "您的用户名或密码错误"); //登录页面的错误信息提示
			return "login";
		}
	}
	
	
	//退出
	@Action("loginAction_logout")
	public String logout(){
		session.remove(SysConstant.CURRENT_USER_INFO);		//删除session
		SecurityUtils.getSubject().logout();   //登出
		return "logout";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

