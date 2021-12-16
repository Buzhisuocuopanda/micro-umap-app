package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.domain.MapLocation;
import java.util.List;	

/**
 * 地图位置 数据层
 * 
 * @author wangyong
 * @date 2020-07-20
 */
public interface MapLocationMapper 
{
	/**
     * 查询地图位置信息
     * 
     * @param locationId 地图位置ID
     * @return 地图位置信息
     */
	public MapLocation selectMapLocationById(Long locationId);
	
	/**
     * 查询地图位置列表
     * 
     * @param mapLocation 地图位置信息
     * @return 地图位置集合
     */
	public List<MapLocation> selectMapLocationList(MapLocation mapLocation);
	
	/**
     * 新增地图位置
     * 
     * @param mapLocation 地图位置信息
     * @return 结果
     */
	public int insertMapLocation(MapLocation mapLocation);
	
	/**
     * 修改地图位置
     * 
     * @param mapLocation 地图位置信息
     * @return 结果
     */
	public int updateMapLocation(MapLocation mapLocation);
	
	/**
     * 删除地图位置
     * 
     * @param locationId 地图位置ID
     * @return 结果
     */
	public int deleteMapLocationById(Long locationId);
	
	/**
     * 批量删除地图位置
     * 
     * @param locationIds 需要删除的数据ID
     * @return 结果
     */
	public int deleteMapLocationByIds(String[] locationIds);
	
}