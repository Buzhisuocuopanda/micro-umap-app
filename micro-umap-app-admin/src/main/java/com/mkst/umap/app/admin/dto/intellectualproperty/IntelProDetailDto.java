package com.mkst.umap.app.admin.dto.intellectualproperty;

import lombok.Data;

import java.util.ArrayList;

/**
 * @ClassName IntelProDetailDto
 * @Description 知识产权-详情
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-12 18:45
 */
@Data
public class IntelProDetailDto {

    private Long id;

    private String title;

    private String content;

    private String hasReplied;

    private ArrayList<String> fileList;
}
