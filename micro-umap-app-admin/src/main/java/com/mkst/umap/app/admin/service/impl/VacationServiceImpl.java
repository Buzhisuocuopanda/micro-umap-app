package com.mkst.umap.app.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.umap.app.admin.api.bean.param.vacation.VacationParam;
import com.mkst.umap.app.admin.api.bean.result.vacation.VacationResult;
import com.mkst.umap.app.admin.domain.AuditRecord;
import com.mkst.umap.app.admin.domain.Vacation;
import com.mkst.umap.app.admin.mapper.VacationMapper;
import com.mkst.umap.app.admin.service.IAuditRecordService;
import com.mkst.umap.app.admin.service.IVacationService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.enums.AuditRecordTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 请假 服务层实现
 *
 * @author wangyong
 * @date 2020-08-24
 */
@Service
public class VacationServiceImpl implements IVacationService {
	@Autowired
	private VacationMapper vacationMapper;

	@Autowired
	private IAuditRecordService auditRecordService;

	/**
	 * 查询请假信息
	 *
	 * @param id 请假ID
	 * @return 请假信息
	 */
	@Override
	public Vacation selectVacationById(Long id) {
		return vacationMapper.selectVacationById(id);
	}

	/**
	 * 查询请假列表
	 *
	 * @param vacation 请假信息
	 * @return 请假集合
	 */
	@Override
	public List<Vacation> selectVacationList(Vacation vacation) {
		return vacationMapper.selectVacationList(vacation);
	}

	/**
	 * 新增请假
	 *
	 * @param vacation 请假信息
	 * @return 结果
	 */
	@Override
	public int insertVacation(Vacation vacation) {
		return vacationMapper.insertVacation(vacation);
	}

	/**
	 * 修改请假
	 *
	 * @param vacation 请假信息
	 * @return 结果
	 */
	@Override
	public int updateVacation(Vacation vacation) {
		return vacationMapper.updateVacation(vacation);
	}

	/**
	 * 删除请假对象
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deleteVacationByIds(String ids) {
		return vacationMapper.deleteVacationByIds(Convert.toStrArray(ids));
	}


	@Override
	public List<VacationResult> selectVacationInfoList(Vacation selectVacation) {
		return vacationMapper.selectVacationInfo(selectVacation);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int audit(String loginName, Long userId, VacationParam vacationParam) {

		List<Long> idList = Arrays.asList(vacationParam.getIdArr());
		AtomicInteger realRow = new AtomicInteger();

		idList.stream().forEach(id -> {
			// 防止二次审核 防止审核已取消的申请
			Vacation vacation = this.selectVacationById(id);
			if (vacation == null
					|| !vacation.getAuditStatus().equals(KeyConstant.EVENT_AUDIT_STATUS_WAIT)
					|| vacation.getStatus().equals(KeyConstant.EVENT_IS_CANCEL_TRUE)) {
				return;
			}

			// id auditStatus
			Vacation updateVacation = transObject(vacationParam, Vacation.class);
			updateVacation.setId(id);
			updateVacation.setUpdateBy(userId);
			// 更新vacation数据
			this.updateVacation(updateVacation);

			// 插入审核记录表
			AuditRecord insertRecord = new AuditRecord(id, AuditRecordTypeEnum.VacationApplyAudit.getValue(),
					vacationParam.getAuditStatus(), vacationParam.getReason());
			insertRecord.setCreateBy(loginName);
			insertRecord.setUpdateBy(loginName);
			int row = auditRecordService.insertAuditRecord(insertRecord);
			if (row > 0) {
				realRow.getAndIncrement();
			}
		});
		return realRow.get();
	}

	public <T> T transObject(Object ob, Class<T> clazz) {
		String oldOb = JSON.toJSONString(ob);
		return JSON.parseObject(oldOb, clazz);
	}

}
