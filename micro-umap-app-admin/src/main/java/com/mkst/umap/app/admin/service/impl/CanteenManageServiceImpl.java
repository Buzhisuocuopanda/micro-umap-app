package com.mkst.umap.app.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.mkst.mini.systemplus.common.shiro.utils.ShiroUtils;
import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.mini.systemplus.system.domain.SysRole;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.mapper.SysRoleMapper;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.mini.systemplus.workflow.domain.EventAuditRecord;
import com.mkst.mini.systemplus.workflow.domain.WfEventDetail;
import com.mkst.mini.systemplus.workflow.service.IEventAuditRecordService;
import com.mkst.mini.systemplus.workflow.service.IWfEventDetailService;
import com.mkst.umap.app.admin.api.bean.param.ApproveParam;
import com.mkst.umap.app.admin.api.bean.param.canteen.CanteenManageParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.api.bean.result.canteen.CanteenDetailResult;
import com.mkst.umap.app.admin.api.bean.result.canteen.CanteenResult;
import com.mkst.umap.app.admin.api.bean.result.canteen.MealTypeCountResult;
import com.mkst.umap.app.admin.domain.ArraignRoom;
import com.mkst.umap.app.admin.domain.AuditRecord;
import com.mkst.umap.app.admin.domain.BoxMeal;
import com.mkst.umap.app.admin.domain.CanteenManage;
import com.mkst.umap.app.admin.mapper.AuditRecordMapper;
import com.mkst.umap.app.admin.mapper.CanteenManageMapper;
import com.mkst.umap.app.admin.service.IArraignRoomService;
import com.mkst.umap.app.admin.service.IAuditRecordService;
import com.mkst.umap.app.admin.service.IBoxMealService;
import com.mkst.umap.app.admin.service.ICanteenManageService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.enums.ApproveStatusEnum;
import com.mkst.umap.app.common.enums.AuditRecordTypeEnum;
import com.mkst.umap.app.common.enums.AuditStatusEnum;
import com.mkst.umap.app.common.enums.RoleKeyEnum;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ???????????? ???????????????
 * 
 * @author wangyong
 * @date 2020-07-20
 */
@Service
public class CanteenManageServiceImpl implements ICanteenManageService 
{
	@Autowired
	private CanteenManageMapper canteenManageMapper;
	@Autowired
	private AuditRecordMapper auditRecordMapper;
	@Autowired
	private SysRoleMapper sysRoleMapper;
	@Autowired
	private IAuditRecordService auditRecordService;
	@Autowired
	private IWfEventDetailService eventDetailService;
	@Autowired
	private IEventAuditRecordService eventAuditRecordService;
	@Autowired
	private ISysUserService userService;
	@Autowired
	private IBoxMealService boxMealService;
	@Autowired
	private IArraignRoomService roomService;

	/**
     * ????????????????????????
     * 
     * @param canteenApplyId ????????????ID
     * @return ??????????????????
     */
    @Override
	public CanteenManage selectCanteenManageById(Long canteenApplyId)
	{
	    return canteenManageMapper.selectCanteenManageById(canteenApplyId);
	}

	@Override
	public CanteenDetailResult selectCanteenManageAllById(Long canteenApplyId){
    	return canteenManageMapper.selectCanteenManageAllById(canteenApplyId);
	}
	/**
     * ????????????????????????
     * 
     * @param canteenManage ??????????????????
     * @return ??????????????????
     */
	@Override
	public List<CanteenManage> selectCanteenManageList(CanteenManage canteenManage)
	{
	    return canteenManageMapper.selectCanteenManageList(canteenManage);
	}

	@Override
	public List<CanteenManage> selectCanteenListByBoxId(Map<String, Object> map){
		return canteenManageMapper.selectCanteenListByBoxId(map);
	}

	@Override
	public List<CanteenResult> selectCanteenResultByDate(CanteenManage canteenManage){
		return canteenManageMapper.selectCanteenResultByDate(canteenManage);
	}

	@Override
	public List<MealTypeCountResult> countByMealType(CanteenManageParam canteenManageParam) {
		return canteenManageMapper.countByMealType(canteenManageParam);
	}

	@Override
	public List<CanteenResult> selectCanteenResultByStatus(CanteenManage canteenManage){
		return canteenManageMapper.selectCanteenResultByStatus(canteenManage);
	}

	@Override
	public List<CanteenResult> selectCanteenResultByCancel(CanteenManage canteenManage){
		return canteenManageMapper.selectCanteenResultByCancel(canteenManage);
	}

	@Override
	public Integer selectCanteenManageCountByDateAndStatusEq01(String date){
		return canteenManageMapper.selectCanteenManageCountByDateAndStatusEq01(date);
	}
	
