package com.msb.crm.dao;

import com.msb.crm.base.BaseMapper;
import com.msb.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {
    // 查询所有的角色列表(只需要ID与roleName)
    public List<Map<String,Object>> queryAllRoles(Integer userId);

    Role selectByRoleName(String roleName);
}