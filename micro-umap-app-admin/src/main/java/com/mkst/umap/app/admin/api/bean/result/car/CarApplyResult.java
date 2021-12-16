package com.mkst.umap.app.admin.api.bean.result.car;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkst.umap.app.admin.domain.MapLocation;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class CarApplyResult {

    /** 车辆预约申请id */
    private Long carApplyId;

    private Integer approveId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm")
    private Date endTime;

    private String approveStatus;

    private String licensePlateNumber;

    private MapLocation startPoint;

    private MapLocation endPoint;
}
