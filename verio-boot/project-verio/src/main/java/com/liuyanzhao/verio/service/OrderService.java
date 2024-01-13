package com.liuyanzhao.verio.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liuyanzhao.verio.domain.Order;

/**
 * 订单Service接口
 *
 * @author saysky
 * @date 2024-01-03
 */
public interface OrderService extends IService<Order> {
    /**
     * 查询订单
     *
     * @param id 订单主键
     * @return 订单
     */
    Order selectOrderById(Long id);

    /**
     * 查询订单列表
     *
     * @param order 订单
     * @return 订单集合
     */
    List<Order> selectOrderList(Order order);

    /**
     * 新增订单
     *
     * @param order 订单
     * @return 结果
     */
    int insertOrder(Order order);

    /**
     * 修改订单
     *
     * @param order 订单
     * @return 结果
     */
    int updateOrder(Order order);

    /**
     * 批量删除订单
     *
     * @param ids 需要删除的订单主键集合
     * @return 结果
     */
    int deleteOrderByIds(Long[] ids);

    /**
     * 删除订单信息
     *
     * @param id 订单主键
     * @return 结果
     */
    int deleteOrderById(Long id);

    /**
     * 查询已到期订单
     *
     * @return
     */
    List<Order> selectOverDueOrder();

    int countByHouseId(Long houseId);

    /**
     * 查询所有有效订单
     *
     * @return
     */
    List<Order> getActiveList(Long userId);

    void payOrder(Long id);
}
