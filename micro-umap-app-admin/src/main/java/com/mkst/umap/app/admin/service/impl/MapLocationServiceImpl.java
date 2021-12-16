package com.mkst.umap.app.admin.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.MapLocationMapper;
import com.mkst.umap.app.admin.domain.MapLocation;
import com.mkst.umap.app.admin.service.IMapLocationService;
import com.mkst.mini.systemplus.common.support.Convert;

/**
 * 地图位置 服务层实现
 * 
 * @author wangyong
 * @date 2020-07-20
 */
@Service
public class MapLocationServiceImpl implements IMapLocationService 
{
	@Autowired
	private MapLocationMapper mapLocationMapper;

	/**
     * 查询地图位置信息
     * 
     * @param locationId 地图位置ID
     * @return 地图位置信息
     */
    @Override
	public MapLocation selectMapLocationById(Long locationId)
	{
	    return mapLocationMapper.selectMapLocationById(locationId);
	}
	
	/**
     * 查询地图位置列表
     * 
     * @param mapLocation 地图位置信息
     * @return 地图位置集合
     */
	@Override
	public List<MapLocation> selectMapLocationList(MapLocation mapLocation)
	{
	    return mapLocationMapper.selectMapLocationList(mapLocation);
	}
	
    /**
     * 新增地图位置
     * 
     * @param mapLocation 地图位置信息
     * @return 结果
     */
	@Override
	public int insertMapLocation(MapLocation mapLocation)
	{
	    return mapLocationMapper.insertMapLocation(mapLocation);
	}
	
	/**
     * 修改地图位置
     * 
     * @param mapLocation 地图位置信息
     * @return 结果
     */
	@Override
	public int updateMapLocation(MapLocation mapLocation)
	{
	    return mapLocationMapper.updateMapLocation(mapLocation);
	}

	/**
     * 删除地图位置对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteMapLocationByIds(String ids)
	{
		return mapLocationMapper.deleteMapLocationByIds(Convert.toStrArray(ids));
	}
	
}
