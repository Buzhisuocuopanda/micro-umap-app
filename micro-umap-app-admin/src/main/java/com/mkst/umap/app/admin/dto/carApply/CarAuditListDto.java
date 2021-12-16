package com.mkst.umap.app.admin.dto.carApply;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName CarDetailDto
 * @Description TODO
 * @Author wangyong
 * @Modified By:
 * @Date 2020-12-01 11:50
 */
@Data
public class CarAuditListDto extends BaseEntity {


    /** 车辆预约申请id */
    private Long carApplyId;

    /** 申请人 */
    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm")
    private Date createTime;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm")
    private Date startTime;
    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm")
    private Date endTime;


    /** 审批状态 */
    private Integer approveStatus;


    private String licensePlateNumber;

    private Integer progress;

    //审批人
    private Integer approvalUserId;


}
