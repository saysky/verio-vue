package com.liuyanzhao.verio.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liuyanzhao.common.core.domain.entity.SysUser;
import com.liuyanzhao.common.exception.ServiceException;
import com.liuyanzhao.common.utils.DateUtils;
import com.liuyanzhao.verio.domain.Bill;
import com.liuyanzhao.verio.domain.Feedback;
import com.liuyanzhao.verio.domain.House;
import com.liuyanzhao.verio.service.BillService;
import com.liuyanzhao.verio.service.HouseService;
import com.liuyanzhao.verio.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.liuyanzhao.verio.mapper.OrderMapper;
import com.liuyanzhao.verio.domain.Order;
import com.liuyanzhao.verio.service.OrderService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单Service业务层处理
 *
 * @author saysky
 * @date 2024-01-03
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private BillService billService;

    /**
     * 查询订单
     *
     * @param id 订单主键
     * @return 订单
     */
    @Override
    public Order selectOrderById(Long id) {
        Order order = orderMapper.selectOrderById(id);
        order.setOwnerUser(userService.selectUserById(order.getOwnerUserId()));
        order.setCustomerUser(userService.selectUserById(order.getCustomerUserId()));
        order.setHouse(houseService.getById(order.getHouseId()));
        return order;
    }

    /**
     * 查询订单列表
     *
     * @param order 订单
     * @return 订单
     */
    @Override
    public List<Order> selectOrderList(Order order) {
        List<Order> orders = orderMapper.selectOrderList(order);
        Map<Long, SysUser> userMap = new HashMap<>();
        for (Order item : orders) {
            if (userMap.containsKey(item.getOwnerUserId())) {
                item.setOwnerUser(userMap.get(item.getOwnerUserId()));
            } else {
                SysUser user = userService.selectUserById(item.getOwnerUserId());
                item.setOwnerUser(user);
                userMap.put(item.getOwnerUserId(), user);
            }

            if (userMap.containsKey(item.getCustomerUserId())) {
                item.setCustomerUser(userMap.get(item.getCustomerUserId()));
            } else {
                SysUser user = userService.selectUserById(item.getCustomerUserId());
                item.setCustomerUser(user);
                userMap.put(item.getCustomerUserId(), user);
            }
        }
        return orders;
    }

    /**
     * 新增订单
     *
     * @param order 订单
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertOrder(Order order) {
        House house = houseService.getById(order.getHouseId());
        if (house == null || house.getStatus() != 0) {
            throw new ServiceException("房屋不存在");
        }
        if (order.getMonthNum() < 6 || order.getMonthNum() > 60) {
            throw new ServiceException("租期必须在6-60个月之间");
        }
        order.setId(generateId());
        order.setMonthRent(house.getMonthRent());
        order.setTotalAmount(house.getMonthRent() * order.getMonthNum());
        order.setOwnerUserId(house.getUserId());
        order.setCreateTime(new Date());
        order.setStatus(-2);

        order.setStartDate(new Date());
        // 退租日期=开始日期+月数
        Calendar c = Calendar.getInstance();
        c.setTime(order.getStartDate());
        c.add(Calendar.MONTH, order.getMonthNum());
        order.setEndDate(c.getTime());
        orderMapper.insertOrder(order);
        return 1;
    }

    /**
     * 修改订单
     *
     * @param order 订单
     * @return 结果
     */
    @Override
    public int updateOrder(Order order) {
        return orderMapper.updateOrder(order);
    }

    /**
     * 批量删除订单
     *
     * @param ids 需要删除的订单主键
     * @return 结果
     */
    @Override
    public int deleteOrderByIds(Long[] ids) {
        return orderMapper.deleteOrderByIds(ids);
    }

    /**
     * 删除订单信息
     *
     * @param id 订单主键
     * @return 结果
     */
    @Override
    public int deleteOrderById(Long id) {
        return orderMapper.deleteOrderById(id);
    }

    @Override
    public List<Order> selectOverDueOrder() {
        return orderMapper.selectOverDueOrder(DateUtils.getNowDate());
    }

    @Override
    public int countByHouseId(Long houseId) {
        return orderMapper.countByHouseId(houseId);
    }

    /**
     * 生成主键: 年月日+8位自增长码
     * 2024010300000001
     *
     * @return
     */
    private Long generateId() {
        String idPrefix = DateUtils.dateTimeNow("yyyyMMdd");
        Long maxId = orderMapper.getMaxId(idPrefix);
        if (maxId == null) {
            return Long.parseLong(idPrefix + "00000001");
        }
        return maxId + 1;
    }

    @Override
    public List<Order> getActiveList(Long userId) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            queryWrapper.eq(Order::getCustomerUserId, userId);
        }
        queryWrapper.eq(Order::getStatus, 0);
        queryWrapper.orderByDesc(Order::getCreateTime);
        List<Order> orders = this.orderMapper.selectList(queryWrapper);
        orders.forEach(order -> {
            order.setHouse(houseService.getById(order.getHouseId()));
        });
        return orders;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payOrder(Long id) {
        Order order = this.getById(id);
        if (order != null) {
            order.setStatus(0); // 0表示生效中
            this.updateById(order);

            // 新建账单
            Bill bill = new Bill();
            bill.setHouseId(order.getHouseId());
            bill.setOrderId(order.getId());
            bill.setCreateTime(new Date());
            bill.setCustomerUserId(order.getCustomerUserId());
            bill.setOwnerUserId(order.getOwnerUserId());
            bill.setTotalAmount(order.getTotalAmount());
            bill.setTitle("房租");
            bill.setStatus(1);
            billService.insertBill(bill);

            House house = houseService.getById(order.getHouseId());
            if (house != null) {
                house.setStatus(1); // 1表示已租出
                houseService.updateById(house);
            }
        }
    }
}
