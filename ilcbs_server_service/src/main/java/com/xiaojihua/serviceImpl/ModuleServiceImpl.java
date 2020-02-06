package com.xiaojihua.serviceImpl;

import com.xiaojihua.dao.ModuleDao;
import com.xiaojihua.dao.UserDao;
import com.xiaojihua.domain.Module;
import com.xiaojihua.domain.User;
import com.xiaojihua.service.ModuleService;
import com.xiaojihua.service.UserService;
import com.xiaojihua.utils.UtilFuns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class ModuleServiceImpl implements ModuleService {
    @Autowired
    private ModuleDao dao;

    @Override
    public List<Module> find(Specification<Module> spec) {
        return dao.findAll(spec);
    }

    @Override
    public Module get(String id) {
        //find相当于get没有延时加载的问题
        return dao.findOne(id);
    }

    @Override
    public Page<Module> findPage(Specification<Module> spec, Pageable pageable) {
        //从这里也看出应该在dao中继承JpaSpecificationExecutor
        return dao.findAll(spec,pageable);
    }

    /**
     * 更新和插入都走dao.save方法。
     * 根据entity的id来判断是否是新增，如果id是新的那么就是新增，
     * 如果id已经在数据库中了那么就是修改。
     * @param entity
     */
    @Override
    public void saveOrUpdate(Module entity) {
        if(UtilFuns.isEmpty(entity.getId())){

        }else{

        }
        dao.save(entity);
    }

    @Override
    public void saveOrUpdateAll(Collection<Module> entitys) {
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
