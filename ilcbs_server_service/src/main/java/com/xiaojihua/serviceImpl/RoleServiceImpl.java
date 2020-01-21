package com.xiaojihua.serviceImpl;

import java.util.Collection;
import java.util.List;

import com.xiaojihua.dao.RoleDao;
import com.xiaojihua.domain.Role;
import com.xiaojihua.service.RoleService;
import com.xiaojihua.utils.UtilFuns;
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
