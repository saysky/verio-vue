package com.liuyanzhao.verio.service.impl;

import java.util.*;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liuyanzhao.common.core.domain.entity.SysUser;
import com.liuyanzhao.common.utils.DateUtils;
import com.liuyanzhao.verio.domain.Post;
import com.liuyanzhao.verio.service.*;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.liuyanzhao.verio.mapper.HouseMapper;
import com.liuyanzhao.verio.domain.House;
import org.springframework.transaction.annotation.Transactional;

/**
 * 房屋Service业务层处理
 *
 * @author saysky
 * @date 2024-01-03
 */
@Service
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House> implements HouseService {
    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private ISysUserService userService;


    /**
     * 查询房屋
     *
     * @param id 房屋主键
     * @return 房屋
     */
    @Override
    public House selectHouseById(Long id) {
        return houseMapper.selectHouseById(id);
    }

    /**
     * 查询房屋列表
     *
     * @param house 房屋
     * @return 房屋
     */
    @Override
    public List<House> selectHouseList(House house) {
        List<House> houses = houseMapper.selectHouseList(house);
        Map<Long, SysUser> userMap = new HashMap<>();
        for (House item : houses) {
            if (userMap.containsKey(item.getUserId())) {
                item.setUser(userMap.get(item.getUserId()));
            } else {
                SysUser user = userService.selectUserById(item.getUserId());
                item.setUser(user);
                userMap.put(item.getUserId(), user);
            }
        }
        return houses;
    }

    /**
     * 新增房屋
     *
     * @param house 房屋
     * @return 结果
     */
    @Override
    @Synchronized
    public int insertHouse(House house) {
        house.setCreateTime(new Date());
        house.setId(generateId());
        house.setStatus(0);
        return houseMapper.insertHouse(house);
    }

    /**
     * 修改房屋
     *
     * @param house 房屋
     * @return 结果
     */
    @Override
    public int updateHouse(House house) {
        return houseMapper.updateHouse(house);
    }

    /**
     * 批量删除房屋
     *
     * @param ids 需要删除的房屋主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteHouseByIds(Long[] ids) {
        return houseMapper.deleteHouseByIds(ids);
    }

    /**
     * 删除房屋信息
     *
     * @param id 房屋主键
     * @return 结果
     */
    @Override
    public int deleteHouseById(Long id) {
        return houseMapper.deleteHouseById(id);
    }

    /**
     * 生成主键: 年月日+8位自增长码
     * 2024010300000001
     *
     * @return
     */
    private Long generateId() {
        String idPrefix = DateUtils.dateTimeNow("yyyyMMdd");
        Long maxId = houseMapper.getMaxId(idPrefix);
        if (maxId == null) {
            return Long.parseLong(idPrefix + "00000001");
        }
        return maxId + 1;
    }
}
