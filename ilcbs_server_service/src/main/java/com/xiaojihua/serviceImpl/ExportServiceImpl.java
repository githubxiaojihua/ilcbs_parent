package com.xiaojihua.serviceImpl;

import com.xiaojihua.dao.ContractDao;
import com.xiaojihua.dao.ContractProductDao;
import com.xiaojihua.dao.ExportDao;
import com.xiaojihua.domain.*;
import com.xiaojihua.service.ExportService;
import com.xiaojihua.utils.UtilFuns;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ExportServiceImpl implements ExportService {

	@Autowired
	private ExportDao exportDao;

	@Autowired
	private ContractDao contractDao;

	@Autowired
	private ContractProductDao contractProductDao;
	
	@Override
	public List<Export> find(Specification<Export> spec) {
		// TODO Auto-generated method stub
		return exportDao.findAll(spec);
	}

	@Override
	public Export get(String id) {
		// TODO Auto-generated method stub
		return exportDao.findOne(id);
	}

	@Override
	public Page<Export> findPage(Specification<Export> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return exportDao.findAll(spec, pageable);
	}

	@Override
	public void saveOrUpdate(Export entity) {
		// TODO Auto-generated method stub
		if(UtilFuns.isEmpty(entity.getId())){ // 判断修改或者新增
			// 设置默认值 (状态，时间)
			entity.setState(0);  // 0草稿  1已上报
			entity.setInputDate(new Date());

			String[] contractIds = entity.getContractIds().split(", ");

			StringBuffer sb = new StringBuffer();
			// 需要将contract中的contractNo拼接成字符串放到报运对象的CustomerContract
			for (String cid : contractIds) { //根据id循环所有购销合同
				Contract contract = contractDao.findOne(cid);
				sb.append(contract.getContractNo()).append(" ");
				// 购销合同使用后，状态应修改为2  0：草稿   1：已上报  2：已报运
				contract.setState(2); //为了防止购销合同多次生成出口报运单，将状态设置为2
			}

			entity.setCustomerContract(sb.toString());


			// 数据搬家  将购销合同下的所有货物转成报运单货物
			// 通过跳跃查询的方式一次性取出所有购销合同下货物的list
			// 注意这里使用的是数组作为参数
			List<ContractProducts> cpList = contractProductDao.findCpByContractIds(contractIds);

			HashSet<ExportProduct> exportProducts = new HashSet<ExportProduct>();
			for (ContractProducts contractProduct : cpList) {
				ExportProduct exportProduct = new ExportProduct(); //数据搬家
				exportProduct.setBoxNum(contractProduct.getBoxNum());
				BeanUtils.copyProperties(contractProduct, exportProduct); //数据对拷，用spring的工具类

				exportProduct.setId(null); //防止属性对拷后id也对拷

				//@OneToMany(mappedBy="export",cascade=CascadeType.ALL) 多方维护一方的关系，需要设置一方对象
				exportProduct.setExport(entity);
				exportProducts.add(exportProduct);

				// 将所有货物下的附件转成报运商品的附件
				Set<ExtCproduct> extCproducts = contractProduct.getExtCproducts();
				HashSet<ExtEproduct> extEproducts = new HashSet<ExtEproduct>();
				for (ExtCproduct extCproduct : extCproducts) { //购销附件对拷到报运附件里
					ExtEproduct extEproduct = new ExtEproduct();
					BeanUtils.copyProperties(extCproduct, extEproduct);

					extEproduct.setId(null); //防止属性对拷后id也对拷

					//@OneToMany(mappedBy="exportProduct",cascade=CascadeType.ALL) 多方维护一方的关系，需要设置一方对象
					extEproduct.setExportProduct(exportProduct);
					extEproducts.add(extEproduct);
				}
				exportProduct.setExtEproducts(extEproducts);
			}

			entity.setExportProducts(exportProducts);
		}
		
		exportDao.save(entity);
	}

	@Override
	public void saveOrUpdateAll(Collection<Export> entitys) {
		// TODO Auto-generated method stub
		exportDao.save(entitys);
	}

	@Override
	public void deleteById(String id) {
		// TODO Auto-generated method stub
		exportDao.delete(id);
	}

	@Override
	public void delete(String[] ids) {
		// TODO Auto-generated method stub
		for (String id : ids) {
			exportDao.delete(id);
		}
	}

}
