package com.msb.crm.controller;

import com.msb.crm.base.BaseController;
import com.msb.crm.base.ResultInfo;
import com.msb.crm.query.RoleQuery;
import com.msb.crm.service.RoleService;
import com.msb.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {
    @Resource
    private RoleService roleService;

    /* 查询所有的角色列表 */
    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleService.queryAllRoles(userId);
    }

    /* 分页条件查询角色列表*/
    @GetMapping("list")
    @ResponseBody
    public Map<String,Object> selectByParams(RoleQuery roleQuery){
        return roleService.queryByParamsForTable(roleQuery);
    }
    /* 进入角色列表页面 */
    @RequestMapping("index")
    public String index(){
        return "role/role";
    }
    /* 添加角色 */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addRole(Role role){
        roleService.addRole(role);
        return success("角色添加成功!!");
    }
    /* 更新角色 */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        roleService.updateRole(role);
        return success("角色更新成功!!");
    }
    /* 删除角色 */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer id){
        roleService.deleteRole(id);
        return success("角色删除成功!!");
    }
    /* 进入添加、更新的页面 */
    @RequestMapping("toAddOrUpdateRolePage")
    public String toAddOrUpdateRolePage(Integer id, HttpServletRequest request){
        // 如果roleId不为空，则表示修改操作，通过角色ID查询角色记录，存到请求域中
        if (id != null) {
            // 通过角色ID查询角色记录
            Role role = roleService.selectByPrimaryKey(id);
            // 设置到请求域中
            request.setAttribute("role",role);
        }
        return "role/add_update";
    }
    /* 角色授权 */
    @PostMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer roleId,Integer[] mids){
        roleService.addGrant(roleId,mids);
        return success("角色授权成功！");
    }
}
