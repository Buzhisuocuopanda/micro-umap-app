package com.mkst.umap.app.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.mini.systemplus.util.SysConfigUtil;
import com.mkst.umap.app.admin.domain.BackUpRoom;
import com.mkst.umap.app.admin.dto.apply.BackUpRoomDto;
import com.mkst.umap.app.admin.dto.apply.DoorLockDeviceDto;
import com.mkst.umap.app.admin.mapper.BackUpRoomMapper;
import com.mkst.umap.app.admin.service.IBackUpRoomService;
import com.mkst.umap.app.admin.util.WebcardApiUtil;

/**
 * 备勤间 服务层实现
 * 
 * @author lijinghui
 * @date 2020-06-17
 */
@Service
public class BackUpRoomServiceImpl implements IBackUpRoomService {
	@Autowired
	private BackUpRoomMapper backUpRoomMapper;

	/**
	 * 查询备勤间信息
	 * 
	 * @param roomId 备勤间ID
	 * @return 备勤间信息
	 */
	@Override
	public BackUpRoom selectBackUpRoomById(Integer roomId) {
		return backUpRoomMapper.selectBackUpRoomById(roomId);
	}

	/**
	 * 查询备勤间列表
	 * 
	 * @param backUpRoom 备勤间信息
	 * @return 备勤间集合
	 */
	@Override
	public List<BackUpRoom> selectBackUpRoomList(BackUpRoom backUpRoom) {
		return backUpRoomMapper.selectBackUpRoomList(backUpRoom);
	}

	/**
	 * 通过集合id查询房间集合信息
	 * 
	 * @param ids
	 * @return
	 */
	@Override
	public List<BackUpRoom> selectRoomListByIds(List<Long> ids) {
		return backUpRoomMapper.selectRoomListByIds(ids);
	}

	/**
	 * 通过日期获取当天备勤间使用状态
	 * 
	 * @param backUpRoomDto
	 * @return
	 */
	@Override
	public List<BackUpRoom> selectBackUpRoomByDate(BackUpRoomDto backUpRoomDto) {
		return backUpRoomMapper.selectBackUpRoomByDate(backUpRoomDto);
	}

	/**
	 * 新增备勤间
	 * 
	 * @param backUpRoom 备勤间信息
	 * @return 结果
	 */
	@Override
	public int insertBackUpRoom(BackUpRoom backUpRoom) {
		return backUpRoomMapper.insertBackUpRoom(backUpRoom);
	}

	/**
	 * 修改备勤间
	 * 
	 * @param backUpRoom 备勤间信息
	 * @return 结果
	 */
	@Override
	public int updateBackUpRoom(BackUpRoom backUpRoom) {
		return backUpRoomMapper.updateBackUpRoom(backUpRoom);
	}

	/**
	 * 删除备勤间对象
	 * 
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deleteBackUpRoomByIds(String ids) {
		return backUpRoomMapper.deleteBackUpRoomByIds(Convert.toStrArray(ids));
	}

	/**
	 * 逻辑删除备勤间对象
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deleteBackUpRoomByIdsUpdateDelFlag(String ids) {
		return backUpRoomMapper.deleteBackUpRoomByIdsUpdateDelFlag(Convert.toStrArray(ids));
	}

	/**
	 * 校验房间号是否唯一
	 *
	 * @param
	 * @return 结果
	 */
	@Override
	public String checkRoomNumUnique(String roomNum) {
		int count = backUpRoomMapper.checkRoomNumUnique(roomNum);
		if (count > 0) {
			return "1";
		}
		return "0";
	}

	/**
	 * 从设备管理系统获取门锁列表
	 *
	 * @param 房间ID
	 * @return 结果
	 */
	@Override
	public List<DoorLockDeviceDto> listDoorLock(String selectedId) {
		Map<String, Object> paramMap = new HashMap<>();
		// 备勤间分组，对应设备管理子系统设备分组的ID
        paramMap.put("groupId", SysConfigUtil.getKey("webcard_deviceGroup_bqj"));
        paramMap.put("devType", SysConfigUtil.getKey("webcard_deviceType_bqj"));

        String url = "/api/device/getDeviceList";
		String responseData = WebcardApiUtil.sendPost(url, paramMap);
		List<DoorLockDeviceDto> listDoorLock = JSON.parseArray(responseData, DoorLockDeviceDto.class);
		for (DoorLockDeviceDto doorLockDeviceDto : listDoorLock) {
			if(doorLockDeviceDto.getId().equals(selectedId)) {
				doorLockDeviceDto.setFlag(true);
			}
		}
		return listDoorLock;
	}
}
