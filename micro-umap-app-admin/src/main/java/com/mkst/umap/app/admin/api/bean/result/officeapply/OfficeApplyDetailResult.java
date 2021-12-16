package com.mkst.umap.app.admin.api.bean.result.officeapply;

import com.mkst.mini.systemplus.workflow.domain.WfEventDetail;
import com.mkst.umap.app.admin.domain.OfficeApplyDevice;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName OfficeApplyDetailResult
 * @Description 办公申请详情
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-24 11:25
 */
@Data
@ApiModel(value = "办公申请详情")
public class OfficeApplyDetailResult {
    /**
     * 申请id
     */
    private Long id;
    private String dept;
    /**
     * 办公申请类型
     */
    private String type;
    private Integer progress;
    /**
     * 标题
     */
    private String title;
    private String content;
    private String destination;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人姓名
     */
    private String createByName;

    /**
     * 申请状态
     */
    private String status;

    private WfEventDetail eventDetail;

    /**
     * 创建人姓名
     */
    private String auditStatus;

    private List<OfficeApplyDevice> deviceList;
}
