package com.mkst.umap.app.admin.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mkst.umap.app.mall.common.constant.MallConstants;
import com.mkst.umap.app.mall.common.entity.GoodsSpu;
import com.mkst.umap.app.mall.common.entity.OrderInfo;
import com.mkst.umap.app.mall.common.entity.OrderItem;
import com.mkst.umap.app.mall.common.enums.OrderInfoEnum;
import com.mkst.umap.app.mall.service.GoodsSpuService;
import com.mkst.umap.app.mall.service.OrderInfoService;
import com.mkst.umap.app.mall.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mkst.mini.systemplus.basic.domain.content.SmsMsgContent;
import com.mkst.mini.systemplus.basic.utils.MsgPushUtils;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.mini.systemplus.common.utils.StringUtils;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.mini.systemplus.util.SysConfigUtil;
import com.mkst.umap.app.admin.api.bean.param.SpendParam;
import com.mkst.umap.app.admin.api.bean.result.SpentStatisticsResult;
import com.mkst.umap.app.admin.domain.UserSpend;
import com.mkst.umap.app.admin.domain.UserSpendLog;
import com.mkst.umap.app.admin.dto.userspend.TransactionAmount;
import com.mkst.umap.app.admin.dto.userspend.UserSpendQrery;
import com.mkst.umap.app.admin.dto.userspend.UserTransactionAmount;
import com.mkst.umap.app.admin.mapper.UserSpendMapper;
import com.mkst.umap.app.admin.service.IUserSpendService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.base.core.util.UmapDateUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 我的消费 服务层实现
 * 
 * @author wangyong
 * @date 2020-11-03
 */
@Slf4j
@Service
public class UserSpendServiceImpl implements IUserSpendService 
{
	@Autowired
	private UserSpendMapper userSpendMapper;
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private OrderInfoService orderInfoService;
	@Autowired
	private GoodsSpuService goodsSpuService;
	@Autowired
	private OrderItemService orderItemService;

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
	@Transactional
	public AjaxResult importFromLog(List<UserSpendLog> logList) {

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
			UserSpend insertUserSpend = new UserSpend();
			insertUserSpend.setType(log.getTransactionType());
			insertUserSpend.setAmount(log.getTransactionAmount());
			insertUserSpend.setPayTime(log.getTransactionTime());
			insertUserSpend.setBalance(log.getBalance());
			insertUserSpend.setCreateTime(new Date());
			if(sysUser == null){
				failureNum++;
				failureMsg.append("<br/>" + failureNum + "：用户 [" + log.getUserName() + "]["+log.getPhonenumber()+"] 不存在");
				// 固定一个异常用户ID
				insertUserSpend.setUserId(999999l);
				insertUserSpend.setUserName(log.getUserId() + "-" + log.getUserName());
				insertUserSpend.setPhonenumber(log.getPhonenumber());
			}else {
				insertUserSpend.setUserId(sysUser.getUserId());
				insertUserSpend.setUserName(sysUser.getUserName());
				insertUserSpend.setPhonenumber(sysUser.getPhonenumber());
				// 记录每个用户最后的余额
				userMap.put(sysUser.getUserId().toString(), insertUserSpend);
			}

			userSpendMapper.insertUserSpend(insertUserSpend);
		}
		// 短信通知开关
		String remindSwitch = SysConfigUtil.getKey("umap_canteen_remind_switch");
		if("Y".equalsIgnoreCase(remindSwitch)) {
			// 多少余额短信提醒配置
			int minAmount = Integer.valueOf(SysConfigUtil.getKey("umap_canteen_remind_min_amount"));
			for (Entry<String, UserSpend> userSpend : userMap.entrySet()) {
				// 余额小于100，短信提醒通知充值
				if(userSpend.getValue().getBalance().intValue() < minAmount) {
					SmsMsgContent msgContent = new SmsMsgContent();
					msgContent.setTitle("食堂余额不足提醒");
					msgContent.setContent(String.format("您的食堂余额为%s元，为不影响您的正常就餐，请留意余额情况并在本月%s日及时向财务充值"
							, userSpend.getValue().getBalance().toString(), SysConfigUtil.getKey("umap_canteen_recharge_date")));
					MsgPushUtils.push(msgContent, userSpend.getValue().getId().toString(), "umap_backup_success", "[CODE]"+userSpend.getValue().getPhonenumber());
					MsgPushUtils.getMsgPushTask().execute();
				}
			}
		}

		if (failureNum > 0)
		{
			failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
			log.error(failureMsg.toString());
			return AjaxResult.error("导入失败！共 " + failureNum + " 条数据格式不正确，请联系管理员。");
		}
		else
		{
			successMsg.append("全部导入成功！共 " + logList.size() + " 条。");
		}
		return AjaxResult.success(successMsg.toString());
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
	public List<UserTransactionAmount> getAllUserBalance(UserSpendQrery userSpendQrery) {
		return userSpendMapper.getAllUserBalance(userSpendQrery);
	}
	/**
	 * 获取指定月份的交易流水总金额
	 */
	@Override
	public TransactionAmount getTotalTransactionAmount(String transactionMonth) {
		return userSpendMapper.getTotalTransactionAmount(transactionMonth);
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


	@Override
	@Transactional(rollbackFor = Exception.class)
	public void orderPayment(OrderInfo orderInfo) {

		UserSpend userLastBalance = userSpendMapper.getUserLastBalance(Long.valueOf(orderInfo.getUserId()));
		if (null == userLastBalance || userLastBalance.getBalance() == null) {
			throw  new RuntimeException("未查询到该订单的用户余额");
		}

		// 扣减用户余额并新增消费记录
		UserSpend insertUserSpend = new UserSpend();
		insertUserSpend.setType("2");
		insertUserSpend.setAmount(orderInfo.getPaymentPrice());
		insertUserSpend.setPayTime(new Date());
		insertUserSpend.setBalance(userLastBalance.getBalance().subtract(orderInfo.getPaymentPrice()));
		insertUserSpend.setCreateTime(new Date());
		insertUserSpend.setUserId(Long.valueOf(orderInfo.getUserId()));
		SysUser user = sysUserService.selectUserById(Long.valueOf(orderInfo.getUserId()));
		if (null != user) {
			insertUserSpend.setUserName(user.getUserName());
		}
		userSpendMapper.insertUserSpend(insertUserSpend);

		// 更新订单状态
		orderInfo.setPaymentTime(LocalDateTime.now());
		orderInfo.setIsPay(MallConstants.YES);
		orderInfo.setOrderStatus(MallConstants.ORDER_STATUS_1);
		orderInfo.setStatus(OrderInfoEnum.STATUS_1.getValue());
		orderInfo.setStatus(OrderInfoEnum.STATUS_1.getValue());
		// todo 生成取餐码
		orderInfo.setQueueNumber("5555");
		orderInfoService.updateById(orderInfo);

		List<OrderItem> listOrderItem = orderItemService.list(Wrappers.<OrderItem>lambdaQuery()
				.eq(OrderItem::getOrderId,orderInfo.getId()));

		Map<String, List<OrderItem>> resultList = listOrderItem.stream().collect(Collectors.groupingBy(OrderItem::getSpuId));
		List<GoodsSpu> listGoodsSpu = goodsSpuService.listByIds(resultList.keySet());

		listGoodsSpu.forEach(goodsSpu -> {
			resultList.get(goodsSpu.getId()).forEach(orderItem -> {
				//更新销量
				goodsSpu.setSaleNum(goodsSpu.getSaleNum()+orderItem.getQuantity());
			});
			goodsSpuService.updateById(goodsSpu);
		});
	}
}
