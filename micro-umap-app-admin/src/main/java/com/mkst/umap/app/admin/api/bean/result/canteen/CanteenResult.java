package com.mkst.umap.app.admin.api.bean.result.canteen;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class CanteenResult {
    private Long canteenApplyId;
    private String name;
    private String address;
    private String typeName;
    private String typeCode;
    private String deptName;
    private Integer diningStatus;
    private String diningType;
    private Integer applyStatus;
    private String userName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
