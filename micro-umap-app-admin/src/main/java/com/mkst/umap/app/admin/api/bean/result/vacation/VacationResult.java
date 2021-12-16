package com.mkst.umap.app.admin.api.bean.result.vacation;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName VacationResult
 * @Description 请假相关信息
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-24 17:55
 */
@Data
public class VacationResult {
    /** id */
    private Long id;
    /** 请假类型 */
    private String type;
    /** 开始时间 */
    private Date startTime;
    /** 结束时间 */
    private Date endTime;
    /** 事由 */
    private String content;
    /** 审核人 */
    private String approveToName;
    /** 是否取消 */
    private String status;
    /** 审核状态 */
    private String auditStatus;
    /** 创建人 */
    private String createByName;
    /** 创建时间 */
    private Date createTime;
}
