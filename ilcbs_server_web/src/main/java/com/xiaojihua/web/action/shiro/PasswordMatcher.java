package com.xiaojihua.web.action.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;

public class PasswordMatcher extends SimpleCredentialsMatcher{

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		// TODO Auto-generated method stub
		System.out.println("调用了密码比较器");
		
		//1.从token获取输入的密码
		UsernamePasswordToken uToken = (UsernamePasswordToken) token;
		
		String password = new String(uToken.getPassword());
		
		//source:要加密的内容   salt：增加复杂度的内容  哈希次数：2
		Md5Hash hash = new Md5Hash(password, uToken.getUsername(), 2);
		
		System.out.println("输入的密码======="+hash.toString());
		
		String dbPass = (String) info.getCredentials();
		//2.从info中获取密码（数据库中的用户密码）
		
		return equals(hash.toString(), dbPass);
	}
}
