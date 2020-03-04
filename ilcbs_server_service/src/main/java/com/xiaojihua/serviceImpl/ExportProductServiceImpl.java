package com.xiaojihua.serviceImpl;

import java.util.Collection;
import java.util.List;

import com.xiaojihua.dao.ContractDao;
import com.xiaojihua.dao.ContractProductDao;
import com.xiaojihua.dao.ExportProductDao;
import com.xiaojihua.domain.ExportProduct;
import com.xiaojihua.service.ExportProductService;
import com.xiaojihua.utils.UtilFuns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;



@Service
public class ExportProductServiceImpl implements ExportProductService {

	@Autowired
	private ExportProductDao exportProductDao;
	
	@Autowired
	private ContractDao contractDao;
	
	@Autowired
	private ContractProductDao contractProductDao;
	
	@Override
	public List<ExportProduct> find(Specification<ExportProduct> spec) {
		// TODO Auto-generated method stub
		return exportProductDao.findAll(spec);
	}

	@Override
	public ExportProduct get(String id) {
		// TODO Auto-generated method stub
		return exportProductDao.findOne(id);
	}

	@Override
	public Page<ExportProduct> findPage(Specification<ExportProduct> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return exportProductDao.findAll(spec, pageable);
	}

	@Override
	public void saveOrUpdate(ExportProduct entity) {
		// TODO Auto-generated method stub
		if(UtilFuns.isEmpty(entity.getId())){ // 判断修改或者新增
			
		}
		exportProductDao.save(entity);
	}

	@Override
	public void saveOrUpdateAll(Collection<ExportProduct> entitys) {
		// TODO Auto-generated method stub
		exportProductDao.save(entitys);
	}

	@Override
	public void deleteById(String id) {
		// TODO Auto-generated method stub
		exportProductDao.delete(id);
	}

	@Override
	public void delete(String[] ids) {
		// TODO Auto-generated method stub
		for (String id : ids) {
			exportProductDao.delete(id);
		}
	}
}