    /**
     * ??????????????????
     * 
     * @param canteenManage ??????????????????
     * @return ??????
     */
	@Override
	public int insertCanteenManage(CanteenManage canteenManage)
	{
	    return canteenManageMapper.insertCanteenManage(canteenManage);
	}
	
	/**
     * ??????????????????
     * 
     * @param canteenManage ??????????????????
     * @return ??????
     */
	@Override
	public int updateCanteenManage(CanteenManage canteenManage)
	{
	    return canteenManageMapper.updateCanteenManage(canteenManage);
	}

	@Override
	public String auditUpdateSuccess(SysUser sysUser, Long applyId){
		int row;
		CanteenManage canteenManage = selectCanteenManageById(applyId);
		if(canteenManage == null) {
			return "??????";
		}
		AuditRecord auditRecord = new AuditRecord();
		auditRecord.setApplyId(applyId);
		String msg = "";

		if(!sysUser.isAdmin()){
			//???????????????
			SysRole logisticsSysRole = getSysRole(RoleKeyEnum.ROLE_LOGISTICS_ADMIN.getValue());
			//???????????????
			SysRole kitchenSysRole = getSysRole(RoleKeyEnum.ROLE_KITCHEN_ADMIN.getValue());

			List<SysRole> lRoles = sysUser.getRoles().stream().filter(o -> o.getRoleId().equals(logisticsSysRole.getRoleId())).collect(Collectors.toList());
			if(lRoles.size() > 0){
				auditRecord.setApplyType(AuditRecordTypeEnum.CanteenLogisticsAudit.getValue());
				List<AuditRecord> list = auditRecordService.selectAuditRecordList(auditRecord);
				//????????????????????? ???????????????   ???????????? ?????????????????????????????????
				if(CollUtil.isEmpty(list)){
					canteenManage.setApplyStatus(ApproveStatusEnum.LOGISTICS.getValue());
					row = this.updateCanteenManage(canteenManage);
					if(row<=0){
						msg = msg+"????????????????????????   ";
					}
					AuditRecord a = new AuditRecord();
					a.setApplyId(canteenManage.getCanteenApplyId());
					a.setApplyType(AuditRecordTypeEnum.CanteenLogisticsAudit.getValue());
					a.setStatus(ApproveStatusEnum.SUCCESS.getValue() +"");
					a.setCreateBy(sysUser.getLoginName());
					a.setCreateTime(new Date());
					a.setUpdateBy(sysUser.getLoginName());
					auditRecordService.insertAuditRecord(a);
					msg = msg + "??????????????????  ";
				}else {
					msg = msg + "???????????????  ";
				}
			}

			List<SysRole> kRoles = sysUser.getRoles().stream().filter(o -> o.getRoleId().equals(kitchenSysRole.getRoleId())).collect(Collectors.toList());
			if(kRoles.size()>0){
				auditRecord.setApplyType(AuditRecordTypeEnum.CanteenKitchenAudit.getValue());
				List<AuditRecord> list = auditRecordService.selectAuditRecordList(auditRecord);
				if(CollUtil.isEmpty(list)){
					canteenManage.setApplyStatus(ApproveStatusEnum.KITCHEN.getValue());
					row = this.updateCanteenManage(canteenManage);
					if(row<=0){
						msg = msg+"   ????????????????????????";
					}
					AuditRecord a = new AuditRecord();
					a.setApplyId(canteenManage.getCanteenApplyId());
					a.setApplyType(AuditRecordTypeEnum.CanteenKitchenAudit.getValue());
					a.setStatus(ApproveStatusEnum.SUCCESS.getValue() +"");
					a.setCreateBy(sysUser.getLoginName());
					a.setCreateTime(new Date());
					a.setUpdateBy(sysUser.getLoginName());
					auditRecordService.insertAuditRecord(a);
					msg = msg + "  ??????????????????  ";
				}else {
					msg = msg + "   ???????????????";
				}
			}

			AuditRecord select = new AuditRecord();
			select.setApplyId(applyId);
			select.setStatus(ApproveStatusEnum.SUCCESS.getValue() +"");
			//?????????????????????????????????  ????????????????????????id?????????????????????  ?????????????????????????????????????????? ????????????  ????????????????????????
			List<AuditRecord> list = auditRecordService.selectAuditRecordList(select);

			List<AuditRecord> kitchenList = list.stream().filter(o -> AuditRecordTypeEnum.CanteenKitchenAudit.getValue().equals(o.getApplyType())).collect(Collectors.toList());
			List<AuditRecord> logisticsList = list.stream().filter(o -> AuditRecordTypeEnum.CanteenLogisticsAudit.getValue().equals(o.getApplyType())).collect(Collectors.toList());
			if(kitchenList.size() >0 && logisticsList.size()>0){
				canteenManage.setApplyStatus(ApproveStatusEnum.SUCCESS.getValue());
				this.updateCanteenManage(canteenManage);
				msg = "???????????????????????????,????????????";
			}

			return msg;
		}else{
			canteenManage.setApplyStatus(ApproveStatusEnum.SUCCESS.getValue());
			row = this.updateCanteenManage(canteenManage);
			if(row<=0){
				msg = msg+"   ????????????";
			}else{
				msg = "???????????????????????????,????????????";
			}
			AuditRecord a = new AuditRecord();
			a.setApplyId(canteenManage.getCanteenApplyId());
			a.setApplyType(AuditRecordTypeEnum.CanteenKitchenAudit.getValue());
			a.setStatus(ApproveStatusEnum.SUCCESS.getValue() +"");
			a.setCreateBy(sysUser.getLoginName());
			a.setCreateTime(new Date());
			a.setUpdateBy(sysUser.getLoginName());
			auditRecordService.insertAuditRecord(a);

			AuditRecord b = new AuditRecord();
			b.setApplyId(canteenManage.getCanteenApplyId());
			b.setApplyType(AuditRecordTypeEnum.CanteenLogisticsAudit.getValue());
			b.setStatus(ApproveStatusEnum.SUCCESS.getValue() +"");
			b.setCreateBy(sysUser.getLoginName());
			b.setCreateTime(new Date());
			b.setUpdateBy(sysUser.getLoginName());
			auditRecordService.insertAuditRecord(b);
			return msg;
		}

	}

