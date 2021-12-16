package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.api.bean.param.RoomScheduleParam;
import com.mkst.umap.app.admin.domain.ArraignRoom;
import com.mkst.umap.app.admin.dto.arraign.ArraignRoomListDto;
import com.mkst.umap.app.admin.dto.arraign.CountDto;

import java.util.Date;
import java.util.List;

/**
 * 提审室 数据层
 *
 * @author lijinghui
 * @date 2020-06-12
 */
public interface ArraignRoomMapper {
    /**
     * 查询提审室信息
     *
     * @param id 提审室ID
     * @return 提审室信息
     */
    public ArraignRoom selectArraignRoomById(String id);

    /**
     * 查询近30天提审室预约情况
     * @return
     */
    List<CountDto> selectRoomType0And30Day();

    /**
     * 查询提审室列表
     *
     * @param arraignRoom 提审室信息
     * @return 提审室集合
     */
    public List<ArraignRoom> selectArraignRoomList(ArraignRoom arraignRoom);

    /**
     * 新增提审室
     *
     * @param arraignRoom 提审室信息
     * @return 结果
     */
    public int insertArraignRoom(ArraignRoom arraignRoom);

    /**
     * 修改提审室
     *
     * @param arraignRoom 提审室信息
     * @return 结果
     */
    public int updateArraignRoom(ArraignRoom arraignRoom);

    /**
     * 删除提审室
     *
     * @param id 提审室ID
     * @return 结果
     */
    public int deleteArraignRoomById(String id);

    /**
     * 批量删除提审室
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteArraignRoomByIds(String[] ids);

    /**
     * @return java.util.List<com.mkst.umap.app.admin.api.bean.result.arraign.ArraignRoomResult>
     * @Author wangyong
     * @Description 获取满足条件的提审室
     * @Date 19:53 2020-07-02
     * @Param [arraignRoomResult]
     */
    List<ArraignRoomListDto> selectRoomScheduleResult(ArraignRoomListDto arraignRoomListDto);

    Date getNextStartTime(RoomScheduleParam roomScheduleParam);

}