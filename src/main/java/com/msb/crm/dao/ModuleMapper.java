package com.msb.crm.dao;

import com.msb.crm.base.BaseMapper;
import com.msb.crm.model.TreeModel;
import com.msb.crm.vo.Module;

import java.util.List;


public interface ModuleMapper extends BaseMapper<Module,Integer> {
    // 查询所有的资源列表
    public List<TreeModel> queryAllModules();
}