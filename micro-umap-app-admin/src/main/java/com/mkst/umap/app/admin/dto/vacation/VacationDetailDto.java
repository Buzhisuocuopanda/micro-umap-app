package com.mkst.umap.app.admin.dto.vacation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName VacationDetailDto
 * @Description 请假管理-申请详情
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-24 19:20
 */
@Data
public class VacationDetailDto {
    /** id */
    private Long id;
    /** 请假类型 */
    private String type;
    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;
    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
    /** 时长 */
    private int duration;
    /** 事由 */
    private String content;
    /** 是否取消 */
    private String status;
}
