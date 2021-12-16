package com.mkst.umap.app.admin.dto.carApply;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import com.mkst.mini.systemplus.workflow.domain.WfEventDetail;
import com.mkst.umap.app.admin.api.bean.result.car.AuditParam;
import com.mkst.umap.app.admin.domain.MapLocation;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @ClassName CarDetailDto
 * @Description TODO
 * @Author wangyong
 * @Modified By:
 * @Date 2020-12-01 11:50
 */
@Data
public class CarDetailDto extends BaseEntity {


    List<AuditParam> auditParamList;
    private MapLocation startPoint;
    private MapLocation endPoint;

    /** 车辆预约申请id */
    private Long carApplyId;
    /** 车辆id */
    private Long carId;
    /** 申请人 */
    private Long userId;
    /** 司机id */
    private Long driverId;
    /**司机名字 */
    private String driverName;
    /** 联系方式 */
    private String telphone;
    /** 是否需要司机 */
    private Boolean driverWhether;
    /** 人数（含司机） */
    private Integer peopleNumber;

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
    /** 地区选择 */
    private String areaSelect;
    /** 起点位置id */
    private Long startLocationId;
    /** 终点位置id */
    private Long endLocationId;
    /** 审批状态 */
    private Integer approveStatus;

    private String remark;

    private String userName;

    /**
     * 车辆车牌号
     */
    private String licensePlateNumber;

    private Integer progress;

    /**
     * 审批人
     */
    private Integer approvalUserId;

    private Boolean isCarApproval;

    private String canAudit;

    private WfEventDetail wfEventDetail;

    /**
     * 司机号码
     */
    private String driverPhoneNumber;
}
