package com.liuyanzhao.verio.domain;

import java.math.BigDecimal;

import com.liuyanzhao.common.core.domain.entity.SysUser;
import com.liuyanzhao.verio.domain.common.CommonEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 房屋对象 t_house
 * 
 * @author saysky
 * @date 2024-01-03
 */
@Data
@TableName("t_house")
public class House extends CommonEntity
{
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private Long id;

    /** 房东用户ID */
    private Long userId;

    /** 房屋名称 */
    private String title;

    /** 详细描述内容 */
    private String content;

    /** 城市名称 */
    private String city;

    /** 详细地址，具体门牌号 */
    private String address;

    /** 缩略图URL */
    private String thumbnailUrl;

    /** 轮播图URL */
    private String slideUrl;

    /** 月租金额 */
    private Long monthRent;

    /** 0未租出，1已租出，,-1已下架 */
    private Integer status;

    /** 房产证号 */
    private String certificateNo;

    /** 卫生间数量 */
    private Long toiletNum;

    /** 厨房数量 */
    private Long kitchenNum;

    /** 客厅数量 */
    private Long livingRoomNum;

    /** 卧室数量 */
    private Long bedroomNum;

    /** 是否有空调，1有，0无 */
    private Long hasAirConditioner;

    /** 面积 */
    private BigDecimal area;

    /** 当前所在楼层数 */
    private Long floor;

    /** 房子最大楼层数 */
    private Long maxFloor;

    /** 是否有电梯 */
    private Long hasElevator;

    /** 建成年份 */
    private Long buildYear;

    /** 朝向 */
    private String direction;

    /** 经纬度 */
    private String longitudeLatitude;

    /** 联系人姓名 */
    private String contactName;

    /** 联系人手机 */
    private String contactPhone;

    @TableField(exist = false)
    private SysUser user;

}
