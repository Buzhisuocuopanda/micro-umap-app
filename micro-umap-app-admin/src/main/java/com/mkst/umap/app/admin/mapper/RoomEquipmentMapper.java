package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.domain.RoomEquipment;

import java.util.List;

/**
 * 提审室设配 数据层
 *
 * @author wangyong
 * @date 2020-09-08
 */
public interface RoomEquipmentMapper {
    /**
     * 查询提审室设配信息
     *
     * @param id 提审室设配ID
     * @return 提审室设配信息
     */
    public RoomEquipment selectRoomEquipmentById(Long id);

    /**
     * 查询提审室设配列表
     *
     * @param roomEquipment 提审室设配信息
     * @return 提审室设配集合
     */
    public List<RoomEquipment> selectRoomEquipmentList(RoomEquipment roomEquipment);

    /**
     * 新增提审室设配
     *
     * @param roomEquipment 提审室设配信息
     * @return 结果
     */
    public int insertRoomEquipment(RoomEquipment roomEquipment);

    /**
     * 修改提审室设配
     *
     * @param roomEquipment 提审室设配信息
     * @return 结果
     */
    public int updateRoomEquipment(RoomEquipment roomEquipment);

    /**
     * 删除提审室设配
     *
     * @param id 提审室设配ID
     * @return 结果
     */
    public int deleteRoomEquipmentById(Long id);

    /**
     * 批量删除提审室设配
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRoomEquipmentByIds(String[] ids);

    int deleteRoomEquipmentByRoomId(String roomId);
}