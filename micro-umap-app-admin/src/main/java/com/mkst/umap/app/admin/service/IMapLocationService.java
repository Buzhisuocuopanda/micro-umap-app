package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.domain.MapLocation;
import java.util.List;

/**
 * 地图位置 服务层
 * 
 * @author wangyong
 * @date 2020-07-20
 */
public interface IMapLocationService 
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
     * 删除地图位置信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMapLocationByIds(String ids);
	
}
