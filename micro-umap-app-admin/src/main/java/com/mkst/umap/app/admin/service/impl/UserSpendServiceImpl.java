package com.mkst.umap.app.admin.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mkst.mini.systemplus.basic.domain.content.SmsMsgContent;
import com.mkst.mini.systemplus.basic.utils.MsgPushUtils;
import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.mini.systemplus.common.utils.StringUtils;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.mini.systemplus.util.SysConfigUtil;
import com.mkst.umap.app.admin.api.bean.param.SpendParam;
import com.mkst.umap.app.admin.api.bean.result.SpentStatisticsResult;
import com.mkst.umap.app.admin.domain.UserSpend;
import com.mkst.umap.app.admin.domain.UserSpendLog;
import com.mkst.umap.app.admin.mapper.UserSpendMapper;
import com.mkst.umap.app.admin.service.IUserSpendService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.base.core.util.UmapDateUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;

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
//		int successNum = 0;
		//失败数量
		int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();

		// 缓存所有用户列表
		Map<String, SysUser> mobileMap = new HashMap<String, SysUser>();
		Map<String, SysUser> nameMap = new HashMap<String, SysUser>();
		Map<String, UserSpend> userMap = new HashMap<String, UserSpend>();
		List<SysUser> userList = sysUserService.selectUserList(new SysUser());
		for (SysUser sysUser : userList) {
			mobileMap.put(sysUser.getPhonenumber(), sysUser);
			nameMap.put(sysUser.getUserName(), sysUser);
		}
		for (UserSpendLog log : logList){
			if(StringUtils.isBlank(log.getUserName())){
				continue;
			}
			SysUser sysUser = mobileMap.get(log.getPhonenumber());
			if(sysUser == null) {
				sysUser = nameMap.get(log.getUserName());
			}
			if(sysUser == null || StringUtils.isEmpty(sysUser.getLoginName())){
				failureNum++;
				failureMsg.append("<br/>" + failureNum + "：用户 [" + log.getUserName() + "]["+log.getPhonenumber()+"] 不存在");
				continue;
			}

			UserSpend insertUserSpend = new UserSpend();
			insertUserSpend.setUserId(sysUser.getUserId());
			insertUserSpend.setUserName(sysUser.getUserName());
			insertUserSpend.setType(log.getTransactionType());
			insertUserSpend.setAmount(log.getTransactionAmount());
			insertUserSpend.setPayTime(log.getTransactionTime());
			insertUserSpend.setBalance(log.getBalance());
			insertUserSpend.setCreateTime(new Date());
			insertUserSpend.setPhonenumber(sysUser.getPhonenumber());

			userSpendMapper.insertUserSpend(insertUserSpend);
			
			// 记录每个用户最后的余额
			userMap.put(sysUser.getUserId().toString(), insertUserSpend);
		}
		// 多少余额短信提醒配置
		int minAmount = Integer.valueOf(SysConfigUtil.getKey("umap_canteen_remind_min_amount"));
		for (Entry<String, UserSpend> userSpend : userMap.entrySet()) {
			// 余额小于100，短信提醒通知充值
			if(userSpend.getValue().getBalance().intValue() < minAmount) {
				SmsMsgContent msgContent = new SmsMsgContent();
				msgContent.setTitle("食堂余额不足提醒");
				msgContent.setContent(StrUtil.format("您的食堂余额为%s元，为不影响您的正常就餐，请留意余额情况并在本月%s日及时向财务充值"
						, userSpend.getValue().getBalance().toString(), SysConfigUtil.getKey("umap_canteen_recharge_date")));
				MsgPushUtils.push(msgContent, userSpend.getValue().getId().toString(), "umap_backup_success", "[CODE]"+userSpend.getValue().getPhonenumber());
				MsgPushUtils.getMsgPushTask().execute();
			}
		}

		if (failureNum > 0)
		{
			failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
			throw new RuntimeException(failureMsg.toString());
		}
		else
		{
			successMsg.append("全部导入成功！共 " + logList.size() + " 条。");
		}
		return successMsg.toString();
	}
	
	/**
	 * @author wangyong
	 * @description 获取用户约
	 * @date 14:28 2020-11-05
	 * @param userId 用户id
	 * @return java.math.BigDecimal
	 */
	@Override
	public UserSpend getUserLastBalance(Long userId) {
		UserSpend selectSpend = new UserSpend();
		selectSpend.setUserId(userId);
		return userSpendMapper.getUserLastBalance(userId);
	}
	
	/**
	 * 获取所有用户余额
	 */
	@Override
	public List<UserSpend> getAllUserBalance() {
		return userSpendMapper.getAllUserBalance();
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
