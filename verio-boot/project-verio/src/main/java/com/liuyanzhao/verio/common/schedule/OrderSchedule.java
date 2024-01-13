package com.liuyanzhao.verio.common.schedule;

import com.liuyanzhao.verio.domain.House;
import com.liuyanzhao.verio.domain.Order;
import com.liuyanzhao.verio.service.HouseService;
import com.liuyanzhao.verio.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 言曌
 * @since 2024/1/5 11:00
 */
@Component
public class OrderSchedule {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HouseService houseService;

    /**
     * 更新到期的订单
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void scheduled() {
        System.out.println("更新到期的订单");
        List<Order> orders = orderService.selectOverDueOrder();
        for (Order order : orders) {
            order.setStatus(1);
            orderService.updateById(order);

            House house = houseService.getById(order.getHouseId());
            if (house != null) {
                house.setStatus(0);
                houseService.updateById(house);
            }
        }
    }
}
