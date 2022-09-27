package com.mkst.umap.app.admin.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkst.mini.systemplus.common.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 我的消费表 umap_user_spend
 * 
 * @author wangyong
 * @date 2020-11-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class UserSpend extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 主键 */
	private Long id;
	/** 交易用户 */
	private Long userId;
	private String userName;
	private String deptName;
	/** 交易类型（1：充值、2：食堂） */
	private String type;
	/** 消费小类（1：早餐、2：中餐、3：晚餐） */
	private String subType;
	/** 交易金额 */
	private BigDecimal amount;
	/** 当前余额 */
	private BigDecimal balance;
	/** 交易时间 */
	private Date payTime;
	/** 创建时间 */
	private Date createTime;

	/** 查询参数 */
	/** 开始日期 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date checkStartTime;
    /** 结束 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkEndTime;
	
	private String phonenumber;

}
