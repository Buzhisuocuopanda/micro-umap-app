package com.mkst.umap.app.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.mini.systemplus.common.utils.StringUtils;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.umap.app.admin.api.bean.param.SpendParam;
import com.mkst.umap.app.admin.api.bean.result.SpentStatisticsResult;
import com.mkst.umap.app.admin.domain.UserSpend;
import com.mkst.umap.app.admin.domain.UserSpendLog;
import com.mkst.umap.app.admin.mapper.UserSpendMapper;
import com.mkst.umap.app.admin.service.IUserSpendService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.base.core.util.UmapDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 我的消费 服务层实现
 * 
 * @author wangyong
 * @date 2020-11-03
 */
@Service
public class UserSpendServiceImpl implements IUserSpendService 
{
	@Autowired
	private UserSpendMapper userSpendMapper;
	@Autowired
	private ISysUserService sysUserService;

	/**
     * 查询我的消费信息
     * 
     * @param id 我的消费ID
     * @return 我的消费信息
     */
    @Override
	public UserSpend selectUserSpendById(Long id)
	{
	    return userSpendMapper.selectUserSpendById(id);
	}
	
	/**
     * 查询我的消费列表
     * 
     * @param userSpend 我的消费信息
     * @return 我的消费集合
     */
	@Override
	public List<UserSpend> selectUserSpendList(UserSpend userSpend)
	{
	    return userSpendMapper.selectUserSpendList(userSpend);
	}
	
    /**
     * 新增我的消费
     * 
     * @param userSpend 我的消费信息
     * @return 结果
     */
	@Override
	public int insertUserSpend(UserSpend userSpend)
	{
	    return userSpendMapper.insertUserSpend(userSpend);
	}
	
	/**
     * 修改我的消费
     * 
     * @param userSpend 我的消费信息
     * @return 结果
     */
	@Override
	public int updateUserSpend(UserSpend userSpend)
	{
	    return userSpendMapper.updateUserSpend(userSpend);
	}

	/**
     * 删除我的消费对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteUserSpendByIds(String ids)
	{
		return userSpendMapper.deleteUserSpendByIds(Convert.toStrArray(ids));
	}

	/**
	 * @author wangyong
	 * @description 导入用户消费日志
	 * @date 14:27 2020-11-05
	 * @param logList 用户日志list
	 * @return java.lang.String
	 */
	@Override
	public String importFromLog(List<UserSpendLog> logList) {

		if (CollectionUtil.isEmpty(logList)){
			throw new RuntimeException("导入日志数据不能为空！");
		}
		//成功数量
		int successNum = 0;
		//失败数量
		int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();

		for (UserSpendLog log : logList){
			if(StringUtils.isBlank(log.getName())){
				continue;
			}
			SysUser sysUser = new SysUser();
			try {
				String userName = log.getName().replaceAll(" ", "");
				sysUser = sysUserService.selectUserByUserName(userName);
			}catch (Exception e){
				e.printStackTrace();
			}
			if(sysUser == null || StringUtils.isEmpty(sysUser.getLoginName())){
				failureNum++;
				failureMsg.append("<br/>" + failureNum + "、用户 " + log.getName() + " 不存在");
				continue;
			}

			UserSpend insertUserSpend = new UserSpend();
			insertUserSpend.setUserId(Integer.parseInt(String.valueOf(sysUser.getUserId())));
			insertUserSpend.setType("2");
			insertUserSpend.setSubType(checkSubType(log.getPayTime()));
			insertUserSpend.setAmount(log.getAmount());
			insertUserSpend.setBalance(log.getBalance());
			insertUserSpend.setPayTime(log.getPayTime());
			insertUserSpend.setCreateTime(new Date());

			userSpendMapper.insertUserSpend(insertUserSpend);
			successNum++;
			successMsg.append("<br/>" + successNum + "用户："  + log.getName()+" 日志导入成功");

		}

		if (failureNum > 0)
		{
			failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
			throw new RuntimeException(failureMsg.toString());
		}
		else
		{
			successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
		}
		return successMsg.toString();
	}

	private String checkSubType(Date payTime) {
		Date date1 = new Date(payTime.getTime());
		date1.setHours(10);
		date1.setMinutes(0);
		date1.setSeconds(0);
		Date date2 = new Date(payTime.getTime());
		date2.setHours(15);
		date2.setMinutes(0);
		date2.setSeconds(0);

		if (payTime.before(date1)){
			return "1";
		}else if (payTime.before(date2)){
			return "2";
		}
		return "3";
	}

	/**
	 * @author wangyong
	 * @description 获取用户约
	 * @date 14:28 2020-11-05
	 * @param userId 用户id
	 * @return java.math.BigDecimal
	 */
	@Override
	public BigDecimal getUserLastBalance(Integer userId) {
		UserSpend selectSpend = new UserSpend();
		selectSpend.setUserId(userId);
		return userSpendMapper.getUserLastBalance(userId);
	}

	@Override
	public List<SpentStatisticsResult> selectStatistics(SpendParam param) {

		List<SpentStatisticsResult> spentStatisticsResults = userSpendMapper.selectStatistics(param);
		if (KeyConstant.SEARCH_MODE_YEAR.equals(param.getWay())){
			return spentStatisticsResults;
		}

		ArrayList<SpentStatisticsResult> monthResult = new ArrayList<>();
		List<String> mList = UmapDateUtils.getLastMonthList(12, "MM", new Date());

		for (String m : mList){
			SpentStatisticsResult here = new SpentStatisticsResult();
			here.setKey(m);
			here.setValue(new BigDecimal(0));
			for (SpentStatisticsResult res : spentStatisticsResults){
				if (m.equals(res.getKey())){
					here.setValue(res.getValue());
					break;
				}
			}
			monthResult.add(here);
		}
		return monthResult;
	}
}
