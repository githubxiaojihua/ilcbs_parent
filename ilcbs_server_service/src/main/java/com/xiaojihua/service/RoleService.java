package com.xiaojihua.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.xiaojihua.domain.Role;

public interface RoleService {
	//查询所有，带条件查询
	public List<Role> find(Specification<Role> spec);
	//获取一条记录
	public Role get(String id);
	//分页查询，将数据封装到一个page分页工具类对象
	public  Page<Role> findPage(Specification<Role> spec, Pageable pageable);
	
	//新增和修改保存
	public  void saveOrUpdate(Role entity);
	//批量新增和修改保存
	public  void saveOrUpdateAll(Collection<Role> entitys);
	
	//单条删除，按id
	public  void deleteById(String id);
	//批量删除
	public  void delete(String[] ids);
}
