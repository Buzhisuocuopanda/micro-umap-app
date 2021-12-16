package com.mkst.umap.app.admin.dto.reception;

import com.mkst.umap.app.admin.api.bean.result.reception.ReceptionInfoResult;
import lombok.Data;

import java.util.LinkedList;

/**
 * @ClassName RoomInfoDto
 * @Description 房间列表信息的Dto
 * @Author wangyong
 * @Date 2020-07-08 18:46
 */
@Data
public class RoomInfoDto {

    /**
     * 房间名
     */
    private String name;

    /**
     * 房间地址
     */
    private String address;

    /**
     * 申请信息
     */
    private LinkedList<ReceptionInfoResult> ongoingAppointmentResult;
}
