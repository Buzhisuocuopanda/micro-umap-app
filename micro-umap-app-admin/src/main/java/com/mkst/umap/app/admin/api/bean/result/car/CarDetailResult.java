package com.mkst.umap.app.admin.api.bean.result.car;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkst.umap.app.admin.domain.MapLocation;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class CarDetailResult {

    List<AuditParam> auditParamList;
    private MapLocation startPoint;
    private MapLocation endPoint;

    /**
     *  车辆预约申请id
     */
    private Long carApplyId;
    /**
     *  车辆id
     */
    private Long carId;
    /**
     *  申请人
     */
    private Long userId;
    /**
     *  司机id
     */
    private Long driverId;
    /**
     * 司机名字
     */
    private String driverName;
    /**
     *  联系方式
     */
    private String telphone;
    /**
     *  是否需要司机
     */
    private Boolean driverWhether;
    /**
     *  人数（含司机）
     */
    private Integer peopleNumber;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm")
    private Date createTime;

    /**
     *  开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm")
    private Date startTime;
    /**
     *  结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm")
    private Date endTime;
    /**
     *  地区选择
     */
    private String areaSelect;
    /**
     *  起点位置id
     */
    private Long startLocationId;
    /**
     *  终点位置id
     */
    private Long endLocationId;
    /**
     *  审批状态
     */
    private Integer approveStatus;
    /**
     * 备注
     */
    private String remark;
    /**
     * 申请人名字
     */
    private String userName;
    /**
     * 车牌号
     */
    private String licensePlateNumber;
    /**
     * 进程
     */
    private Integer progress;
    /**
     * 审批人
     */
    private Integer approvalUserId;
    /**
     * 是否可以审批
     */
    private String canAudit;
    /**
     * 是否需要额外审批
     */
    Boolean needExtraApproval;
    /**
     * 司机号码
     */
    private String driverPhoneNumber;
}
