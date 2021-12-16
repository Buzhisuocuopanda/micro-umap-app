package com.mkst.umap.app.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.mkst.mini.systemplus.basic.domain.content.SmsMsgContent;
import com.mkst.mini.systemplus.basic.utils.MsgPushUtils;
import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.mini.systemplus.common.util.DictUtil;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.mini.systemplus.workflow.domain.WfEventDetail;
import com.mkst.mini.systemplus.workflow.service.IWfEventDetailService;
import com.mkst.umap.app.admin.api.bean.param.officeapply.OfficeApplyParam;
import com.mkst.umap.app.admin.api.bean.result.officeapply.OfficeApplyDetailResult;
import com.mkst.umap.app.admin.api.bean.result.officeapply.OfficeApplyInfoResult;
import com.mkst.umap.app.admin.domain.AuditRecord;
import com.mkst.umap.app.admin.domain.OfficeApply;
import com.mkst.umap.app.admin.domain.OfficeApplyDevice;
import com.mkst.umap.app.admin.dto.meeting.MeetingAuditProgressInfoDto;
import com.mkst.umap.app.admin.dto.officeapply.MyApplyDto;
import com.mkst.umap.app.admin.dto.officeapply.MyAuditDto;
import com.mkst.umap.app.admin.mapper.OfficeApplyMapper;
import com.mkst.umap.app.admin.service.IAuditRecordService;
import com.mkst.umap.app.admin.service.IOfficeApplyDeviceService;
import com.mkst.umap.app.admin.service.IOfficeApplyService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.enums.AuditRecordTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 办公申请 服务层实现
 *
 * @author wangyong
 * @date 2020-08-07
 */
@Service
public class OfficeApplyServiceImpl implements IOfficeApplyService {
	@Autowired
	private OfficeApplyMapper officeApplyMapper;

	@Autowired
	private IAuditRecordService auditRecordService;

	@Autowired
	private IOfficeApplyDeviceService officeApplyDeviceService;

	@Autowired
	private ISysUserService userService;

	@Autowired
	private IWfEventDetailService eventDetailService;

	/**
	 * 查询办公申请信息
	 *
	 * @param id 办公申请ID
	 * @return 办公申请信息
	 */
	@Override
	public OfficeApply selectOfficeApplyById(Long id) {
		return officeApplyMapper.selectOfficeApplyById(id);
	}

	/**
	 * 查询办公申请列表
	 *
	 * @param officeApply 办公申请信息
	 * @return 办公申请集合
	 */
	@Override
	public List<OfficeApply> selectOfficeApplyList(OfficeApply officeApply) {
		return officeApplyMapper.selectOfficeApplyList(officeApply);
	}

	/**
	 * 新增办公申请
	 *
	 * @param officeApply 办公申请信息
	 * @return 结果
	 */
	@Override
	public int insertOfficeApply(OfficeApply officeApply) {
		return officeApplyMapper.insertOfficeApply(officeApply);
	}

	/**
	 * 修改办公申请
	 *
	 * @param officeApply 办公申请信息
	 * @return 结果
	 */
	@Override
	public int updateOfficeApply(OfficeApply officeApply) {
		return officeApplyMapper.updateOfficeApply(officeApply);
	}

