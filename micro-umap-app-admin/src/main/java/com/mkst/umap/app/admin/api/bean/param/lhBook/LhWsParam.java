package com.mkst.umap.app.admin.api.bean.param.lhBook;

import lombok.Data;

import java.util.List;

/**
 * @Auther: ltt
 * @Date: 2020/09/17/15:23
 * @Description:
 */
@Data
public class LhWsParam {

    /**地区*/
    private List<String> areaList;

    /**犯罪类型*/
    private List<String> typeList;

    private String  content;

    private String publishTime;
    /**起始时间*/
    private String startTime;
    /**结束时间*/
    private String endTime;
}
