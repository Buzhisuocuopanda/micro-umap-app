package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName ArraignAppointmentOld
 * @Description
 * @Author yinyuanping
 * @Modified By:
 * @Date 2020/12/4 0004 上午 9:06
 */
@Data
public class ArraignAppointmentOld extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 老系统ID
     */
    private Long oldId;

    /**
     * ArraignAppointment.id
     */
    private Long arraignAppointmentId;
    /**
     * 审核状态
     */
    private String status;
    /**
     * 拉取时间
     */
    private Date pullTime;
    /**
     * 同步时间
     */
    private Date updateTime;

}
