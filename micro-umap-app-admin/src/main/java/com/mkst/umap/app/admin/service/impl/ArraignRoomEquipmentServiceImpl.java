package com.mkst.umap.app.admin.service.impl;

import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.umap.app.admin.domain.ArraignRoomEquipment;
import com.mkst.umap.app.admin.mapper.ArraignRoomEquipmentMapper;
import com.mkst.umap.app.admin.service.IArraignRoomEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 提审室设配 服务层实现
 *
 * @author lijinghui
 * @date 2020-06-11
 */
@Service
public class ArraignRoomEquipmentServiceImpl implements IArraignRoomEquipmentService {
    @Autowired
    private ArraignRoomEquipmentMapper arraignRoomEquipmentMapper;

    /**
     * 查询提审室设配信息
     *
     * @param id 提审室设配ID
     * @return 提审室设配信息
     */
    @Override
    public ArraignRoomEquipment selectArraignRoomEquipmentById(Integer id) {
        return arraignRoomEquipmentMapper.selectArraignRoomEquipmentById(id);
    }

    /**
     * 查询提审室设配列表
     *
     * @param arraignRoomEquipment 提审室设配信息
     * @return 提审室设配集合
     */
    @Override
    public List<ArraignRoomEquipment> selectArraignRoomEquipmentList(ArraignRoomEquipment arraignRoomEquipment) {
        return arraignRoomEquipmentMapper.selectArraignRoomEquipmentList(arraignRoomEquipment);
    }

    /**
     * 新增提审室设配
     *
     * @param arraignRoomEquipment 提审室设配信息
     * @return 结果
     */
    @Override
    public int insertArraignRoomEquipment(ArraignRoomEquipment arraignRoomEquipment) {
        return arraignRoomEquipmentMapper.insertArraignRoomEquipment(arraignRoomEquipment);
    }

    /**
     * 修改提审室设配
     *
     * @param arraignRoomEquipment 提审室设配信息
     * @return 结果
     */
    @Override
    public int updateArraignRoomEquipment(ArraignRoomEquipment arraignRoomEquipment) {
        return arraignRoomEquipmentMapper.updateArraignRoomEquipment(arraignRoomEquipment);
    }

    /**
     * 删除提审室设配对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteArraignRoomEquipmentByIds(String ids) {
        return arraignRoomEquipmentMapper.deleteArraignRoomEquipmentByIds(Convert.toStrArray(ids));
    }

}
