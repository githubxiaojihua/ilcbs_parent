package com.xiaojihua.service;

import com.xiaojihua.domain.Factory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.List;

public interface FactoryService {
	//查询所有，带条件查询
	public List<Factory> find(Specification<Factory> spec);
	//获取一条记录
	public Factory get(String id);
	//分页查询，将数据封装到一个page分页工具类对象
	public  Page<Factory> findPage(Specification<Factory> spec, Pageable pageable);
	
	//新增和修改保存
	public  void saveOrUpdate(Factory entity);
	//批量新增和修改保存
	public  void saveOrUpdateAll(Collection<Factory> entitys);
	
	//单条删除，按id
	public  void deleteById(String id);
	//批量删除
	public  void delete(String[] ids);
}
