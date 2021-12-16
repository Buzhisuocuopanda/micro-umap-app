package com.mkst.umap.app.admin.dto.intellectualproperty;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName IntelProInfoDto
 * @Description 知识产权-我的列表
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-12 17:57
 */
@Data
public class IntelProInfoDto {

    private Long id;

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
