package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.api.bean.param.SpendParam;
import com.mkst.umap.app.admin.api.bean.result.SpentStatisticsResult;
import com.mkst.umap.app.admin.domain.UserSpend;
import com.mkst.umap.app.admin.domain.UserSpendLog;

import java.math.BigDecimal;
import java.util.List;

/**
 * 我的消费 服务层
 * 
 * @author wangyong
 * @date 2020-11-03
 */
public interface IUserSpendService 
{
	/**
     * 查询我的消费信息
     * 
     * @param id 我的消费ID
     * @return 我的消费信息
     */
	public UserSpend selectUserSpendById(Long id);
	
	/**
     * 查询我的消费列表
     * 
     * @param userSpend 我的消费信息
     * @return 我的消费集合
     */
	public List<UserSpend> selectUserSpendList(UserSpend userSpend);
	
	/**
     * 新增我的消费
     * 
     * @param userSpend 我的消费信息
     * @return 结果
     */
	public int insertUserSpend(UserSpend userSpend);
	
	/**
     * 修改我的消费
     * 
     * @param userSpend 我的消费信息
     * @return 结果
     */
	public int updateUserSpend(UserSpend userSpend);
		
	/**
     * 删除我的消费信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteUserSpendByIds(String ids);

	public String importFromLog(List<UserSpendLog> logList);

    BigDecimal getUserLastBalance(Integer userId);

	List<SpentStatisticsResult> selectStatistics(SpendParam param);
}
