package com.mkst.umap.app.admin.api.bean.result.officeapply;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName OfficeApplyInfoResult
 * @Description mapper结果
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-07 16:36
 */
@Data
public class OfficeApplyInfoResult {

    /**
     * 申请id
     */
    private Long id;
    /**
     * 办公申请类型
     */
    private String type;
    /**
     * 标题
     */
    private String title;
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

    /**
     * 创建人姓名
     */
    private String auditStatus;
}
