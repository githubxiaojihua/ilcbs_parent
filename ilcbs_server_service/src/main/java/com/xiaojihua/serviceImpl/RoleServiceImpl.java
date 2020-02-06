package com.xiaojihua.serviceImpl;

import java.util.Collection;
import java.util.List;

import com.xiaojihua.dao.RoleDao;
import com.xiaojihua.domain.Role;
import com.xiaojihua.service.RoleService;
import com.xiaojihua.utils.UtilFuns;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;
	
	@Override
	public List<Role> find(Specification<Role> spec) {
		// TODO Auto-generated method stub
		return roleDao.findAll(spec);
	}

	@Override
	public Role get(String id) {
		// TODO Auto-generated method stub
		return roleDao.findOne(id);
	}

	/**
	 * 使用注解的方式来代替过滤器连中的perms过滤器链，
	 * 但是注意一点，需要加载Sercice上，如果加在Action上
	 * 那么会与STRUTS冲突导致，如果一个用户有权限的话
	 * 会报错误：service无法注入，导致空指针异常
	 * 因此直接加到service中的相关方法上也是一样
	 * 另外，注解抛出的异常页面，过滤器链配置的话
	 * 会直接跳转到未授权页面
	 * @param spec
	 * @param pageable
	 * @return
	 */
	@RequiresPermissions("角色管理")
	@Override
	public Page<Role> findPage(Specification<Role> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return roleDao.findAll(spec, pageable);
	}

	@Override
	public void saveOrUpdate(Role entity) {
		// TODO Auto-generated method stub
		if(UtilFuns.isEmpty(entity.getId())){ // 判断修改或者新增
			
		}else{
			
		}
		
		roleDao.save(entity);
	}

	@Override
	public void saveOrUpdateAll(Collection<Role> entitys) {
		// TODO Auto-generated method stub
		roleDao.save(entitys);
	}

	@Override
	public void deleteById(String id) {
		// TODO Auto-generated method stub
		roleDao.delete(id);
	}

	@Override
	public void delete(String[] ids) {
		// TODO Auto-generated method stub
		for (String id : ids) {
			roleDao.delete(id);
		}
	}

}
