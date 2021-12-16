package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.domain.BoxMeal;
import java.util.List;	

/**
 * 食堂包厢餐次 数据层
 * 
 * @author wangyong
 * @date 2020-08-31
 */
public interface BoxMealMapper 
{
	/**
     * 查询食堂包厢餐次信息
     * 
     * @param id 食堂包厢餐次ID
     * @return 食堂包厢餐次信息
     */
	public BoxMeal selectBoxMealById(Long id);
	
	/**
     * 查询食堂包厢餐次列表
     * 
     * @param boxMeal 食堂包厢餐次信息
     * @return 食堂包厢餐次集合
     */
	public List<BoxMeal> selectBoxMealList(BoxMeal boxMeal);
	
	/**
     * 新增食堂包厢餐次
     * 
     * @param boxMeal 食堂包厢餐次信息
     * @return 结果
     */
	public int insertBoxMeal(BoxMeal boxMeal);
	
	/**
     * 修改食堂包厢餐次
     * 
     * @param boxMeal 食堂包厢餐次信息
     * @return 结果
     */
	public int updateBoxMeal(BoxMeal boxMeal);
	
	/**
     * 删除食堂包厢餐次
     * 
     * @param id 食堂包厢餐次ID
     * @return 结果
     */
	public int deleteBoxMealById(Long id);
	
	/**
     * 批量删除食堂包厢餐次
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteBoxMealByIds(String[] ids);
	
}