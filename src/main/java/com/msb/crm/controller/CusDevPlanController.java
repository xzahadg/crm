package com.msb.crm.controller;

import com.msb.crm.base.BaseController;
import com.msb.crm.base.ResultInfo;
import com.msb.crm.query.CusDevPlanQuery;
import com.msb.crm.service.CusDevPlanService;
import com.msb.crm.service.SaleChanceService;
import com.msb.crm.vo.CusDevPlan;
import com.msb.crm.vo.SaleChance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("cus_dev_plan")
@Controller
public class CusDevPlanController extends BaseController {
    @Resource
    SaleChanceService saleChanceService;
    @Resource
    CusDevPlanService cusDevPlanService;

    /* 进入客户开发计划页面 */
    @RequestMapping("index")
    public String index(){
        return "cusDevPlan/cus_dev_plan";
    }

    /* 打开计划项开发与详情页面 */
    @RequestMapping("toCusDevPlanDataPage")
    public String toCusDevPlanPage(Integer id, HttpServletRequest request){
        // 通过Id查询营销机会对象
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
        // 将对象设置到请求域中
        request.setAttribute("saleChance",saleChance);
        return "cusDevPlan/cus_dev_plan_data";
    }
    /* 营销机会数据查询(分页多条件查询) */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String , Object> querySaleChanceByParams (CusDevPlanQuery cusDevPlanQuery){
        return cusDevPlanService.queryCusDevPlanByParams(cusDevPlanQuery);
    }
    /* 添加计划项 */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success("计划项添加成功！！");
    }
    /* 更新计划项 */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        return success("计划项更新成功！！");
    }
    /* 进入添加或修改计划项的页面 */
    @RequestMapping("toAddOrUpdateCusDevPlanPage")
    public String toAddOrUpdateDevPlanPage(HttpServletRequest request,Integer sId,Integer id){
        // 将营销机会Id设置到请求域中，给计划项页面获取
        request.setAttribute("sid",sId);
        // 通过计划项Id查询记录
        CusDevPlan cusDevPlan = cusDevPlanService.selectByPrimaryKey(id);
        // 将计划项数据设置到请求域中
        request.setAttribute("cusDevPlan",cusDevPlan);
        return "cusDevPlan/add_update";
    }
    /* 删除计划项 */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlan(Integer id){
        cusDevPlanService.deleteCusDevPlan(id);
        return success("计划项更新成功");
    }
}
