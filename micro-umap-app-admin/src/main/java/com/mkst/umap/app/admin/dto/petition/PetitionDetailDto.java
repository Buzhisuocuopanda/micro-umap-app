package com.mkst.umap.app.admin.dto.petition;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName PetitionDetailDto
 * @Description 信访详情
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-26 15:25
 */
@Data
public class PetitionDetailDto {

    private Long id;

    private String type;

    private String title;

    private String content;

    private String createByName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    private List<String> fileList;

    private Map<String, Map<Long,String>> personnelInfo;

    private Map<String,Long> matterInfo;

    private boolean isLegal;

    private String anonymous;
}
