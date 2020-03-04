package com.xiaojihua.service;

import java.util.Collection;
import java.util.List;

import com.xiaojihua.domain.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ContractService {
	//查询所有，带条件查询
	public List<Contract> find(Specification<Contract> spec);
	//获取一条记录
	public Contract get(String id);
	//分页查询，将数据封装到一个page分页工具类对象
	public  Page<Contract> findPage(Specification<Contract> spec, Pageable pageable);
	
	//新增和修改保存
	public  void saveOrUpdate(Contract entity);
	//批量新增和修改保存
	public  void saveOrUpdateAll(Collection<Contract> entitys);
	
	//单条删除，按id
	public  void deleteById(String id);
	//批量删除
	public  void delete(String[] ids);
}
