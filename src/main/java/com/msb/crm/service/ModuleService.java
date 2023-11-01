package com.msb.crm.service;

import com.msb.crm.base.BaseService;
import com.msb.crm.dao.ModuleMapper;
import com.msb.crm.dao.PermissionMapper;
import com.msb.crm.model.TreeModel;
import com.msb.crm.vo.Module;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ModuleService extends BaseService<Module,Integer> {
    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private PermissionMapper permissionMapper;

    /* 查询所有的资源列表 */
    public List<TreeModel> queryAllModules(Integer roleId){
        // 查询所有的资源列表
        List<TreeModel> treeModelList = moduleMapper.queryAllModules();
        // 查询指定角色已经授权过的资源列表 (查询角色拥有的资源ID)
        List<Integer> permissionIds = permissionMapper.queryRoleHasModuleIdsByRoleId(roleId);
        // 判断角色是否拥有资源ID
        if (permissionIds != null && permissionIds.size() > 0) {
            // 循环所有的资源列表，判断用户拥有的资源ID中是否有匹配的，如果有，则设置checked属性为true
            treeModelList.forEach(treeModel -> {
                // 判断角色拥有的资源ID中是否有当前遍历的资源ID
                if (permissionIds.contains(treeModel.getId())) {
                    // 如果包含id，则说明角色拥有授权，设置checked为true
                    treeModel.setChecked(true);
                }
            });
        }
        return treeModelList;
    }
}