	/**
	 * 删除办公申请对象
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deleteOfficeApplyByIds(String ids) {
		return officeApplyMapper.deleteOfficeApplyByIds(Convert.toStrArray(ids));
	}

	@Override
	public List<MyAuditDto> selectApplyAuditListByParam(OfficeApplyParam officeApplyParam) {
		List<OfficeApplyInfoResult> applyResults = officeApplyMapper.selectAuditList(officeApplyParam);
		List<MyAuditDto> myAuditDtos = transList(applyResults, MyAuditDto.class);
		if (CollectionUtils.isEmpty(myAuditDtos)) {
			return new ArrayList<>();
		}
		return myAuditDtos;
	}

	@Override
	public List<MyApplyDto> selectMyApplyByParam(OfficeApplyParam officeApplyParam) {

		List<OfficeApplyInfoResult> applyInfoResults = officeApplyMapper.selectApplyInfoListByParam(officeApplyParam);
		List<MyApplyDto> myApplyDtos = transList(applyInfoResults, MyApplyDto.class);
		if (CollectionUtils.isEmpty(myApplyDtos)) {
			return new ArrayList<>();
		}
		return myApplyDtos;
	}

	/**
	 * @author wangyong
	 * @description 审核办公申请
	 * @date 10:05 2020-11-19
	 * @param officeApplyParam
	 * @return int
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateApplyByParam(OfficeApplyParam officeApplyParam) {
		Long targetId = officeApplyParam.getTargetId();
		OfficeApply officeApply = this.selectOfficeApplyById(officeApplyParam.getId());
		WfEventDetail nextEvent = eventDetailService.selectNextEventDetail(AuditRecordTypeEnum.OfficeApplyAudit.getValue(), Integer.parseInt(officeApply.getProgress()));

		// 有下一步
		if (BeanUtil.isNotEmpty(nextEvent)){
			auditRecordService.updateRecordInfo(AuditRecordTypeEnum.OfficeApplyAudit.getValue(),officeApply.getId(),officeApply.getProgress(),officeApplyParam.getAuditStatus(),officeApplyParam.getReason());
			// 如果审核结果是驳回，那直接是最后一步
			if (StringUtils.equals(KeyConstant.EVENT_AUDIT_STATUS_FAIL,officeApplyParam.getAuditStatus())){
				officeApply.setAuditStatus(officeApplyParam.getAuditStatus());
			}else {
				SysUser targetUser = userService.selectUserById(targetId);
				if (BeanUtil.isEmpty(targetUser)){
					throw new NullPointerException("当前系统中不存在该审核员（用户ID：" + targetId + "），请选择其他人或联系管理员！！");
				}

				officeApply.setProgress(nextEvent.getApprovalOrder());

				// 新增审核表数据
				AuditRecord auditRecord = new AuditRecord(officeApply.getId(),AuditRecordTypeEnum.OfficeApplyAudit.getValue(),KeyConstant.EVENT_AUDIT_STATUS_WAIT,"");
				SysUser user = userService.selectUserById(officeApplyParam.getTargetId());
				if (BeanUtil.isEmpty(user)){
					throw new NullPointerException("当前系统中不存在该审核员（用户ID：" + targetId + "），请选择其他人或联系管理员！！");
				}
				auditRecord.setProgress(nextEvent.getApprovalOrder());
				auditRecord.setCreateBy(user.getLoginName());
				auditRecord.setUpdateBy(user.getLoginName());
				auditRecordService.insertAuditRecord(auditRecord);
			}
		}else {
			// 修改审核表数据
			auditRecordService.updateRecordInfo(AuditRecordTypeEnum.OfficeApplyAudit.getValue(),officeApply.getId(),officeApply.getProgress(),officeApplyParam.getAuditStatus(),officeApplyParam.getReason());
			// 随后应修改申请的状态
			officeApply.setAuditStatus(officeApplyParam.getAuditStatus());
			if (KeyConstant.EVENT_AUDIT_STATUS_PASS.equals(officeApplyParam.getAuditStatus())){

				String type = DictUtil.getDictLabel("office_apply_type", officeApply.getType());
				SmsMsgContent msgContent = new SmsMsgContent();
				msgContent.setTitle("办公申请");
				msgContent.setContent("有一条您提交的"+ type+"已通过审核！！！");
				MsgPushUtils.push(msgContent, officeApply.getId().toString(), "OFFICEAPPLY", officeApply.getCreateBy());
				MsgPushUtils.getMsgPushTask().execute();
			}
		}

		return officeApplyMapper.updateOfficeApply(officeApply);
	}

	@Override
	public OfficeApplyDetailResult selectDetail(Long id) {
		OfficeApplyDetailResult officeApplyDetailResult = officeApplyMapper.selectDetailId(id);
		OfficeApplyDevice officeApplyDevice = new OfficeApplyDevice();
		officeApplyDevice.setParentId(id);
		List<OfficeApplyDevice> officeApplyDevices = officeApplyDeviceService.selectOfficeApplyDeviceList(officeApplyDevice);
		officeApplyDetailResult.setDeviceList(officeApplyDevices);
		return officeApplyDetailResult;
	}

	@Override
	public List<MeetingAuditProgressInfoDto> selectAuditList(Long applyId) {
		OfficeApplyParam officeApplyParam = new OfficeApplyParam();
		officeApplyParam.setEventCode(AuditRecordTypeEnum.OfficeApplyAudit.getValue());
		officeApplyParam.setId(applyId);
		List<MeetingAuditProgressInfoDto> meetingAuditProgressInfoDtos = officeApplyMapper.selectAuditListByParam(officeApplyParam);

		MeetingAuditProgressInfoDto defaultDto = new MeetingAuditProgressInfoDto();
		OfficeApply officeApply = this.selectOfficeApplyById(applyId);
		defaultDto.setMAuditStatus(KeyConstant.EVENT_AUDIT_STATUS_WAIT);
		defaultDto.setPCreateBy(officeApply.getCreateBy());
		defaultDto.setPCreateTime(officeApply.getCreateTime());
		defaultDto.setPProgress(0);
		defaultDto.setRStatus(KeyConstant.EVENT_AUDIT_STATUS_WAIT);
		defaultDto.setRoleName("提交");
		defaultDto.setUserName(userService.selectUserByLoginName(officeApply.getCreateBy()).getUserName());
		meetingAuditProgressInfoDtos.add(0,defaultDto);
		return meetingAuditProgressInfoDtos;
	}


	public <T> List<T> transList(List<?> list, Class<T> clazz) {
		String oldOb = JSON.toJSONString(list);
		return JSON.parseArray(oldOb, clazz);
	}
}
