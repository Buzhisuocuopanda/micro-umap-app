package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.domain.ArraignRoomEquipment;

import java.util.List;

/**
 * 提审室设配 服务层
 *
 * @author lijinghui
 * @date 2020-06-11
 */
public interface IArraignRoomEquipmentService {
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
     * 删除提审室设配信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteArraignRoomEquipmentByIds(String ids);

}
