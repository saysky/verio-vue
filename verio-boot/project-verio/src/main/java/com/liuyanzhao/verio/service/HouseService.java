package com.liuyanzhao.verio.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liuyanzhao.verio.domain.House;

/**
 * 房屋Service接口
 * 
 * @author saysky
 * @date 2024-01-03
 */
public interface HouseService extends IService<House>
{
    /**
     * 查询房屋
     * 
     * @param id 房屋主键
     * @return 房屋
     */
    House selectHouseById(Long id);

    /**
     * 查询房屋列表
     * 
     * @param house 房屋
     * @return 房屋集合
     */
    List<House> selectHouseList(House house);

    /**
     * 新增房屋
     * 
     * @param house 房屋
     * @return 结果
     */
    int insertHouse(House house);

    /**
     * 修改房屋
     * 
     * @param house 房屋
     * @return 结果
     */
    int updateHouse(House house);

    /**
     * 批量删除房屋
     * 
     * @param ids 需要删除的房屋主键集合
     * @return 结果
     */
    int deleteHouseByIds(Long[] ids);

    /**
     * 删除房屋信息
     * 
     * @param id 房屋主键
     * @return 结果
     */
    int deleteHouseById(Long id);
}
