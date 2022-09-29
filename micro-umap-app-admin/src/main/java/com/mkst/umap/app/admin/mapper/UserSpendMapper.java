package com.mkst.umap.app.admin.mapper;

import java.util.List;

import com.mkst.umap.app.admin.api.bean.param.SpendParam;
import com.mkst.umap.app.admin.api.bean.result.SpentStatisticsResult;
import com.mkst.umap.app.admin.domain.UserSpend;
import com.mkst.umap.app.admin.dto.userspend.TransactionAmount;
import com.mkst.umap.app.admin.dto.userspend.UserSpendQrery;

/**
 * 我的消费 数据层
 * 
 * @author wangyong
 * @date 2020-11-03
 */
public interface UserSpendMapper 
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
     * 删除我的消费
     * 
     * @param id 我的消费ID
     * @return 结果
     */
	public int deleteUserSpendById(Long id);
	
	/**
     * 批量删除我的消费
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteUserSpendByIds(String[] ids);

	/**
	 * @Author wangyong
	 * @Description 获取用户余额（写的不好，得改）
	 * @Date 11:41 2020-11-05
	 * @Param [userId]
	 * @return java.math.BigDecimal
	 */
	UserSpend getUserLastBalance(Long userId);
	/**
	 * 获取所有用户余额
	 */
	List<UserSpend> getAllUserBalance(UserSpendQrery userSpendQrery);
	/**
	 * 获取指定月份的交易流水总金额
	 */
	TransactionAmount getTotalTransactionAmount(String transactionMonth);

    /**
     * @Author wangyong
     * @Description 用户图标数据分析（写的不好，得改）
     * @Date 11:41 2020-11-05
     * @param param 参数
     * @return java.util.List<com.mkst.umap.app.admin.api.bean.result.SpentStatisticsResult>
     */
	List<SpentStatisticsResult> selectStatistics(SpendParam param);
}