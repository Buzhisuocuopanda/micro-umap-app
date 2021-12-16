package com.mkst.umap.app.admin.dto.officeapply;

import com.mkst.umap.app.admin.domain.OfficeApplyDevice;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName OfficeApplyDetailDto
 * @Description 申请详情
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-24 11:42
 */
@Data
public class OfficeApplyDetailDto {
    /**
     * 申请id
     */
    private Long id;
    private String dept;

    private String canAudit;
    /**
     * 标题
     */
    private String title;
    private String type;
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
     * 申请状态
     */
    private String status;

    /**
     * 创建人姓名
     */
    private String auditStatus;
    private List<OfficeApplyDevice> deviceList;
    private String nextRole;
    private String auditProgress;
    private Long useById;
}
