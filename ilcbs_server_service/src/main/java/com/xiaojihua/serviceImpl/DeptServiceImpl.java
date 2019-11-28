package com.xiaojihua.serviceImpl;

import com.xiaojihua.dao.DeptDao;
import com.xiaojihua.domain.Dept;
import com.xiaojihua.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptDao dao;

    @Override
    public List<Dept> find(Specification<Dept> spec) {
        return dao.findAll(spec);
    }

    @Override
    public Dept get(String id) {
        //find相当于get没有延时加载的问题
        return dao.findOne(id);
    }

    @Override
    public Page<Dept> findPage(Specification<Dept> spec, Pageable pageable) {
        //从这里也看出应该在dao中继承JpaSpecificationExecutor
        return dao.findAll(spec,pageable);
    }

    @Override
    public void saveOrUpdate(Dept entity) {
        dao.save(entity);
    }

    @Override
    public void saveOrUpdateAll(Collection<Dept> entitys) {
        dao.save(entitys);
    }

    @Override
    public void deleteById(String id) {
        dao.delete(id);
    }

    @Override
    public void delete(String[] ids) {
        for(String id : ids){
            dao.delete(id);
        }
    }
}
