package com.mkst.umap.app.admin.api.bean.result.canteen;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkst.umap.app.admin.api.bean.result.car.AuditParam;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class CanteenDetailResult {

    private List<AuditParam> auditParamList;
    /** 食堂申请id */
    private Long canteenApplyId;

    /** 用餐人数 */
    private Integer diningNumber;
    /** 用餐类型 */
    private String diningType;

    /** 日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm:ss")
    private Date createTime;

    //包厢名
    private String name;
    private String progress;
    //包厢地址
    private String address;
    //餐次类型名
    private String typeName;
    //餐次类型code
    private String typeCode;
    //申请人
    private String userName;
    //部门名
    private String deptName;

    private Integer applyStatus;

    private String canAudit;

    private String nextRole;

    private Long useById;
}
