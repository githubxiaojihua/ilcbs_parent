package com.xiaojihua.serviceImpl;

import com.xiaojihua.dao.ContractDao;
import com.xiaojihua.dao.ContractProductDao;
import com.xiaojihua.domain.Contract;
import com.xiaojihua.domain.ContractProducts;
import com.xiaojihua.service.ContractProductService;
import com.xiaojihua.utils.UtilFuns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;


@Service
public class ContractProductServiceImpl implements ContractProductService {

	@Autowired
	private ContractProductDao contractProductDao;

	@Autowired
	private ContractDao contractDao;
	
	@Override
	public List<ContractProducts> find(Specification<ContractProducts> spec) {
		// TODO Auto-generated method stub
		return contractProductDao.findAll(spec);
	}

	@Override
	public ContractProducts get(String id) {
		// TODO Auto-generated method stub
		return contractProductDao.findOne(id);
	}

	@Override
	public Page<ContractProducts> findPage(Specification<ContractProducts> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return contractProductDao.findAll(spec, pageable);
	}

	@Override
	public void saveOrUpdate(ContractProducts entity) {
		// TODO Auto-generated method stub
		if(UtilFuns.isEmpty(entity.getId())){ // 判断修改或者新增
			//分散计算
			Double amount = 0.0;
			if(UtilFuns.isNotEmpty(entity.getCnumber()) && UtilFuns.isNotEmpty(entity.getPrice())){
				amount = entity.getCnumber() * entity.getPrice();
			}
			// 货物的总金额
			entity.setAmount(amount);

			// 获取购销合同对象
			Contract contract = contractDao.findOne(entity.getContract().getId());
			contract.setTotalAmount(contract.getTotalAmount() + amount); //分散计算的新增
			contractDao.save(contract);
		}else{
			Double oldAmount = entity.getAmount();  // 保存旧的总金额

			Double amount = 0.0;
			if(UtilFuns.isNotEmpty(entity.getCnumber()) && UtilFuns.isNotEmpty(entity.getPrice())){
				amount = entity.getCnumber() * entity.getPrice();
			}
			// 货物的总金额
			if(oldAmount != amount){
				entity.setAmount(amount);  //修改后的总金额设置到商品上
				Contract contract = contractDao.findOne(entity.getContract().getId());
				contract.setTotalAmount(contract.getTotalAmount() + amount - oldAmount); //修改的差值
				contractDao.save(contract);
			}
		}

		contractProductDao.save(entity);
	}

	@Override
	public void saveOrUpdateAll(Collection<ContractProducts> entitys) {
		// TODO Auto-generated method stub
		contractProductDao.save(entitys);
	}

	@Override
	public void deleteById(String id) {
		// TODO Auto-generated method stub
		contractProductDao.delete(id);
	}

	@Override
	public void delete(String[] ids) {
		// TODO Auto-generated method stub
		for (String id : ids) {
			contractProductDao.delete(id);
		}
	}

	@Override
	public List<ContractProducts> findCpByShipTime(String shipTime) {
		// TODO Auto-generated method stub
		return contractProductDao.findCpByShipTime(shipTime);
	}

}
