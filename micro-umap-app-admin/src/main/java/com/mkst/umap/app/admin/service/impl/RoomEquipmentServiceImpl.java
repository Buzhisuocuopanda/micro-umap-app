package com.mkst.umap.app.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.mini.systemplus.system.service.ISysConfigService;
import com.mkst.umap.app.admin.domain.Equipment;
import com.mkst.umap.app.admin.domain.RoomEquipment;
import com.mkst.umap.app.admin.mapper.RoomEquipmentMapper;
import com.mkst.umap.app.admin.service.IRoomEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 提审室设配 服务层实现
 *
 * @author wangyong
 * @date 2020-09-08
 */
@Service
public class RoomEquipmentServiceImpl implements IRoomEquipmentService {

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private RoomEquipmentMapper roomEquipmentMapper;

	/**
	 * 查询提审室设配信息
	 *
	 * @param id 提审室设配ID
	 * @return 提审室设配信息
	 */
	@Override
	public RoomEquipment selectRoomEquipmentById(Long id) {
		return roomEquipmentMapper.selectRoomEquipmentById(id);
	}

	/**
	 * 查询提审室设配列表
	 *
	 * @param roomEquipment 提审室设配信息
	 * @return 提审室设配集合
	 */
	@Override
	public List<RoomEquipment> selectRoomEquipmentList(RoomEquipment roomEquipment) {
		return roomEquipmentMapper.selectRoomEquipmentList(roomEquipment);
	}

	@Override
	public List<Equipment> selectRemoteEquipmentList(Equipment equipment) {

		Map<String, Object> selectEquipmentMap = BeanUtil.beanToMap(equipment);

		String httpUrl = configService.selectConfigByKey("webcard_url");
		String appId = configService.selectConfigByKey("webcard_appid");
		String secret = configService.selectConfigByKey("webcard_secret");
		String url = httpUrl + "/api/device/getDeviceList";
		long t = System.currentTimeMillis();
		StringBuilder query = new StringBuilder();
		query.append("appId").append(appId);
		query.append("t").append(t);
		query.append(secret);
		String sign = SecureUtil.md5(query.toString());
		url += "?appId=" + appId + "&t=" + t + "&sign=" + sign;
		String responseStr = HttpUtil.createPost(url).body(JSON.toJSONString(selectEquipmentMap), "application/json").execute().body();
		String oldData = JSON.parseObject(responseStr).getString("data");

		List<Equipment> equipmentList = JSON.parseArray(oldData, Equipment.class);

		return equipmentList;
	}

	@Override
	public List<Equipment> selectRemoteEquipmentListByType(String type) {

		String httpUrl = configService.selectConfigByKey("webcard_url");
		String appId = configService.selectConfigByKey("webcard_appid");
		String secret = configService.selectConfigByKey("webcard_secret");

		Equipment equipment = new Equipment();
		equipment.setDevType(type);
		Map<String, Object> selectEquipmentMap = BeanUtil.beanToMap(equipment);

		String url = httpUrl + "/api/device/getDeviceList";
		long t = System.currentTimeMillis();
		StringBuilder query = new StringBuilder();
		query.append("appId").append(appId);
		query.append("t").append(t);
		query.append(secret);
		String sign = SecureUtil.md5(query.toString());
		url += "?appId=" + appId + "&t=" + t + "&sign=" + sign;
		String responseStr = HttpUtil.createPost(url).body(JSON.toJSONString(selectEquipmentMap), "application/json").execute().body();
		String oldData = JSON.parseObject(responseStr).getString("data");

		List<Equipment> equipmentList = JSON.parseArray(oldData, Equipment.class);

		return equipmentList;
	}


	@Override
	public List<Equipment> selectRoomBindEquipmentList(RoomEquipment selectRoomEquipment) {

		ArrayList<Equipment> result = new ArrayList<>();

		List<RoomEquipment> equipmentList = this.selectRoomEquipmentList(selectRoomEquipment);

		equipmentList.stream().forEach(equip -> {
			Equipment selectRemoteEquipment = new Equipment();
			selectRemoteEquipment.setId(equip.getEquipmentId());
			List<Equipment> remoteEquipmentList = this.selectRemoteEquipmentList(selectRemoteEquipment);
			if (CollectionUtil.isNotEmpty(remoteEquipmentList)) {
				result.add(remoteEquipmentList.get(0));
			}
		});

		return result;
	}

	@Override
	public List<Equipment> selectRoomNotBindEquipmentList() {

		ArrayList<Equipment> result = new ArrayList<>();
		Equipment selectRemoteEquipment = new Equipment();
		selectRemoteEquipment.setGroupId("125");
		List<Equipment> remoteEquipmentList = this.selectRemoteEquipmentList(selectRemoteEquipment);
		RoomEquipment selectRoomEquipment = new RoomEquipment();
		remoteEquipmentList.stream().forEach(remoteEquip -> {
			selectRoomEquipment.setEquipmentId(remoteEquip.getId());
			List<RoomEquipment> roomEquipments = this.selectRoomEquipmentList(selectRoomEquipment);
			if (CollectionUtil.isEmpty(roomEquipments)) {
				result.add(remoteEquip);
			}
		});

		return result;
	}

	/**
	 * 新增提审室设配
	 *
	 * @param roomEquipment 提审室设配信息
	 * @return 结果
	 */
	@Override
	public int insertRoomEquipment(RoomEquipment roomEquipment) {
		return roomEquipmentMapper.insertRoomEquipment(roomEquipment);
	}

	/**
	 * 修改提审室设配
	 *
	 * @param roomEquipment 提审室设配信息
	 * @return 结果
	 */
	@Override
	public int updateRoomEquipment(RoomEquipment roomEquipment) {
		return roomEquipmentMapper.updateRoomEquipment(roomEquipment);
	}

	/**
	 * 删除提审室设配对象
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deleteRoomEquipmentByIds(String ids) {
		return roomEquipmentMapper.deleteRoomEquipmentByIds(Convert.toStrArray(ids));
	}

	/**
	 * 删除提审室设配对象
	 *
	 * @param roomId 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deleteRoomEquipmentByRoomId(String roomId) {
		return roomEquipmentMapper.deleteRoomEquipmentByRoomId(roomId);
	}

}
