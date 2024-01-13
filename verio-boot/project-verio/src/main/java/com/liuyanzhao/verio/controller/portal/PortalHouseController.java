package com.liuyanzhao.verio.controller.portal;

import com.liuyanzhao.common.annotation.Log;
import com.liuyanzhao.common.core.controller.BaseController;
import com.liuyanzhao.common.core.domain.AjaxResult;
import com.liuyanzhao.common.core.page.TableDataInfo;
import com.liuyanzhao.common.enums.BusinessType;
import com.liuyanzhao.common.exception.ServiceException;
import com.liuyanzhao.verio.domain.House;
import com.liuyanzhao.verio.service.HouseService;
import com.liuyanzhao.verio.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 房屋Controller
 *
 * @author saysky
 * @date 2024-01-03
 */
@RestController
@RequestMapping("/verio/portal/house")
public class PortalHouseController extends BaseController {
    @Autowired
    private HouseService houseService;

    @Autowired
    private OrderService orderService;

    /**
     * 查询房屋列表
     */
    @GetMapping("/list")
    public TableDataInfo list(House house) {
        startPage();
        house.setStatus(0);
        List<House> list = houseService.selectHouseList(house);
        return getDataTable(list);
    }


    /**
     * 获取房屋详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(houseService.selectHouseById(id));
    }

}
