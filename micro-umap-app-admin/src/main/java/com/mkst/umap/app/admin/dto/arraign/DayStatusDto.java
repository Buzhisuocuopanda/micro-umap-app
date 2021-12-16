package com.mkst.umap.app.admin.dto.arraign;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName DayStatusDto
 * @Description 某日状态的Dto
 * @Author hsw
 * @Date 2020-06-28 16:54
 */
@Data
public class DayStatusDto {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private String weekDay;
    private String status;
    private Integer index;
}
