package com.mkst.umap.app.admin.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.mkst.mini.systemplus.common.annotation.Excel;

import lombok.Data;

/**
 * @ClassName UserSpendLog
 * @Description
 * @Author wangyong
 * @Modified By:
 * @Date 2020-11-03 18:04
 */
@Data
public class UserSpendLog {
	
    @Excel(name = "交易月份")
    private String batchMonth;
    @Excel(name = "用户ID")
    private Long userId;
    @Excel(name = "用户姓名")
    private String userName;
    @Excel(name = "手机号码")
    private String phonenumber;
    @Excel(name = "交易类型")
    private String transactionType;
    @Excel(name = "交易类型名称")
    private String transactionTypeName;
    @Excel(name = "交易金额")
    private BigDecimal transactionAmount;
    @Excel(name = "当前余额")
    private BigDecimal balance;
    @Excel(name = "交易时间")
    private Date transactionTime;

}
