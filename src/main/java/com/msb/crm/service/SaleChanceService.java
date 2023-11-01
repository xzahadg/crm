package com.msb.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.msb.crm.base.BaseService;
import com.msb.crm.dao.SaleChanceMapper;
import com.msb.crm.enums.DevResult;
import com.msb.crm.enums.StateStatus;
import com.msb.crm.query.SaleChanceQuery;
import com.msb.crm.utils.AssertUtil;
import com.msb.crm.utils.PhoneUtil;
import com.msb.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {
    // 注入SaleChanceMapper
    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件分页查询营销机会 (返回的数据格式必须满足LayUi中数据表格要求的格式)
     * @param saleChanceQuery
     * @return
     */
    public Map<String ,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery){
        Map<String , Object> map = new HashMap<>();

        // 开启分页
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getLimit());
        // 得到对应分页对象
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(saleChanceQuery));

        // 设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        // 设置分页完成的列表
        map.put("data",pageInfo.getList());

        return map;
    }

    /*
        添加营销机会
            1、参数校验
                customerName客户名称    非空
                linkMan联系人          非空
                linkPhone联系号码       非空，手机号码格式正确
            2、设置相关参数的默认值
                createMan创建人        当前登录用户名
                assignMan指派人        如果未设置指派人(默认)
                     state分配状态(0=未分配，1=已分配)  0未分配
                     assignTime 指派时间         设置为null
                     devResult开发状态 (0=未开发，1=开发中，2=开发成功，3=开发失败)  0未开发（默认）
                                      如果设置了指派人
                     state分配状态(0=未分配，1=已分配)  1未分配
                     assignTime 指派时间         系统当前时间
                     devResult开发状态 (0=未开发，1=开发中，2=开发成功，3=开发失败)  1开发中
                isValid是否有效         设置为有效 1=有效
                createDate创建时间      默认是系统当前时间
                updateDate修改时间      默认是系统当前时间
            3、执行添加操作，判断受影响的行数
     */
    public void addSaleChance(SaleChance saleChance){
        /* 1、校验参数 */
        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        /* 2、设置相关字段的默认值 */
        // isValid是否有效         设置为有效 1=有效
        saleChance.setIsValid(1);
        // createDate创建时间      默认是系统当前时间
        saleChance.setCreateDate(new Date());
        // updateDate修改时间      默认是系统当前时间
        saleChance.setUpdateDate(new Date());
        // 判断是否设置了指派人
        if (StringUtils.isBlank(saleChance.getAssignMan())) {
            // 如果为空，则表示未设置指派人
            // state分配状态(0=未分配，1=已分配)  0未分配
            saleChance.setState(StateStatus.UNSTATE.getType());
            // assignTime 指派时间         设置为null
            saleChance.setAssignTime(null);
            // devResult开发状态 (0=未开发，1=开发中，2=开发成功，3=开发失败)  0未开发（默认）
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        }else{
            // 如果不为空，则表示设置了指派人
            // state分配状态(0=未分配，1=已分配)  1未分配
            saleChance.setState(StateStatus.STATED.getType());
            // assignTime 指派时间         系统当前时间
            saleChance.setAssignTime(new Date());
            // devResult开发状态 (0=未开发，1=开发中，2=开发成功，3=开发失败)  1开发中
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }
        // 3、执行添加操作，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance) != 1,"添加营销机会失败");
    }

    /*
        更新营销机会
            1、参数校验
                营销机会ID                 非空，数据库中对应的记录存在
                customerName客户名称       非空
                linkMan联系人              非空
                linkPhone联系号码          非空，手机号码格式正确
            2、设置相关参数的默认值
                updateDate更新时间          设置为系统当前时间
                assignMan指派人
                    原始数据未设置          修改后未设置    不需要操作
                                         修改后已设置     assignTime指派时间(设置为系统当前时间) 分配状态(1=已分配)  开发状态(1=开发中)
                    原始数据已设置          修改后未设置    assignTime指派时间(设置为null)       分配状态(0=未分配)  开发状态(0=未开发)
                                         修改后已设置     判断修改前后是否是同一个指派人
                                                            如果是，则不需要操作
                                                            如果不是，则需要更新  assignTime指派时间  设置为系统当前时间
            3、执行更新操作，判断受影响的行数
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance){
        // 1、参数校验
        // 营销机会ID   非空，数据库中对应的记录存在
        AssertUtil.isTrue(null == saleChance.getId(),"待更新记录不存在！");
        // 通过主键查询对象
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        // 判断数据库中对应的记录存在
        AssertUtil.isTrue(temp ==null,"待更新的记录不存在！");
        // 参数校验
        checkSaleChanceParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());

        // 设置默认值
        // updateDate更新时间          设置为系统当前时间
        saleChance.setUpdateDate(new Date());
        // assignMan指派人
        // 判断原始数据是否存在
        if (!StringUtils.isBlank(temp.getAssignMan())) {// 不存在
            // 判断修改之后是否存在
            if (!StringUtils.isBlank(saleChance.getAssignMan())) {
                // 修改后已设置     assignTime指派时间(设置为系统当前时间) 分配状态(1=已分配)  开发状态(1=开发中)
                saleChance.setAssignTime(new Date());
                saleChance.setState(StateStatus.STATED.getType());
                saleChance.setDevResult(DevResult.DEVING.getStatus());
            }else {
                // 修改后未设置    不需要操作
            }
        }else{// 存在
            if (!StringUtils.isBlank(saleChance.getAssignMan())) {
                // 修改后已设置     判断修改前后是否是同一个指派人
                if (!temp.getAssignMan().equals(saleChance.getAssignMan())) {
                    // 如果不是，则需要更新  assignTime指派时间  设置为系统当前时间
                    saleChance.setAssignTime(new Date());
                }else {
                    // 如果是，则不需要操作
                    saleChance.setAssignTime(temp.getAssignTime());
                }
            }else {
                // 修改后未设置    assignTime指派时间(设置为null)       分配状态(0=未分配)  开发状态(0=未开发)
                saleChance.setAssignTime(null);
                saleChance.setState(StateStatus.UNSTATE.getType());
                saleChance.setDevResult(DevResult.UNDEV.getStatus());
            }
        }
        // 执行更新操作，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1,"营销机会更新失败");
    }
    private void checkSaleChanceParams(String customerName, String linkMan, String linkPhone) {
        // 判断customerName参数非空
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名称不能为空");
        // 判断linkMan参数
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空");
        // 判断linkPhone参数非空
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"联系电话不能为空");
        // 判断联系电话格式正确
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"电话号码格式错误");
    }

    /* 批量删除 */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChance(Integer[] ids){
        // 判断ID是否为空
        AssertUtil.isTrue(ids == null || ids.length < 1,"待删除记录不存在！！");
        // 执行删除(更新)操作，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids) != ids.length,"营销机会数据删除失败！！");
    }

    /* 营销机会更新开发状态*/
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChanceDevResult(Integer id, Integer devResult) {
        // 判断Id是否为空
        AssertUtil.isTrue(id == null,"待更新记录不存在！！");
        // 判断对象是否为空
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(saleChance == null,"待更新记录不存在！");
        // 设置开发状态
        saleChance.setDevResult(devResult);
        // 执行更新操作，判断受影响的函数
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1,"营销机会更新失败");
    }
}
