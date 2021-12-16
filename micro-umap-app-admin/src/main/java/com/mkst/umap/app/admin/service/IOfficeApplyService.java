package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.api.bean.param.officeapply.OfficeApplyParam;
import com.mkst.umap.app.admin.api.bean.result.officeapply.OfficeApplyDetailResult;
import com.mkst.umap.app.admin.domain.OfficeApply;
import com.mkst.umap.app.admin.dto.meeting.MeetingAuditProgressInfoDto;
import com.mkst.umap.app.admin.dto.officeapply.MyApplyDto;
import com.mkst.umap.app.admin.dto.officeapply.MyAuditDto;

import java.util.List;

/**
 * 办公申请 服务层
 *
 * @author wangyong
 * @date 2020-08-07
 */
public interface IOfficeApplyService {
	/**
	 * 查询办公申请信息
	 *
	 * @param id 办公申请ID
	 * @return 办公申请信息
	 */
	public OfficeApply selectOfficeApplyById(Long id);

	/**
	 * 查询办公申请列表
	 *
	 * @param officeApply 办公申请信息
	 * @return 办公申请集合
	 */
	public List<OfficeApply> selectOfficeApplyList(OfficeApply officeApply);

	/**
	 * 新增办公申请
	 *
	 * @param officeApply 办公申请信息
	 * @return 结果
	 */
	public int insertOfficeApply(OfficeApply officeApply);

	/**
	 * 修改办公申请
	 *
	 * @param officeApply 办公申请信息
	 * @return 结果
	 */
	public int updateOfficeApply(OfficeApply officeApply);

    /**
	 * 删除办公申请信息
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	public int deleteOfficeApplyByIds(String ids);

	List<MyAuditDto> selectApplyAuditListByParam(OfficeApplyParam officeApplyParam);

	List<MyApplyDto> selectMyApplyByParam(OfficeApplyParam officeApplyParam);

	int updateApplyByParam(OfficeApplyParam officeApplyParam);

    OfficeApplyDetailResult selectDetail(Long id);

    List<MeetingAuditProgressInfoDto> selectAuditList(Long applyId);

}
