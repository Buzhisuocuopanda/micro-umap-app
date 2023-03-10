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
 * ???????????? ???????????????
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
     * ????????????????????????
     * 
     * @param id ????????????ID
     * @return ??????????????????
     */
    @Override
	public UserSpend selectUserSpendById(Long id)
	{
	    return userSpendMapper.selectUserSpendById(id);
	}
	
	/**
     * ????????????????????????
     * 
     * @param userSpend ??????????????????
     * @return ??????????????????
     */
	@Override
	public List<UserSpend> selectUserSpendList(UserSpend userSpend)
	{
	    return userSpendMapper.selectUserSpendList(userSpend);
	}
	
    /**
     * ??????????????????
     * 
     * @param userSpend ??????????????????
     * @return ??????
     */
	@Override
	public int insertUserSpend(UserSpend userSpend)
	{
	    return userSpendMapper.insertUserSpend(userSpend);
	}
	
	/**
     * ??????????????????
     * 
     * @param userSpend ??????????????????
     * @return ??????
     */
	@Override
	public int updateUserSpend(UserSpend userSpend)
	{
	    return userSpendMapper.updateUserSpend(userSpend);
	}

	/**
     * ????????????????????????
     * 
     * @param ids ?????????????????????ID
     * @return ??????
     */
	@Override
	public int deleteUserSpendByIds(String ids)
	{
		return userSpendMapper.deleteUserSpendByIds(Convert.toStrArray(ids));
	}

	/**
	 * @author wangyong
	 * @description ????????????????????????
	 * @date 14:27 2020-11-05
	 * @param logList ????????????list
	 * @return java.lang.String
	 */
	@Override
	@Transactional
	public AjaxResult importFromLog(List<UserSpendLog> logList) {

		if (CollectionUtil.isEmpty(logList)){
			throw new RuntimeException("?????????????????????????????????");
		}
		//????????????
//		int successNum = 0;
		//????????????
		int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();

		// ????????????????????????
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
				failureMsg.append("<br/>" + failureNum + "????????? [" + log.getUserName() + "]["+log.getPhonenumber()+"] ?????????");
				// ????????????????????????ID
				insertUserSpend.setUserId(999999l);
				insertUserSpend.setUserName(log.getUserId() + "-" + log.getUserName());
				insertUserSpend.setPhonenumber(log.getPhonenumber());
			}else {
				insertUserSpend.setUserId(sysUser.getUserId());
				insertUserSpend.setUserName(sysUser.getUserName());
				insertUserSpend.setPhonenumber(sysUser.getPhonenumber());
				// ?????????????????????????????????
				userMap.put(sysUser.getUserId().toString(), insertUserSpend);
			}

			userSpendMapper.insertUserSpend(insertUserSpend);
		}
		// ??????????????????
		String remindSwitch = SysConfigUtil.getKey("umap_canteen_remind_switch");
		if("Y".equalsIgnoreCase(remindSwitch)) {
			// ??????????????????????????????
			int minAmount = Integer.valueOf(SysConfigUtil.getKey("umap_canteen_remind_min_amount"));
			for (Entry<String, UserSpend> userSpend : userMap.entrySet()) {
				// ????????????100???????????????????????????
				if(userSpend.getValue().getBalance().intValue() < minAmount) {
					SmsMsgContent msgContent = new SmsMsgContent();
					msgContent.setTitle("????????????????????????");
					msgContent.setContent(String.format("?????????????????????%s????????????????????????????????????????????????????????????????????????%s????????????????????????"
							, userSpend.getValue().getBalance().toString(), SysConfigUtil.getKey("umap_canteen_recharge_date")));
					MsgPushUtils.push(msgContent, userSpend.getValue().getId().toString(), "umap_backup_success", "[CODE]"+userSpend.getValue().getPhonenumber());
					MsgPushUtils.getMsgPushTask().execute();
				}
			}
		}

		if (failureNum > 0)
		{
			failureMsg.insert(0, "?????????????????????????????? " + failureNum + " ??????????????????????????????????????????");
			log.error(failureMsg.toString());
			return AjaxResult.error("?????????????????? " + failureNum + " ????????????????????????????????????????????????");
		}
		else
		{
			successMsg.append("???????????????????????? " + logList.size() + " ??????");
		}
		return AjaxResult.success(successMsg.toString());
	}
	
	/**
	 * @author wangyong
	 * @description ???????????????
	 * @date 14:28 2020-11-05
	 * @param userId ??????id
	 * @return java.math.BigDecimal
	 */
	@Override
	public UserSpend getUserLastBalance(Long userId) {
		UserSpend selectSpend = new UserSpend();
		selectSpend.setUserId(userId);
		return userSpendMapper.getUserLastBalance(userId);
	}
	
	/**
	 * ????????????????????????
	 */
	@Override
	public List<UserTransactionAmount> getAllUserBalance(UserSpendQrery userSpendQrery) {
		return userSpendMapper.getAllUserBalance(userSpendQrery);
	}
	/**
	 * ??????????????????????????????????????????
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
			throw  new RuntimeException("????????????????????????????????????");
		}

		// ???????????????????????????????????????
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

		// ??????????????????
		orderInfo.setPaymentTime(LocalDateTime.now());
		orderInfo.setIsPay(MallConstants.YES);
		orderInfo.setOrderStatus(MallConstants.ORDER_STATUS_0);
		orderInfo.setStatus(OrderInfoEnum.STATUS_1.getValue());
		orderInfo.setStatus(OrderInfoEnum.STATUS_1.getValue());
		// todo ???????????????
		orderInfo.setQueueNumber("5555");
		orderInfoService.updateById(orderInfo);

		List<OrderItem> listOrderItem = orderItemService.list(Wrappers.<OrderItem>lambdaQuery()
				.eq(OrderItem::getOrderId,orderInfo.getId()));

		Map<String, List<OrderItem>> resultList = listOrderItem.stream().collect(Collectors.groupingBy(OrderItem::getSpuId));
		List<GoodsSpu> listGoodsSpu = goodsSpuService.listByIds(resultList.keySet());

		listGoodsSpu.forEach(goodsSpu -> {
			resultList.get(goodsSpu.getId()).forEach(orderItem -> {
				//????????????
				goodsSpu.setSaleNum(goodsSpu.getSaleNum()+orderItem.getQuantity());
			});
			goodsSpuService.updateById(goodsSpu);
		});
	}
}
