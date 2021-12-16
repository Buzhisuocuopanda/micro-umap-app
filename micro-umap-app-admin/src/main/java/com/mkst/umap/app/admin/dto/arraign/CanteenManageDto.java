package com.mkst.umap.app.admin.dto.arraign;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CanteenManageDto {
    //包厢id
    private String id;
    //包厢名
    private String name;
    private String address;
    //最大人数
    private Integer total;
    //使用状态
    private List<Map<String,Object>> useStatus;
}
