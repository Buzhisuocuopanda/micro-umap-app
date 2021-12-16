package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.domain.ArraignRoomEquipment;

import java.util.List;

/**
 * 提审室设配 数据层
 *
 * @author lijinghui
 * @date 2020-06-11
 */
public interface ArraignRoomEquipmentMapper {
    /**
     * 查询提审室设配信息
     *
     * @param id 提审室设配ID
     * @return 提审室设配信息
     */
    public ArraignRoomEquipment selectArraignRoomEquipmentById(Integer id);

    /**
     * 查询提审室设配列表
     *
     * @param arraignRoomEquipment 提审室设配信息
     * @return 提审室设配集合
     */
    public List<ArraignRoomEquipment> selectArraignRoomEquipmentList(ArraignRoomEquipment arraignRoomEquipment);

    /**
     * 新增提审室设配
     *
     * @param arraignRoomEquipment 提审室设配信息
     * @return 结果
     */
    public int insertArraignRoomEquipment(ArraignRoomEquipment arraignRoomEquipment);

    /**
     * 修改提审室设配
     *
     * @param arraignRoomEquipment 提审室设配信息
     * @return 结果
     */
    public int updateArraignRoomEquipment(ArraignRoomEquipment arraignRoomEquipment);

    /**
     * 删除提审室设配
     *
     * @param id 提审室设配ID
     * @return 结果
     */
    public int deleteArraignRoomEquipmentById(Long id);

    /**
     * 批量删除提审室设配
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteArraignRoomEquipmentByIds(String[] ids);

}