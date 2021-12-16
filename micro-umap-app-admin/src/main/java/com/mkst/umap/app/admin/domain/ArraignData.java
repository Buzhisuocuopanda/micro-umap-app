package com.mkst.umap.app.admin.domain;

import lombok.Data;

/**
 * @ClassName ArraignData
 * @Description 提审室同步数据
 * @Author wangyong
 * @Modified By:
 * @Date 2020-11-24 14:54
 */
@Data
public class ArraignData {
    private Long applyer;
    private Long applytime;
    private String birthday;
    private String certificateId;
    private String crimetype;
    private String criminal;
    private String descr;
    private String prosecuteName;
    private int roomtype;
    private int status;
    private Long time1;
    private Long time2;
    private String undertaker;
}
