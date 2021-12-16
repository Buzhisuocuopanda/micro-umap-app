package com.mkst.umap.app.admin.api.bean.param;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName AppointmentArraignSortParam
 * @Description
 * @Author yinyuanping
 * @Modified By:
 * @Date 2021/1/12 0012 上午 10:48
 */
@Data
public class AppointmentArraignSortParam {

    private Long id;
    private Integer numOrder;
    private Date checkDate;
    private String timePie;

}
