package com.mkst.umap.app.admin.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.CarInfoMapper;
import com.mkst.umap.app.admin.domain.CarInfo;
import com.mkst.umap.app.admin.service.ICarInfoService;
import com.mkst.mini.systemplus.common.support.Convert;

/**
 * 车辆 服务层实现
 * 
 * @author wangyong
 * @date 2020-07-20
 */
@Service
public class CarInfoServiceImpl implements ICarInfoService 
{
	@Autowired
	private CarInfoMapper carInfoMapper;

	/**
     * 查询车辆信息
     * 
     * @param carId 车辆ID
     * @return 车辆信息
     */
    @Override
	public CarInfo selectCarInfoById(Long carId)
	{
	    return carInfoMapper.selectCarInfoById(carId);
	}

	@Override
	public List<CarInfo> selectCarInfoByIds(List<Long> ids){
    	return carInfoMapper.selectCarInfoByIds(ids);
	}
	
	/**
     * 查询车辆列表
     * 
     * @param carInfo 车辆信息
     * @return 车辆集合
     */
	@Override
	public List<CarInfo> selectCarInfoList(CarInfo carInfo)
	{
	    return carInfoMapper.selectCarInfoList(carInfo);
	}
	
    /**
     * 新增车辆
     * 
     * @param carInfo 车辆信息
     * @return 结果
     */
	@Override
	public int insertCarInfo(CarInfo carInfo)
	{
	    return carInfoMapper.insertCarInfo(carInfo);
	}
	
	/**
     * 修改车辆
     * 
     * @param carInfo 车辆信息
     * @return 结果
     */
	@Override
	public int updateCarInfo(CarInfo carInfo)
	{
	    return carInfoMapper.updateCarInfo(carInfo);
	}

	/**
     * 删除车辆对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteCarInfoByIds(String ids)
	{
		return carInfoMapper.deleteCarInfoByIds(Convert.toStrArray(ids));
	}
	
}
