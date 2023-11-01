package com.msb.crm.controller;

import com.msb.crm.base.BaseController;
import com.msb.crm.model.TreeModel;
import com.msb.crm.service.ModuleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {
    @Resource
    private ModuleService moduleService;
    /* 查询所有的资源列表 */
    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeModel> queryALlModules(Integer roleId){
        return moduleService.queryAllModules(roleId);
    }
    /* 进入授权页面 */
    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId, HttpServletRequest request){
        // 将需要授权的角色ID设置到请求域中
        request.setAttribute("roleId",roleId);
        return "role/grant";
    }
}
