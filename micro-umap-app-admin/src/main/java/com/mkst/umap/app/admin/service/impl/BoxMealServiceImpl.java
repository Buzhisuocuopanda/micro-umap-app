package com.mkst.umap.app.admin.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.BoxMealMapper;
import com.mkst.umap.app.admin.domain.BoxMeal;
import com.mkst.umap.app.admin.service.IBoxMealService;
import com.mkst.mini.systemplus.common.support.Convert;

/**
 * 食堂包厢餐次 服务层实现
 * 
 * @author wangyong
 * @date 2020-08-31
 */
@Service
public class BoxMealServiceImpl implements IBoxMealService 
{
	@Autowired
	private BoxMealMapper boxMealMapper;

	/**
     * 查询食堂包厢餐次信息
     * 
     * @param id 食堂包厢餐次ID
     * @return 食堂包厢餐次信息
     */
    @Override
	public BoxMeal selectBoxMealById(Long id)
	{
	    return boxMealMapper.selectBoxMealById(id);
	}
	
	/**
     * 查询食堂包厢餐次列表
     * 
     * @param boxMeal 食堂包厢餐次信息
     * @return 食堂包厢餐次集合
     */
	@Override
	public List<BoxMeal> selectBoxMealList(BoxMeal boxMeal)
	{
	    return boxMealMapper.selectBoxMealList(boxMeal);
	}
	
    /**
     * 新增食堂包厢餐次
     * 
     * @param boxMeal 食堂包厢餐次信息
     * @return 结果
     */
	@Override
	public int insertBoxMeal(BoxMeal boxMeal)
	{
	    return boxMealMapper.insertBoxMeal(boxMeal);
	}
	
	/**
     * 修改食堂包厢餐次
     * 
     * @param boxMeal 食堂包厢餐次信息
     * @return 结果
     */
	@Override
	public int updateBoxMeal(BoxMeal boxMeal)
	{
	    return boxMealMapper.updateBoxMeal(boxMeal);
	}

	/**
     * 删除食堂包厢餐次对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteBoxMealByIds(String ids)
	{
		return boxMealMapper.deleteBoxMealByIds(Convert.toStrArray(ids));
	}
	
}