	/**
     * ????????????????????????
     * 
     * @param ids ?????????????????????ID
     * @return ??????
     */
	@Override
	public int deleteCanteenManageByIds(String ids)
	{
		return canteenManageMapper.deleteCanteenManageByIds(Convert.toStrArray(ids));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String audit(Long id, String status) {

		CanteenManage canteenManage = selectCanteenManageById(id);

		/*if (!ApproveStatusEnum.Pending.getValue().equals(canteenManage.getApplyStatus()) ) {
			return 0;
		}*/

		SysUser sysUser = ShiroUtils.getSysUser();
		//???????????????
		SysRole logisticsSysRole = getSysRole(RoleKeyEnum.ROLE_LOGISTICS_ADMIN.getValue());
		List<SysRole> lRoles = sysUser.getRoles().stream().filter(o -> o.getRoleId().equals(logisticsSysRole.getRoleId())).collect(Collectors.toList());

		//???????????????
		SysRole kitchenSysRole = getSysRole(RoleKeyEnum.ROLE_KITCHEN_ADMIN.getValue());
		List<SysRole> kRoles = sysUser.getRoles().stream().filter(o -> o.getRoleId().equals(kitchenSysRole.getRoleId())).collect(Collectors.toList());

		if(!sysUser.isAdmin()){
			if(lRoles.size()<=0 && kRoles.size()<=0){
				return "??????????????????";
			}
		}

		String msg = "";
		if(AuditStatusEnum.EVENT_AUDIT_STATUS_PASS.getValue().toString().equals(status)){
			msg = auditUpdateSuccess(sysUser,id);
		}else if(AuditStatusEnum.EVENT_AUDIT_STATUS_FAIL.getValue().toString().equals(status)){
			canteenManage.setApplyStatus(ApproveStatusEnum.FAIL.getValue());
			int row = this.updateCanteenManage(canteenManage);
			if(row <=0){
				msg = "????????????";
			}
			//???????????????
			if(CollUtil.isEmpty(lRoles)){
				AuditRecord auditRecord = new AuditRecord();
				auditRecord.setApplyId(id);
				auditRecord.setApplyType(AuditRecordTypeEnum.CanteenLogisticsAudit.getValue());
				auditRecord.setStatus(ApproveStatusEnum.FAIL.getValue()+"");
				auditRecord.setCreateBy(sysUser.getLoginName());
				auditRecord.setUpdateBy(sysUser.getLoginName());
				auditRecordService.insertAuditRecord(auditRecord);
			}
			//???????????????
			if(CollUtil.isEmpty(kRoles)){
				AuditRecord auditRecord = new AuditRecord();
				auditRecord.setApplyId(id);
				auditRecord.setApplyType(AuditRecordTypeEnum.CanteenKitchenAudit.getValue());
				auditRecord.setStatus(ApproveStatusEnum.FAIL.getValue()+"");
				auditRecord.setCreateBy(sysUser.getLoginName());
				auditRecord.setUpdateBy(sysUser.getLoginName());
				auditRecordService.insertAuditRecord(auditRecord);
			}
		}
		return msg;
	}

	private SysRole getSysRole(String roleKey){
		return sysRoleMapper.checkRoleKeyUnique(roleKey);
	}

	@Override
	public List<NameCountResult> selectDayCount(CanteenManageParam param) {

		List<NameCountResult> result = canteenManageMapper.selectDayCount(param);
		result.stream().forEach(o -> o.setStatus(false));
		return result;
	}

	/**
	 * ????????????
	 * @param param
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int auditByParam(ApproveParam param) {

		CanteenManage canteenManage = canteenManageMapper.selectCanteenManageById(param.getId());

		String boxId = canteenManage.getBoxId();
		Long boxMealId = canteenManage.getBoxMealId();


		Long targetId = param.getTargetId();
		// ??????????????????
		eventAuditRecordService.updateEventAuditRecordStatus(
				StringUtil.toString(canteenManage.getProgress()),
				Math.toIntExact(canteenManage.getCanteenApplyId()),
				AuditRecordTypeEnum.CANTEEN_AUDIT.getValue(),
				StringUtil.toString(param.getAuditStatus()),
				param.getReason());

		WfEventDetail nextEvent = eventDetailService.selectNextEventDetail(AuditRecordTypeEnum.CANTEEN_AUDIT.getValue(), canteenManage.getProgress());
		// ????????????
		if (BeanUtil.isNotEmpty(nextEvent)){
			// ?????????????????????
			if (StringUtils.equals(KeyConstant.EVENT_AUDIT_STATUS_FAIL,param.getAuditStatus().toString())){
				canteenManage.setApplyStatus(param.getAuditStatus());
			} else {
				SysUser targetUser = userService.selectUserById(targetId);
				if (BeanUtil.isEmpty(targetUser)){
					throw new NullPointerException("?????????????????????????????????????????????ID???" + targetId + "????????????????????????????????????????????????");
				}

				canteenManage.setProgress(Integer.valueOf(nextEvent.getApprovalOrder()));

				EventAuditRecord insertRecord = new EventAuditRecord();
				insertRecord.setApplyId(Math.toIntExact(canteenManage.getCanteenApplyId()));
				insertRecord.setApplyType(AuditRecordTypeEnum.CANTEEN_AUDIT.getValue());
				insertRecord.setStatus(KeyConstant.EVENT_AUDIT_STATUS_WAIT);
				insertRecord.setApprovalOrder(nextEvent.getApprovalOrder());
				insertRecord.setApprovalUserId(param.getTargetId().intValue());
				insertRecord.setCreateBy(targetUser.getLoginName());
				insertRecord.setUpdateBy(targetUser.getLoginName());
				eventAuditRecordService.insertEventAuditRecord(insertRecord);
			}
		}else {
			// ????????????
			canteenManage.setApplyStatus(param.getAuditStatus());
		}
		return canteenManageMapper.updateCanteenManage(canteenManage);
	}

	@Override
	public List<EventAuditRecord> selectAuditList(Long applyId) {
		List<EventAuditRecord> eventAuditRecords = eventAuditRecordService.selectAuditEventLit(AuditRecordTypeEnum.CANTEEN_AUDIT.getValue(), Math.toIntExact(applyId));
		CanteenManage defaultApply = this.selectCanteenManageById(applyId);
		EventAuditRecord defaultRecord = new EventAuditRecord();
		defaultRecord.setStatus(KeyConstant.EVENT_AUDIT_STATUS_WAIT);
		defaultRecord.setApprovalUserId(Math.toIntExact(defaultApply.getUserId()));
		defaultRecord.setApprovalObjectName("????????????");
		defaultRecord.setApprovalOrder(KeyConstant.EVENT_ZERO);
		SysUser user = userService.selectUserById(Long.valueOf(defaultApply.getCreateBy()));
		if (BeanUtil.isNotEmpty(user)){
			defaultRecord.setApprovalUserName(user.getUserName());
		}
		defaultRecord.setUpdateTime(defaultApply.getCreateTime());
		eventAuditRecords.add(0,defaultRecord);
		return eventAuditRecords;
	}

	@Override
	public List<CanteenResult> selectApplyAuditListByParam(CanteenManageParam param) {
		List<CanteenResult> canteenResults = canteenManageMapper.selectAuditListByParam(param);
		canteenResults.stream().forEach(r -> {
			Long canteenApplyId = r.getCanteenApplyId();
			CanteenManage canteenManage = canteenManageMapper.selectCanteenManageById(canteenApplyId);
			BoxMeal boxMeal = boxMealService.selectBoxMealById(canteenManage.getBoxMealId());
			r.setTypeName(boxMeal.getTypeName());
			ArraignRoom room = roomService.selectArraignRoomById(boxMeal.getBoxId());
			if (BeanUtil.isNotEmpty(room)){
				r.setName(room.getName());
			}
		});
		return canteenResults;
	}



}
