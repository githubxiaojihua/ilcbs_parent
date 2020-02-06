package com.xiaojihua.web.action.shiro;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.xiaojihua.domain.Module;
import com.xiaojihua.domain.Role;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import com.xiaojihua.domain.User;
import com.xiaojihua.service.UserService;

public class AuthRealm extends AuthorizingRealm{

	@Autowired
	private UserService userService;

    /**
     * 完成授权数据查询
     * @param pc
     * @return
     */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		// TODO Auto-generated method stub
		System.out.println("调用了授权方法");
		SimpleAuthorizationInfo authorInfo = new SimpleAuthorizationInfo();
        User user = (User)pc.getPrimaryPrincipal();
        Set<Role> roles = user.getRoles();
        List<String> permission = new ArrayList<>();

        for(Role role : roles){
            Set<Module> modules = role.getModules();
            for(Module module : modules){
                permission.add(module.getCpermission());
            }
        }
        authorInfo.addStringPermissions(permission);
        return authorInfo;
	}

    /**
     * 通过自定义密码比较器来比较密码，完成登录验证
     * @param arg0
     * @return
     * @throws AuthenticationException
     */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
		// TODO Auto-generated method stub、
		System.out.println("调用了认证方法");
		UsernamePasswordToken token = (UsernamePasswordToken) arg0;
		final String username = token.getUsername();
		
		Specification<User> spec = new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.equal(root.get("userName").as(String.class), username);
			}
		};
		List<User> userList = userService.find(spec);
		
		if(userList!=null && userList.size() > 0){
			User user = userList.get(0);
			System.out.println(user.getPassword());
			//principal:主要对象（登录的用户）   credentials:密码      realm的名字可以通过getName获取类名作为区分
			return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
		}
		return null;
	}

}
