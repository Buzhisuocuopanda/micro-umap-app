package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName UserSpendLog
 * @Description
 * @Author wangyong
 * @Modified By:
 * @Date 2020-11-03 18:04
 */
@Data
public class UserSpendLog {

    @Excel(name = "*序号")
    private Long no;

    @Excel(name = "*姓名")
    private String name;

    @Excel(name = "*人员编号")
    private String peopleNo;
    @Excel(name = "*工号")
    private String jobNo;

    @Excel(name = "*所属部门")
    private String deptName;

    @Excel(name = "*项目名称")
    private String projectName;

    /** 交易金额 */
    @Excel(name = "*消费金额")
    private BigDecimal amount;
    /** 交易时间 */
    @Excel(name = "*消费时间")
    private Date payTime;
    @Excel(name = "*月份")
    private String month;
    /** 当前余额 */
    @Excel(name = "*账户余额")
    private BigDecimal balance;

}
