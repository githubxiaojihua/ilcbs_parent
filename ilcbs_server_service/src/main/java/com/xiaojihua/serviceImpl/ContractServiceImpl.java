package com.xiaojihua.serviceImpl;

import java.util.Collection;
import java.util.List;

import com.xiaojihua.dao.ContractDao;
import com.xiaojihua.domain.Contract;
import com.xiaojihua.service.ContractService;
import com.xiaojihua.utils.UtilFuns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class ContractServiceImpl implements ContractService {

	@Autowired
	private ContractDao contractDao;
	
	@Override
	public List<Contract> find(Specification<Contract> spec) {
		// TODO Auto-generated method stub
		return contractDao.findAll(spec);
	}

	@Override
	public Contract get(String id) {
		// TODO Auto-generated method stub
		return contractDao.findOne(id);
	}

	@Override
	public Page<Contract> findPage(Specification<Contract> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return contractDao.findAll(spec, pageable);
	}

	@Override
	public void saveOrUpdate(Contract entity) {
		// TODO Auto-generated method stub
		if(UtilFuns.isEmpty(entity.getId())){ // 判断修改或者新增
			//设置默认值
			entity.setState(0);  //0:草稿  1：已上报  2:已报运
			entity.setTotalAmount(0.0); //务必设置默认值否则新增货物，分散计算的时候会出先null + xxxx
		}else{
			
		}
		
		contractDao.save(entity);
	}

	@Override
	public void saveOrUpdateAll(Collection<Contract> entitys) {
		// TODO Auto-generated method stub
		contractDao.save(entitys);
	}

	@Override
	public void deleteById(String id) {
		// TODO Auto-generated method stub
		contractDao.delete(id);
	}

	@Override
	public void delete(String[] ids) {
		// TODO Auto-generated method stub
		for (String id : ids) {
			contractDao.delete(id);
		}
	}

}
