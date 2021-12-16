package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 办公申请表 umap_office_apply
 *
 * @author wangyong
 * @date 2020-08-07
 */
@Data
public class OfficeApply extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 申请id
     */
    private Long id;
    /**
     * 办公申请类型
     */
    private String type;
    /**
     * 申请部门
     */
    private Long deptId;
    /**
     * 申请标题
     */
    private String title;
    /**
     * 详细内容
     */
    private String content;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 目的地
     */
    private String destination;
    /**
     * 审批人ID
     */
    private Long approvalTo;
    /**
     * 当前状态
     */
    private String status;
    /**
     * 审核状态
     */
    private String auditStatus;
    /**
     * 审批流程
     */
    private String progress;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 删除标志(0:正常 1:删除)
     */
    private String delFlag;
    /**
     * 备注
     */
    private String remark;

    private List<OfficeApplyDevice> deviceList;
}
