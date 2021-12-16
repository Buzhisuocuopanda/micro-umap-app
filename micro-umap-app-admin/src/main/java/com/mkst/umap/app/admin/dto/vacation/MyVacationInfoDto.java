package com.mkst.umap.app.admin.dto.vacation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName MyVacationInfoDto
 * @Description 请假管理-我的申请
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-24 18:24
 */
@Data
public class MyVacationInfoDto {
    /** id */
    private Long id;
    /** 请假类型 */
    private String type;
    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;
    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
    /** 审核人 */
    private String approveToName;
    /** 是否取消 */
    private String status;
    /** 审核状态 */
    private String auditStatus;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
