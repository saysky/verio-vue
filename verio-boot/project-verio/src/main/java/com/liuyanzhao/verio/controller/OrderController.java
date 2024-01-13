package com.liuyanzhao.verio.controller;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.liuyanzhao.common.exception.ServiceException;
import com.liuyanzhao.verio.domain.Bill;
import com.liuyanzhao.verio.domain.House;
import com.liuyanzhao.verio.service.BillService;
import com.liuyanzhao.verio.service.HouseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.liuyanzhao.common.annotation.Log;
import com.liuyanzhao.common.core.controller.BaseController;
import com.liuyanzhao.common.core.domain.AjaxResult;
import com.liuyanzhao.common.enums.BusinessType;
import com.liuyanzhao.verio.domain.Order;
import com.liuyanzhao.verio.service.OrderService;
import com.liuyanzhao.common.core.page.TableDataInfo;

/**
 * 订单Controller
 *
 * @author saysky
 * @date 2024-01-03
 */
@RestController
@RequestMapping("/verio/order")
public class OrderController extends BaseController {
    @Autowired
    private OrderService orderService;

    /**
     * 查询订单列表
     */
    @PreAuthorize("@ss.hasPermi('verio:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(Order order) {
        if (!isAdmin()) {
            order.setCustomerUserId(getUserId());
        }
        startPage();
        List<Order> list = orderService.selectOrderList(order);
        return getDataTable(list);
    }

    /**
     * 查询所有有效订单
     */
    @PreAuthorize("@ss.hasPermi('verio:order:list')")
    @GetMapping("/activeList")
    public AjaxResult activeList() {
        Long userId = !isAdmin() ? getUserId() : null;
        List<Order> activeList = orderService.getActiveList(userId);
        return success(activeList);
    }


    /**
     * 获取订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('verio:order:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        checkDataAuth(id);
        return success(orderService.selectOrderById(id));
    }

    /**
     * 新增订单
     */
    @PreAuthorize("@ss.hasPermi('verio:order:add')")
    @Log(title = "订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Order order) {
        order.setCustomerUserId(getUserId());
        orderService.insertOrder(order);
        return success(order.getId());
    }


    /**
     * 取消订单
     */
    @PreAuthorize("@ss.hasPermi('verio:order:cancel')")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @PostMapping("/cancel/{id}")
    public AjaxResult cancel(@PathVariable Long id) {

        checkDataAuth(id);
        Order order = orderService.getById(id);
        if (order != null) {
            order.setStatus(-3); // -3表示已取消
            orderService.updateById(order);
        }
        return success();
    }

    /**
     * 签合同
     */
    @PreAuthorize("@ss.hasPermi('verio:order:signContract')")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @PostMapping("/signContract/{id}")
    public AjaxResult signContract(@PathVariable Long id) {

        checkDataAuth(id);
        Order order = orderService.getById(id);
        if (order != null) {
            order.setStatus(-1); // -1表示待付款
            orderService.updateById(order);
        }
        return success();
    }

    /**
     * 支付
     */
    @PreAuthorize("@ss.hasPermi('verio:order:pay')")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @PostMapping("/pay/{id}")
    public AjaxResult pay(@PathVariable Long id) {
        checkDataAuth(id);
        orderService.payOrder(id);
        return success();
    }

    /**
     * 删除订单
     */
    @PreAuthorize("@ss.hasPermi('verio:order:remove')")
    @Log(title = "订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        for (Long id : ids) {
            checkDataAuth(id);
        }
        return toAjax(orderService.deleteOrderByIds(ids));
    }

    /**
     * 校验权限
     */
    private void checkDataAuth(Long id) {
        if (isAdmin()) {
            return;
        }
        Long loginUserId = getUserId();
        Order order = orderService.getById(id);
        if (order != null
                && !Objects.equals(loginUserId, order.getOwnerUser())
                && !Objects.equals(loginUserId, order.getCustomerUserId())) {
            throw new ServiceException("没有权限");
        }
    }
}
