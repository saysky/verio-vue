package com.liuyanzhao.verio.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.liuyanzhao.common.core.domain.entity.SysUser;
import com.liuyanzhao.verio.domain.common.CommonEntity;
import com.liuyanzhao.verio.service.HouseService;
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
 * 订单对象 t_order
 * 
 * @author saysky
 * @date 2024-01-03
 */
@Data
@TableName("t_order")
public class Order extends CommonEntity
{
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private Long id;

    /** 租客用户ID */
    private Long customerUserId;

    /** 房东用户ID */
    private Long ownerUserId;

    /** 订单状态：-1待付款，-2待签合同，0生效中，1已到期，-3订单已取消 */
    private Integer status;

    /** 房子ID */
    private Long houseId;

    /** 月租金 */
    private Long monthRent;

    /** 总金额 */
    private Long totalAmount;

    /** 月数 */
    private Integer monthNum;

    /** 开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    /** 结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;


    @TableField(exist = false)
    private SysUser customerUser;

    @TableField(exist = false)
    private SysUser ownerUser;


    @TableField(exist = false)
    private House house;

}
