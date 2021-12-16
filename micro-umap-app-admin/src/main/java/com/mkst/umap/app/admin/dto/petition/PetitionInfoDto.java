package com.mkst.umap.app.admin.dto.petition;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName PetitionInfoDto
 * @Description 信访列表信息
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-26 15:02
 */
@Data
public class PetitionInfoDto {

    private Long id;

    private String type;

    private String createByName;

    private String title;

    private boolean isLegal;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String anonymous;

}
